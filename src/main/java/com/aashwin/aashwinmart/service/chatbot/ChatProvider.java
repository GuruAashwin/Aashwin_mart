package com.aashwin.aashwinmart.service.chatbot;

/**
 * Provider interface for AI Chatbot response generation.
 */
public interface ChatProvider {

    /**
     * Generates an intelligent reply to the user's marketplace inquiry.
     *
     * @param userMessage Cleaned message from buyer/seller
     * @param context Real-time marketplace context (catalog, categories, seller policy)
     * @return AI assistant reply
     */
    String getReply(String userMessage, String context);
}
