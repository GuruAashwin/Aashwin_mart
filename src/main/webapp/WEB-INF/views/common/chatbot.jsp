<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- AI Chatbot Floating Button -->
<button id="chat-toggle-btn" class="chatbot-toggle-btn" title="Ask AASHWIN MART Royal Concierge AI" aria-label="Open AI Chat">
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
    </svg>
</button>

<!-- AI Chatbot Floating Panel -->
<div id="chat-panel" class="chat-panel hidden">
    <!-- Chat Header -->
    <div class="chat-header">
        <div class="chat-header-brand">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="m2 4 3 12h14l3-12-6 7-4-7-4 7-6-7zm3 16h14"/>
            </svg>
            <div>
                <div class="chat-header-title">AASHWIN MART AI</div>
                <div class="chat-header-sub">Royal Concierge Assistant</div>
            </div>
        </div>
        <button id="chat-close-btn" class="chat-close-btn" aria-label="Close Chat">&times;</button>
    </div>

    <!-- Chat Messages Scroll Body -->
    <div id="chat-body" class="chat-body">
        <div class="chat-msg ai">
            Greetings! I am the AASHWIN MART Royal Concierge AI. How may I assist your luxury shopping journey today?
        </div>
    </div>

    <!-- Suggested Quick FAQ Chips -->
    <div class="chat-chips">
        <button type="button" class="chat-chip js-chat-chip">What products are available?</button>
        <button type="button" class="chat-chip js-chat-chip">What categories do you have?</button>
        <button type="button" class="chat-chip js-chat-chip">How does checkout work?</button>
        <button type="button" class="chat-chip js-chat-chip">How can I become a seller?</button>
        <button type="button" class="chat-chip js-chat-chip">Can I review a product?</button>
    </div>

    <!-- Chat Input Form -->
    <div class="chat-footer">
        <input type="text" id="chat-input" class="chat-input" placeholder="Ask about products, orders, shipping..." maxlength="300">
        <button id="chat-send-btn" class="chat-send-btn" aria-label="Send Message">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/>
            </svg>
        </button>
    </div>
</div>
