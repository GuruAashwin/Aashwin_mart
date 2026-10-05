<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Shop Luxury Collections — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding-top: 2.5rem; padding-bottom: 4rem;">
    <!-- Page Header & Filter Bar -->
    <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
        <div>
            <h1 style="font-size: 2.2rem; margin-bottom: 0.25rem;">Marketplace Collections</h1>
            <p>Showing <c:out value="${totalCount}" /> luxury products found</p>
        </div>

        <!-- Filter Form -->
        <form action="${pageContext.request.contextPath}/shop" method="GET" style="display: flex; gap: 0.75rem; flex-wrap: wrap;">
            <input type="hidden" name="q" value="<c:out value='${keyword}'/>">

            <!-- Category Filter -->
            <select name="category" class="form-control" style="width: auto;" onchange="this.form.submit()">
                <option value="ALL" ${selectedCategory == 'ALL' ? 'selected' : ''}>All Categories</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="<c:out value='${cat}'/>" ${selectedCategory == cat ? 'selected' : ''}><c:out value='${cat}'/></option>
                </c:forEach>
            </select>

            <!-- Sorting Filter -->
            <select name="sort" class="form-control" style="width: auto;" onchange="this.form.submit()">
                <option value="newest" ${sort == 'newest' ? 'selected' : ''}>Newest Arrivals</option>
                <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                <option value="rating" ${sort == 'rating' ? 'selected' : ''}>Highest Rated</option>
            </select>
        </form>
    </div>

    <!-- Active Filters Badge Chips -->
    <c:if test="${keyword != '' || (selectedCategory != 'ALL' && selectedCategory != '')}">
        <div style="display: flex; gap: 0.5rem; margin-bottom: 1.5rem; align-items: center;">
            <span style="font-size: 0.85rem; font-weight: 600;">Active Filters:</span>
            <c:if test="${keyword != ''}">
                <span class="badge" style="background-color: #FFFFFF; border: 1px solid var(--border-color);">
                    Keyword: "<c:out value="${keyword}" />"
                </span>
            </c:if>
            <c:if test="${selectedCategory != 'ALL' && selectedCategory != ''}">
                <span class="badge" style="background-color: var(--gold-light); border: 1px solid var(--gold-border); color: var(--gold-dark);">
                    Category: <c:out value="${selectedCategory}" />
                </span>
            </c:if>
            <a href="${pageContext.request.contextPath}/shop" style="font-size: 0.8rem; color: var(--status-danger); text-decoration: underline; margin-left: 0.5rem;">Clear All</a>
        </div>
    </c:if>

    <!-- Product Grid -->
    <c:choose>
        <c:when test="${not empty products}">
            <div class="product-grid">
                <c:forEach var="prod" items="${products}">
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
                            <div class="product-seller">Merchant: <c:out value="${prod.sellerName}" /></div>
                            <h3 class="product-name">
                                <a href="${pageContext.request.contextPath}/product?id=${prod.id}"><c:out value="${prod.name}" /></a>
                            </h3>

                            <div class="product-rating">
                                <span class="stars">★★★★★</span>
                                <span>(<fmt:formatNumber value="${prod.averageRating}" pattern="0.0"/> • <c:out value="${prod.reviewCount}"/> reviews)</span>
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

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div style="display: flex; justify-content: center; gap: 0.5rem; margin-top: 3rem;">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="${pageContext.request.contextPath}/shop?page=${i}&q=${keyword}&category=${selectedCategory}&sort=${sort}"
                           class="btn btn-sm ${currentPage == i ? 'btn-primary' : 'btn-secondary'}" style="min-width: 36px;">
                            ${i}
                        </a>
                    </c:forEach>
                </div>
            </c:if>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 4rem 1rem; background-color: #FFFFFF; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="color: var(--text-muted); margin-bottom: 1rem;">
                    <circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/>
                </svg>
                <h3 style="margin-bottom: 0.5rem;">No products match your criteria</h3>
                <p style="margin-bottom: 1.5rem;">Try adjusting your keyword search or category filter to discover more luxury pieces.</p>
                <a href="${pageContext.request.contextPath}/shop" class="btn btn-secondary">View All Products</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
