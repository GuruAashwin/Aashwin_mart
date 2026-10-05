<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="AASHWIN MART — Where Luxury Meets Everyday Shopping" />
</jsp:include>

<!-- Hero Section -->
<section class="hero-section">
    <div class="container">
        <div class="hero-grid">
            <div>
                <div class="hero-badge">
                    <span>👑 Authentic Luxury &amp; Multi-Seller Marketplace</span>
                </div>
                <h1 class="hero-title">Where Luxury Meets Everyday Shopping</h1>
                <p class="hero-subtitle">
                    Discover handpicked luxury timepieces, bespoke haute fashion, artisan home living, and precision audio engineered by premier verified merchants.
                </p>
                <div class="hero-ctas">
                    <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-lg">Shop Now</a>
                    <a href="${pageContext.request.contextPath}/register-seller" class="btn btn-secondary btn-lg">Become a Seller</a>
                </div>
            </div>

            <div class="hero-card">
                <div class="hero-img-wrap">
                    <img src="https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=800&auto=format&fit=crop&q=80" alt="Luxury Horology">
                </div>
                <div style="margin-top: 1rem; display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <div style="font-family: var(--font-serif); font-weight: 700; font-size: 1.1rem;">Crown Royal Automatic</div>
                        <div style="font-size: 0.8rem; color: var(--text-muted);">Verified Merchant: Crown &amp; Co.</div>
                    </div>
                    <div class="text-gold" style="font-weight: 700; font-size: 1.2rem;">$1,850.00</div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Trust & Features Bar -->
<section class="trust-bar">
    <div class="container">
        <div class="trust-grid">
            <div class="trust-item">
                <div class="trust-icon-box">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
                    </svg>
                </div>
                <div>
                    <div class="trust-title">100% Certified Luxury</div>
                    <div class="trust-desc">Rigorous quality verification for all listed items.</div>
                </div>
            </div>

            <div class="trust-item">
                <div class="trust-icon-box">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/>
                    </svg>
                </div>
                <div>
                    <div class="trust-title">Royal Delivery</div>
                    <div class="trust-desc">Complimentary expedited delivery on all orders.</div>
                </div>
            </div>

            <div class="trust-item">
                <div class="trust-icon-box">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                    </svg>
                </div>
                <div>
                    <div class="trust-title">Secure Transactions</div>
                    <div class="trust-desc">Encrypted checkout with mock payment safety.</div>
                </div>
            </div>

            <div class="trust-item">
                <div class="trust-icon-box">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
                    </svg>
                </div>
                <div>
                    <div class="trust-title">AI Royal Concierge</div>
                    <div class="trust-desc">Instant 24/7 shopping assistance and guidance.</div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Featured Products Section -->
<section class="section">
    <div class="container">
        <div class="section-header">
            <span class="section-tag">Curated Selections</span>
            <h2 class="section-title">Trending Royal Masterpieces</h2>
            <p>Explore exclusive, finely crafted pieces from our verified premier sellers.</p>
        </div>

        <div class="product-grid">
            <c:forEach var="prod" items="${featuredProducts}">
                <div class="product-card">
                    <div class="product-thumb">
                        <a href="${pageContext.request.contextPath}/product?id=${prod.id}">
                            <img src="<c:out value='${prod.imageUrl}' />" alt="<c:out value='${prod.name}' />" loading="lazy">
                        </a>
                        <span class="product-category-tag"><c:out value="${prod.category}" /></span>
                        <c:choose>
                            <c:when test="${prod.stockQty > 0}">
                                <span class="stock-badge stock-in">In Stock (<c:out value="${prod.stockQty}" />)</span>
                            </c:when>
                            <c:otherwise>
                                <span class="stock-badge stock-out">Sold Out</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="product-body">
                        <div class="product-seller">By <c:out value="${prod.sellerName}" /></div>
                        <h3 class="product-name">
                            <a href="${pageContext.request.contextPath}/product?id=${prod.id}"><c:out value="${prod.name}" /></a>
                        </h3>

                        <div class="product-rating">
                            <span class="stars">★★★★★</span>
                            <span>(<fmt:formatNumber value="${prod.averageRating}" pattern="0.0"/>)</span>
                        </div>

                        <div class="product-footer">
                            <div class="product-price">₹<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></div>
                            <c:if test="${prod.stockQty > 0}">
                                <button class="btn btn-primary btn-sm js-ajax-add-cart" data-product-id="${prod.id}">
                                    Add to Cart
                                </button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div style="text-align: center; margin-top: 3rem;">
            <a href="${pageContext.request.contextPath}/shop" class="btn btn-secondary btn-lg">Explore Full Catalog</a>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
