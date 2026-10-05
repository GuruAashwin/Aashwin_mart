package com.aashwin.aashwinmart.service.chatbot;

import java.util.Locale;

/**
 * Intelligent Mock Chatbot Provider with rich rule engine covering 10+ FAQ domains strictly bounded to AASHWIN MART.
 */
public class MockChatProvider implements ChatProvider {

    private static final String DOMAIN_RESTRICTION_MESSAGE =
            "Welcome to AASHWIN MART! I am your Royal Concierge AI, specialized exclusively in assisting with " +
            "AASHWIN MART products, categories, orders, shipping, and seller marketplace policies. " +
            "How may I assist your shopping journey today?";

    @Override
    public String getReply(String userMessage, String context) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Greetings from AASHWIN MART! How may I assist your luxury shopping experience today?";
        }

        String msg = userMessage.trim().toLowerCase(Locale.ROOT);

        // 1. GREETINGS
        if (msg.matches(".*\\b(hi|hello|hey|greetings|good morning|good evening|namaste)\\b.*")) {
            return "Greetings and welcome to AASHWIN MART — Where Luxury Meets Everyday Shopping! " +
                   "I can help you explore luxury collections, track orders, or learn how to become a seller. What would you like to discover?";
        }

        // 2. PRODUCT CATALOG & AVAILABILITY
        if (msg.contains("what product") || msg.contains("available") || msg.contains("collection") || msg.contains("catalog") || msg.contains("item")) {
            return "AASHWIN MART offers a curated selection of luxury products including Sovereign ANC Wireless Headphones, " +
                   "Crown Royal Automatic Chronographs, Italian Silk Jacquard Blazers, 24K Gold Trim Espresso sets, and Egyptian Cotton Bedding. " +
                   "You can browse all items by clicking 'Shop' in the navigation bar!";
        }

        // 3. CATEGORIES
        if (msg.contains("categor") || msg.contains("department") || msg.contains("type")) {
            return "Our royal departments include: Electronics, Fashion, Home & Living, Accessories, Beauty & Fragrances, and Gadgets. " +
                   "You can use the Category filter on our Shop page to browse specific collections.";
        }

        // 4. HOW TO ADD TO CART
        if (msg.contains("add to cart") || msg.contains("how to add") || msg.contains("buy item")) {
            return "To add an item to your cart, click on any product card to view its details, select your desired quantity, and click the golden 'Add to Cart' button. You can then view your cart anytime via the cart icon in the top navigation.";
        }

        // 5. CHECKOUT & PAYMENT METHODS
        if (msg.contains("checkout") || msg.contains("payment") || msg.contains("pay") || msg.contains("card") || msg.contains("upi") || msg.contains("netbanking")) {
            return "Checkout at AASHWIN MART is seamless and secure. When you proceed from your Cart to Checkout, you will enter your delivery address and choose from Mock Credit/Debit Card, Mock UPI, or Mock NetBanking confirmation. Once confirmed, your royal order is placed instantly!";
        }

        // 6. ORDER TRACKING & HISTORY
        if (msg.contains("order") || msg.contains("track") || msg.contains("history") || msg.contains("status") || msg.contains("delivery")) {
            return "You can view all your placed orders and their live fulfillment statuses (PENDING, CONFIRMED, SHIPPED, DELIVERED) by navigating to 'My Orders' under your Buyer Account menu.";
        }

        // 7. REVIEWS & RATINGS
        if (msg.contains("review") || msg.contains("rating") || msg.contains("star") || msg.contains("feedback")) {
            return "At AASHWIN MART, only verified buyers who have completed an order can submit 1–5 star ratings and reviews. Once your order is fulfilled, visit the product details page or your order history to submit your royal review.";
        }

        // 8. SELLER MARKETPLACE & LISTINGS
        if (msg.contains("seller") || msg.contains("sell") || msg.contains("vendor") || msg.contains("listing") || msg.contains("merchant")) {
            return "Interested in selling at AASHWIN MART? Click 'Become a Seller' in the navigation to register a Seller account. Once registered, access your Seller Dashboard to create listings, manage inventory stock, and fulfill incoming customer orders!";
        }

        // 9. OUT OF STOCK / STOCK POLICIES
        if (msg.contains("out of stock") || msg.contains("stock") || msg.contains("inventory") || msg.contains("sold out")) {
            return "Each product listing shows real-time available stock. If a product is out of stock, orders cannot be placed until the seller replenishes inventory. We prevent any over-ordering through atomic database locks.";
        }

        // 10. RETURNS & REFUNDS POLICY
        if (msg.contains("return") || msg.contains("refund") || msg.contains("exchange") || msg.contains("cancel")) {
            return "AASHWIN MART guarantees a 14-day Royal Satisfaction Guarantee on all luxury items. If you wish to cancel an order before dispatch or request an exchange, you can manage this from your Order Details dashboard or contact support.";
        }

        // 11. CONTACT & SUPPORT
        if (msg.contains("contact") || msg.contains("support") || msg.contains("help") || msg.contains("email") || msg.contains("phone")) {
            return "Our Concierge Support team is available 24/7. You can reach us at concierge@aashwinmart.com or ask any shopping question right here in this chat!";
        }

        // 12. DOMAIN RESTRICTION FALLBACK for off-topic queries
        return DOMAIN_RESTRICTION_MESSAGE;
    }
}
