<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Shopping Bag — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding-top: 2.5rem; padding-bottom: 4rem;">
    <h1 style="font-size: 2.2rem; margin-bottom: 0.25rem;">Your Shopping Bag</h1>
    <p style="margin-bottom: 2rem;">Review your selected luxury items before entering the royal checkout.</p>

    <c:if test="${sessionScope.cartError != null}">
        <div class="alert alert-danger">
            <c:out value="${sessionScope.cartError}" />
            <c:remove var="cartError" scope="session" />
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <div style="display: grid; grid-template-columns: 1fr 340px; gap: 2.5rem; align-items: start;">
                <!-- Cart Items Table -->
                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Unit Price</th>
                                <th>Quantity</th>
                                <th>Subtotal</th>
                                <th>Remove</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 1rem;">
                                            <img src="<c:out value='${item.productImageUrl}'/>" alt="<c:out value='${item.productName}'/>" style="width: 60px; height: 60px; object-fit: cover; border-radius: var(--radius-sm); border: 1px solid var(--gold-border);">
                                            <div>
                                                <a href="${pageContext.request.contextPath}/product?id=${item.productId}" style="font-weight: 700; color: var(--text-main);">
                                                    <c:out value="${item.productName}"/>
                                                </a>
                                                <div style="font-size: 0.75rem; color: var(--text-muted);">Seller: <c:out value="${item.sellerName}"/></div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/update" method="POST" style="display: flex; align-items: center; gap: 4px;">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                            <button type="button" class="js-qty-btn btn btn-secondary btn-sm" data-action="dec" style="padding: 0.2rem 0.5rem;">-</button>
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.stockQty}" class="js-cart-qty-input form-control" style="width: 50px; text-align: center; padding: 0.3rem;" readonly>
                                            <button type="button" class="js-qty-btn btn btn-secondary btn-sm" data-action="inc" style="padding: 0.2rem 0.5rem;">+</button>
                                        </form>
                                    </td>
                                    <td><strong>₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></strong></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="POST" style="display: inline;">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                            <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--status-danger); border-color: var(--border-color);" title="Remove Item">
                                                ✕
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div style="padding: 1rem; display: flex; justify-content: space-between; border-top: 1px solid var(--border-color);">
                        <a href="${pageContext.request.contextPath}/shop" class="btn btn-secondary btn-sm">← Continue Shopping</a>
                        <form action="${pageContext.request.contextPath}/cart/clear" method="POST">
                            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                            <button type="submit" class="btn btn-secondary btn-sm" style="color: var(--status-danger);" onclick="return confirm('Are you sure you want to clear your entire bag?');">Clear Bag</button>
                        </form>
                    </div>
                </div>

                <!-- Order Summary Sidebar -->
                <div style="background-color: #FFFFFF; border: 1px solid var(--gold-border); border-radius: var(--radius-md); padding: 1.75rem; box-shadow: var(--shadow-sm);">
                    <h3 style="font-size: 1.25rem; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.75rem;">Summary</h3>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 0.75rem; font-size: 0.95rem;">
                        <span style="color: var(--text-muted);">Items Total:</span>
                        <span>₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 1rem; font-size: 0.95rem;">
                        <span style="color: var(--text-muted);">Royal Delivery:</span>
                        <span style="color: var(--status-success); font-weight: 600;">FREE</span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 1.5rem; font-size: 1.25rem; font-weight: 800; border-top: 1px solid var(--border-color); padding-top: 1rem; color: var(--text-main);">
                        <span>Total:</span>
                        <span class="text-gold">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-block btn-lg">
                        Proceed to Checkout →
                    </a>

                    <div style="text-align: center; margin-top: 1rem; font-size: 0.75rem; color: var(--text-muted);">
                        🔒 Safe &amp; Encrypted Mock Payment Simulation
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 5rem 1rem; background-color: #FFFFFF; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                <svg width="56" height="56" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="color: var(--gold-primary); margin-bottom: 1rem;">
                    <circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/>
                    <path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"/>
                </svg>
                <h2 style="font-size: 1.6rem; margin-bottom: 0.5rem;">Your Shopping Bag is Empty</h2>
                <p style="margin-bottom: 1.5rem;">Explore our curated selection of fine pieces and discover timeless luxury.</p>
                <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-lg">Browse Collections</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
