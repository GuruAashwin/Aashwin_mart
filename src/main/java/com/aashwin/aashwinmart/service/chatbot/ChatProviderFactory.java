package com.aashwin.aashwinmart.service.chatbot;

import com.aashwin.aashwinmart.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class implementing the Factory Design Pattern for ChatProvider instantiation.
 */
public final class ChatProviderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatProviderFactory.class);

    private static ChatProvider instance;

    private ChatProviderFactory() {
    }

    public static synchronized ChatProvider getProvider() {
        if (instance == null) {
            String configuredProvider = AppConfig.get("ai.chatbot.provider", "mock").trim().toLowerCase();
            LOGGER.info("Configuring AI Chat Provider: '{}'", configuredProvider);

            if ("gemini".equalsIgnoreCase(configuredProvider)) {
                instance = new GeminiChatProvider();
            } else {
                instance = new MockChatProvider();
            }
        }
        return instance;
    }

    /**
     * Allows setting a specific provider instance (e.g. in tests).
     */
    public static synchronized void setProvider(ChatProvider provider) {
        instance = provider;
    }
}
