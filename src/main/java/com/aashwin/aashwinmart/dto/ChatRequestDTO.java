package com.aashwin.aashwinmart.dto;

import java.io.Serializable;

/**
 * Request payload for AI Chatbot queries.
 */
public class ChatRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;

    public ChatRequestDTO() {
    }

    public ChatRequestDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
