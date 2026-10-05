package com.aashwin.aashwinmart.dto;

import java.io.Serializable;

/**
 * Response payload for AI Chatbot replies.
 */
public class ChatResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reply;
    private boolean cached;

    public ChatResponseDTO() {
    }

    public ChatResponseDTO(String reply) {
        this.reply = reply;
        this.cached = false;
    }

    public ChatResponseDTO(String reply, boolean cached) {
        this.reply = reply;
        this.cached = cached;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }
}
