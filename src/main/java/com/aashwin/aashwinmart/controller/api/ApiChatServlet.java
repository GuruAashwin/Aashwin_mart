package com.aashwin.aashwinmart.controller.api;

import com.aashwin.aashwinmart.dto.ApiResponse;
import com.aashwin.aashwinmart.dto.ChatRequestDTO;
import com.aashwin.aashwinmart.dto.ChatResponseDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.service.chatbot.ChatService;
import com.aashwin.aashwinmart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Controller endpoint for the AI Chatbot Assistant.
 * URLs: POST /api/v1/chat and POST /api/chat
 */
@WebServlet(name = "ApiChatServlet", urlPatterns = {"/api/v1/chat", "/api/chat"})
public class ApiChatServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiChatServlet.class);
    private final ChatService chatService;

    public ApiChatServlet() {
        this.chatService = new ChatService();
    }

    public ApiChatServlet(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();

        String message = null;

        // Support both JSON body and form parameters
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            try {
                ChatRequestDTO requestDTO = JsonUtil.fromJson(sb.toString(), ChatRequestDTO.class);
                if (requestDTO != null) {
                    message = requestDTO.getMessage();
                }
            } catch (Exception ignored) {
            }
        } else {
            message = request.getParameter("message");
        }

        if (message == null || message.trim().isEmpty()) {
            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("VALIDATION_ERROR", "Message parameter is required"));
            return;
        }

        try {
            String context = "AASHWIN MART: Luxury Multi-Seller E-Commerce Marketplace. Categories: Electronics, Fashion, Home, Accessories, Beauty, Gadgets.";
            ChatResponseDTO reply = chatService.processMessage(sessionId, message, context);

            // If invoked from standard /api/chat or /api/v1/chat
            if (request.getServletPath().equals("/api/chat")) {
                JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, reply);
            } else {
                JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(reply));
            }
        } catch (AppException e) {
            LOGGER.warn("Chat error for session {}: {}", sessionId, e.getMessage());
            JsonUtil.writeJsonResponse(response, e.getHttpStatus(),
                    ApiResponse.error(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("Unexpected error in chat endpoint", e);
            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("INTERNAL_SERVER_ERROR", "AI Chatbot is currently taking a moment. Please try again."));
        }
    }
}
