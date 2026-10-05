package com.aashwin.aashwinmart.service.chatbot;

import com.aashwin.aashwinmart.dto.ChatResponseDTO;
import com.aashwin.aashwinmart.exception.ValidationException;
import com.aashwin.aashwinmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service orchestrating AI Chatbot query processing, rate limiting, session-level question caching,
 * input truncation, and fallback handling.
 */
public class ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private static final int MAX_INPUT_LENGTH = 300;
    private static final int RATE_LIMIT_MAX_REQUESTS = 10;
    private static final long RATE_LIMIT_WINDOW_MS = 60_000L; // 1 minute

    private final ChatProvider chatProvider;

    // Per-session message timestamp history for sliding-window rate limiting: sessionId -> queue of timestamps
    private final Map<String, Deque<Long>> sessionRateLimits = new ConcurrentHashMap<>();

    // Per-session query cache for repeated identical queries: sessionId -> (normalizedQuery -> reply)
    private final Map<String, Map<String, String>> sessionQueryCache = new ConcurrentHashMap<>();

    public ChatService() {
        this.chatProvider = ChatProviderFactory.getProvider();
    }

    public ChatService(ChatProvider chatProvider) {
        this.chatProvider = chatProvider;
    }

    /**
     * Processes a user chat message with rate limiting and session caching.
     *
     * @param sessionId User HTTP session identifier
     * @param message User message text
     * @param context Marketplace catalog context
     * @return ChatResponseDTO with reply and cache flag
     */
    public ChatResponseDTO processMessage(String sessionId, String message, String context) {
        ValidationUtil.requireNonEmpty(message, "Message");

        String safeSessionId = sessionId != null ? sessionId : "anonymous-session";
        String trimmedMessage = message.trim();

        // 1. Enforce max input length cap (300 characters)
        if (trimmedMessage.length() > MAX_INPUT_LENGTH) {
            throw new ValidationException("Message exceeds maximum allowed length of " + MAX_INPUT_LENGTH + " characters.");
        }

        // 2. Enforce session rate limiting (max 10 messages/minute/session)
        checkRateLimit(safeSessionId);

        // 3. Check in-memory repeated-question session cache
        String normalizedQuery = trimmedMessage.toLowerCase();
        Map<String, String> cache = sessionQueryCache.computeIfAbsent(safeSessionId, k -> new ConcurrentHashMap<>());
        if (cache.containsKey(normalizedQuery)) {
            LOGGER.debug("Serving cached AI reply for session {} query '{}'", safeSessionId, normalizedQuery);
            return new ChatResponseDTO(cache.get(normalizedQuery), true);
        }

        // 4. Generate reply via configured ChatProvider
        String reply;
        try {
            reply = chatProvider.getReply(trimmedMessage, context);
            if (reply == null || reply.trim().isEmpty()) {
                reply = "I apologize, but I could not process your request at this moment. How else may I assist you with AASHWIN MART?";
            }
        } catch (Exception e) {
            LOGGER.error("Chat provider encountered an error", e);
            reply = "Welcome to AASHWIN MART! Our Royal Concierge is temporarily operating in standard mode. How may I assist your shopping journey today?";
        }

        // 5. Store in session cache
        cache.put(normalizedQuery, reply);

        return new ChatResponseDTO(reply, false);
    }

    private void checkRateLimit(String sessionId) {
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = sessionRateLimits.computeIfAbsent(sessionId, k -> new ArrayDeque<>());

        synchronized (timestamps) {
            // Remove timestamps older than the sliding window (1 minute)
            while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) > RATE_LIMIT_WINDOW_MS) {
                timestamps.pollFirst();
            }

            if (timestamps.size() >= RATE_LIMIT_MAX_REQUESTS) {
                LOGGER.warn("Rate limit exceeded for session: {}", sessionId);
                throw new ValidationException("You have exceeded the maximum of " + RATE_LIMIT_MAX_REQUESTS +
                        " messages per minute. Please pause for a moment before sending another message.");
            }

            timestamps.addLast(now);
        }
    }

    /**
     * Clears session cache upon user logout or session expiration.
     */
    public void invalidateSession(String sessionId) {
        if (sessionId != null) {
            sessionRateLimits.remove(sessionId);
            sessionQueryCache.remove(sessionId);
        }
    }
}
