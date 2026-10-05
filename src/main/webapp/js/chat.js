/**
 * AASHWIN MART — AI Chatbot Assistant Widget Logic
 */

document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.getElementById('chat-toggle-btn');
    const chatPanel = document.getElementById('chat-panel');
    const closeBtn = document.getElementById('chat-close-btn');
    const sendBtn = document.getElementById('chat-send-btn');
    const chatInput = document.getElementById('chat-input');
    const chatBody = document.getElementById('chat-body');
    const chips = document.querySelectorAll('.js-chat-chip');

    if (!toggleBtn || !chatPanel) return;

    // Toggle Chat Panel visibility
    toggleBtn.addEventListener('click', () => {
        chatPanel.classList.toggle('hidden');
        if (!chatPanel.classList.contains('hidden')) {
            chatInput.focus();
            scrollToBottom();
        }
    });

    closeBtn.addEventListener('click', () => {
        chatPanel.classList.add('hidden');
    });

    // Send Message Handler
    const handleSend = async () => {
        const text = chatInput.value.trim();
        if (!text) return;

        // Append user message
        appendMessage(text, 'user');
        chatInput.value = '';
        chatInput.disabled = true;
        sendBtn.disabled = true;

        // Show typing indicator
        const typingEl = showTypingIndicator();
        scrollToBottom();

        try {
            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 2)) || '';
            const response = await fetch(`${contextPath}/api/v1/chat`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ message: text })
            });

            const result = await response.json();
            typingEl.remove();

            if (result.success && result.data) {
                appendMessage(result.data.reply, 'ai');
            } else if (result.reply) { // support direct reply object
                appendMessage(result.reply, 'ai');
            } else {
                const err = result.error ? result.error.message : 'I could not process your message right now.';
                appendMessage(err, 'ai');
            }
        } catch (e) {
            if (typingEl) typingEl.remove();
            appendMessage('I am currently experiencing a connection delay. Please try asking again in a moment.', 'ai');
        } finally {
            chatInput.disabled = false;
            sendBtn.disabled = false;
            chatInput.focus();
            scrollToBottom();
        }
    };

    sendBtn.addEventListener('click', handleSend);

    chatInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSend();
        }
    });

    // Quick Prompt Chips
    chips.forEach(chip => {
        chip.addEventListener('click', () => {
            chatInput.value = chip.textContent.trim();
            handleSend();
        });
    });

    function appendMessage(text, sender) {
        const msgDiv = document.createElement('div');
        msgDiv.className = `chat-msg ${sender}`;
        msgDiv.textContent = text;
        chatBody.appendChild(msgDiv);
        scrollToBottom();
    }

    function showTypingIndicator() {
        const typingDiv = document.createElement('div');
        typingDiv.className = 'chat-msg ai typing-indicator-msg';
        typingDiv.innerHTML = '<div class="typing-dots"><div class="typing-dot"></div><div class="typing-dot"></div><div class="typing-dot"></div></div>';
        chatBody.appendChild(typingDiv);
        return typingDiv;
    }

    function scrollToBottom() {
        chatBody.scrollTop = chatBody.scrollHeight;
    }
});
