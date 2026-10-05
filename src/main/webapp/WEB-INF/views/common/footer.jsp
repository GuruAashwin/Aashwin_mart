<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</main>

<!-- Footer -->
<footer class="footer">
    <div class="container">
        <div class="footer-grid">
            <div class="footer-brand">
                <a href="${pageContext.request.contextPath}/" class="brand-logo" style="margin-bottom: 1rem;">
                    <svg class="crown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="m2 4 3 12h14l3-12-6 7-4-7-4 7-6-7zm3 16h14"/>
                    </svg>
                    <div class="brand-text">
                        <span class="brand-title" style="color:#FFFFFF;">AASHWIN <span>MART</span></span>
                        <span class="brand-subtitle" style="color:#A3A3A3;">Royal Shopping</span>
                    </div>
                </a>
                <p class="footer-desc">
                    AASHWIN MART is an elite multi-seller e-commerce marketplace delivering authentic luxury, curated lifestyle essentials, and trusted craftsmanship worldwide.
                </p>
            </div>

            <div>
                <h4 class="footer-heading">Marketplace</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/shop" class="footer-link">All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/shop?category=Electronics" class="footer-link">Audio &amp; Tech</a></li>
                    <li><a href="${pageContext.request.contextPath}/shop?category=Fashion" class="footer-link">Haute Fashion</a></li>
                    <li><a href="${pageContext.request.contextPath}/shop?category=Accessories" class="footer-link">Horology &amp; Jewelry</a></li>
                    <li><a href="${pageContext.request.contextPath}/shop?category=Home" class="footer-link">Home &amp; Living</a></li>
                </ul>
            </div>

            <div>
                <h4 class="footer-heading">Partner With Us</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/register-seller" class="footer-link">Become a Seller</a></li>
                    <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="footer-link">Merchant Portal</a></li>
                    <li><a href="${pageContext.request.contextPath}/shop" class="footer-link">Seller Guidelines</a></li>
                    <li><a href="${pageContext.request.contextPath}/" class="footer-link">Affiliate Program</a></li>
                </ul>
            </div>

            <div>
                <h4 class="footer-heading">Customer Care</h4>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/orders" class="footer-link">Track Your Order</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart" class="footer-link">Shopping Bag</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="footer-link">Member Sign In</a></li>
                    <li><a href="${pageContext.request.contextPath}/" class="footer-link">14-Day Royal Guarantee</a></li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            <p>&copy; 2026 AASHWIN MART. Anna University R2025 Semester 3 Java Capstone Project. All Rights Reserved.</p>
        </div>
    </div>
</footer>

<!-- Include AI Chatbot Widget -->
<jsp:include page="/WEB-INF/views/common/chatbot.jsp" />

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script src="${pageContext.request.contextPath}/js/chat.js"></script>
</body>
</html>
