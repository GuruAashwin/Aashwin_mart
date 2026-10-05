package com.aashwin.aashwinmart.service.chatbot;

import com.aashwin.aashwinmart.config.AppConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Real Google Gemini LLM API client for AASHWIN MART chatbot with server-side prompt and timeout.
 */
public class GeminiChatProvider implements ChatProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeminiChatProvider.class);
    private static final String SYSTEM_PROMPT =
            "You are the Royal Concierge AI assistant for AASHWIN MART, an elite luxury multi-seller e-commerce marketplace. " +
            "Your tone is polite, refined, professional, and helpful. " +
            "CRITICAL RESTRICTION: You may ONLY answer questions relating strictly to AASHWIN MART, its products, categories " +
            "(Electronics, Fashion, Home & Living, Accessories, Beauty, Gadgets), orders, cart, checkout, mock payment methods, " +
            "verified reviews, seller listings, and marketplace policies. " +
            "Do NOT answer general trivia, politics, coding questions, recipes, or topics outside AASHWIN MART. " +
            "If an off-topic question is asked, politely redirect the user back to AASHWIN MART marketplace features. " +
            "Keep responses concise (1-3 sentences max).";

    private final String apiKey;
    private final String model;
    private final int timeoutMs;
    private final MockChatProvider fallbackProvider;
    private final Gson gson;

    public GeminiChatProvider() {
        this.apiKey = AppConfig.get("ai.chatbot.gemini.apiKey", "");
        this.model = AppConfig.get("ai.chatbot.gemini.model", "gemini-1.5-flash");
        this.timeoutMs = AppConfig.getInt("ai.chatbot.timeoutMs", 5000);
        this.fallbackProvider = new MockChatProvider();
        this.gson = new Gson();
    }

    public GeminiChatProvider(String apiKey, String model, int timeoutMs) {
        this.apiKey = apiKey != null ? apiKey : "";
        this.model = model != null ? model : "gemini-1.5-flash";
        this.timeoutMs = timeoutMs > 0 ? timeoutMs : 5000;
        this.fallbackProvider = new MockChatProvider();
        this.gson = new Gson();
    }

    @Override
    public String getReply(String userMessage, String context) {
        if (apiKey == null || apiKey.trim().isEmpty() || "your_gemini_api_key_here".equalsIgnoreCase(apiKey.trim())) {
            LOGGER.debug("Gemini API key not configured, utilizing MockChatProvider fallback");
            return fallbackProvider.getReply(userMessage, context);
        }

        try {
            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setConnectTimeout(timeoutMs);
            conn.setReadTimeout(timeoutMs);
            conn.setDoOutput(true);

            // Construct Gemini Request Body
            JsonObject root = new JsonObject();
            JsonObject systemInstruction = new JsonObject();
            JsonArray systemParts = new JsonArray();
            JsonObject systemPart = new JsonObject();
            systemPart.addProperty("text", SYSTEM_PROMPT + (context != null ? "\nContext: " + context : ""));
            systemParts.add(systemPart);
            systemInstruction.add("parts", systemParts);
            root.add("system_instruction", systemInstruction);

            JsonArray contents = new JsonArray();
            JsonObject userContent = new JsonObject();
            userContent.addProperty("role", "user");
            JsonArray userParts = new JsonArray();
            JsonObject textPart = new JsonObject();
            textPart.addProperty("text", userMessage);
            userParts.add(textPart);
            userContent.add("parts", userParts);
            contents.add(userContent);
            root.add("contents", contents);

            String requestJson = gson.toJson(root);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                }

                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                if (candidates != null && candidates.size() > 0) {
                    JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                    JsonObject content = firstCandidate.getAsJsonObject("content");
                    JsonArray parts = content.getAsJsonArray("parts");
                    if (parts != null && parts.size() > 0) {
                        return parts.get(0).getAsJsonObject().get("text").getAsString().trim();
                    }
                }
            } else {
                LOGGER.warn("Gemini API returned HTTP status {}. Falling back to mock provider.", responseCode);
            }
        } catch (Exception e) {
            LOGGER.error("Gemini API call failed or timed out: {}. Using fallback response.", e.getMessage());
        }

        return fallbackProvider.getReply(userMessage, context);
    }
}
