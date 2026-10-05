<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${product.name} — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding-top: 2.5rem; padding-bottom: 4rem;">
    <!-- Breadcrumbs -->
    <nav style="margin-bottom: 1.5rem; font-size: 0.85rem; color: var(--text-muted);">
        <a href="${pageContext.request.contextPath}/">Home</a> &nbsp;/&nbsp;
        <a href="${pageContext.request.contextPath}/shop">Shop</a> &nbsp;/&nbsp;
        <a href="${pageContext.request.contextPath}/shop?category=${product.category}"><c:out value="${product.category}"/></a> &nbsp;/&nbsp;
        <span style="color: var(--text-main); font-weight: 600;"><c:out value="${product.name}"/></span>
    </nav>

    <!-- Success / Error Alerts -->
    <c:if test="${param.reviewSuccess == 'true'}">
        <div class="alert alert-success">
            ✨ Thank you! Your verified product review and rating have been submitted successfully.
        </div>
    </c:if>
    <c:if test="${param.reviewError != null}">
        <div class="alert alert-danger">
            <c:out value="${param.reviewError}" />
        </div>
    </c:if>

    <!-- Product Main Details Grid -->
    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 3.5rem; align-items: start; background-color: #FFFFFF; padding: 2.5rem; border-radius: var(--radius-lg); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm); margin-bottom: 3rem;">
        <!-- Left: Image Gallery -->
        <div style="background-color: #FAF8F2; border-radius: var(--radius-md); overflow: hidden; border: 1px solid var(--gold-border); height: 460px;">
            <img src="<c:out value='${product.imageUrl}' />" alt="<c:out value='${product.name}' />" style="width: 100%; height: 100%; object-fit: cover;">
        </div>

        <!-- Right: Information & Actions -->
        <div>
            <div style="display: flex; gap: 0.5rem; align-items: center; margin-bottom: 0.75rem;">
                <span class="product-category-tag" style="position: static;"><c:out value="${product.category}"/></span>
                <c:choose>
                    <c:when test="${product.stockQty > 0}">
                        <span class="badge" style="background-color: #ECFDF5; color: #065F46; border: 1px solid #A7F3D0;">
                            ✓ In Stock (<c:out value="${product.stockQty}"/> units available)
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge" style="background-color: #FEF2F2; color: #991B1B; border: 1px solid #FCA5A5;">
                            Out of Stock
                        </span>
                    </c:otherwise>
                </c:choose>
            </div>

            <h1 style="font-size: 2rem; margin-bottom: 0.75rem; color: var(--text-main);"><c:out value="${product.name}"/></h1>

            <!-- Ratings Summary -->
            <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 1.5rem;">
                <div class="product-rating" style="margin-bottom: 0;">
                    <span class="stars" style="font-size: 1.1rem;">★★★★★</span>
                    <span style="font-weight: 700; font-size: 0.95rem;"><fmt:formatNumber value="${product.averageRating}" pattern="0.0"/> / 5.0</span>
                </div>
                <span style="color: var(--text-muted); font-size: 0.85rem;">(<c:out value="${product.reviewCount}"/> verified customer reviews)</span>
            </div>

            <!-- Price -->
            <div style="font-size: 2.2rem; font-weight: 800; color: var(--text-main); font-family: var(--font-serif); margin-bottom: 1.5rem;">
                ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
            </div>

            <!-- Description -->
            <div style="margin-bottom: 2rem; line-height: 1.7; color: var(--text-muted); border-top: 1px solid var(--border-light); border-bottom: 1px solid var(--border-light); padding: 1.25rem 0;">
                <p><c:out value="${product.description}"/></p>
            </div>

            <!-- Seller Info -->
            <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 2rem; padding: 0.75rem 1rem; background-color: var(--bg-secondary); border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="color: var(--gold-dark);">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>
                </svg>
                <span style="font-size: 0.9rem;">Verified Merchant: <strong><c:out value="${product.sellerName}"/></strong></span>
            </div>

            <!-- Add to Cart Form -->
            <c:choose>
                <c:when test="${product.stockQty > 0}">
                    <form action="${pageContext.request.contextPath}/cart/add" method="POST" style="display: flex; gap: 1rem; align-items: center;">
                        <input type="hidden" name="productId" value="${product.id}">
                        <input type="hidden" name="returnUrl" value="/product?id=${product.id}">
                        <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">

                        <div style="display: flex; align-items: center; border: 1px solid var(--border-color); border-radius: var(--radius-sm); background: #FFFFFF;">
                            <button type="button" class="js-qty-btn" data-action="dec" style="padding: 0.7rem 0.9rem; border: none; background: transparent; cursor: pointer; font-weight: bold;">-</button>
                            <input type="number" id="product-qty" name="quantity" value="1" min="1" max="${product.stockQty}" style="width: 50px; text-align: center; border: none; font-weight: 600;" readonly>
                            <button type="button" class="js-qty-btn" data-action="inc" style="padding: 0.7rem 0.9rem; border: none; background: transparent; cursor: pointer; font-weight: bold;">+</button>
                        </div>

                        <button type="button" class="btn btn-primary btn-lg js-ajax-add-cart" data-product-id="${product.id}" style="flex: 1;">
                            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/><path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"/>
                            </svg>
                            Add to Cart
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-dark btn-lg btn-block" disabled style="opacity: 0.6; cursor: not-allowed;">
                        Out of Stock
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Reviews Section (F8) -->
    <div style="background-color: #FFFFFF; padding: 2.5rem; border-radius: var(--radius-lg); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
            <div>
                <h2 style="font-size: 1.6rem; margin-bottom: 0.25rem;">Verified Customer Reviews</h2>
                <p>Authentic feedback from verified purchasers of this item</p>
            </div>
            <div style="text-align: right;">
                <div style="font-size: 2rem; font-weight: 800; font-family: var(--font-serif); color: var(--gold-dark);">
                    <fmt:formatNumber value="${product.averageRating}" pattern="0.0"/> <span style="font-size: 1rem; color: var(--text-muted);">/ 5.0</span>
                </div>
            </div>
        </div>

        <!-- Submit Review Module for Eligible Verified Buyers -->
        <c:if test="${isEligibleToReview}">
            <div style="background-color: var(--gold-light); border: 1px solid var(--gold-border); border-radius: var(--radius-md); padding: 1.5rem; margin-bottom: 2.5rem;">
                <h3 style="font-size: 1.15rem; margin-bottom: 0.5rem; color: var(--text-main);">Share Your Royal Experience</h3>
                <p style="font-size: 0.85rem; margin-bottom: 1rem;">As a verified purchaser, your review helps others make confident luxury selections.</p>

                <form action="${pageContext.request.contextPath}/review/submit" method="POST">
                    <input type="hidden" name="productId" value="${product.id}">
                    <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">

                    <div class="form-group">
                        <label class="form-label">Your Rating (1 to 5 Stars)</label>
                        <div class="js-star-rating" style="display: flex; gap: 0.5rem; font-size: 1.5rem; cursor: pointer; color: #F59E0B;">
                            <input type="hidden" name="rating" value="5">
                            <span class="star-icon active">★</span>
                            <span class="star-icon active">★</span>
                            <span class="star-icon active">★</span>
                            <span class="star-icon active">★</span>
                            <span class="star-icon active">★</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Written Feedback</label>
                        <textarea name="comment" class="form-control" rows="3" placeholder="Describe the craftsmanship, quality, and your overall satisfaction..." required></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary btn-sm">Submit Verified Review</button>
                </form>
            </div>
        </c:if>

        <!-- Reviews List -->
        <c:choose>
            <c:when test="${not empty reviews}">
                <div style="display: flex; flex-direction: column; gap: 1.5rem;">
                    <c:forEach var="rev" items="${reviews}">
                        <div style="padding: 1.25rem; border-radius: var(--radius-md); background-color: #FAFAF8; border: 1px solid var(--border-light);">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
                                <div>
                                    <strong><c:out value="${rev.userName}"/></strong>
                                    <span class="badge" style="background-color: #ECFDF5; color: #065F46; font-size: 0.65rem; margin-left: 0.5rem;">✓ Verified Purchase</span>
                                </div>
                                <div style="font-size: 0.8rem; color: var(--text-muted);">
                                    <fmt:formatDate value="${rev.createdAt}" pattern="MMM dd, yyyy"/>
                                </div>
                            </div>
                            <div style="color: #F59E0B; margin-bottom: 0.5rem; font-size: 0.9rem;">
                                <c:forEach begin="1" end="${rev.rating}">★</c:forEach>
                                <c:forEach begin="${rev.rating + 1}" end="5">☆</c:forEach>
                            </div>
                            <p style="color: var(--text-main); font-size: 0.95rem; line-height: 1.6;"><c:out value="${rev.comment}"/></p>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 2rem; color: var(--text-muted);">
                    <p>No customer reviews yet. Be the first to purchase and review this royal piece!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
