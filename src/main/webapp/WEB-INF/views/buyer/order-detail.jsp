<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Order #ORD-${order.id} — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding-top: 2.5rem; padding-bottom: 4rem; max-width: 900px;">
    <div style="margin-bottom: 1.5rem;">
        <a href="${pageContext.request.contextPath}/orders" style="font-size: 0.85rem; color: var(--text-muted);">← Back to Order History</a>
    </div>

    <!-- Order Header Card -->
    <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 2rem; box-shadow: var(--shadow-sm); margin-bottom: 2rem;">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-color); padding-bottom: 1.25rem; margin-bottom: 1.25rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <span style="font-size: 0.8rem; color: var(--text-muted); text-transform: uppercase;">Official Receipt</span>
                <h1 style="font-size: 1.8rem; margin: 0;">Order #ORD-<c:out value="${order.id}"/></h1>
                <div style="font-size: 0.85rem; color: var(--text-muted);">
                    Placed on <fmt:formatDate value="${order.createdAt}" pattern="MMMM dd, yyyy 'at' hh:mm a"/>
                </div>
            </div>
            <div style="text-align: right;">
                <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 0.25rem;">CURRENT STATUS</div>
                <span class="badge badge-${fn:toLowerCase(order.status)}" style="font-size: 0.85rem; padding: 0.35rem 0.85rem;">
                    <c:out value="${order.status}"/>
                </span>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; margin-bottom: 2rem; font-size: 0.9rem;">
            <div>
                <h3 style="font-size: 1rem; margin-bottom: 0.5rem; color: var(--text-main);">Delivery Destination</h3>
                <p style="white-space: pre-line; line-height: 1.5; color: var(--text-muted);"><c:out value="${order.shippingAddress}"/></p>
            </div>
            <div>
                <h3 style="font-size: 1rem; margin-bottom: 0.5rem; color: var(--text-main);">Payment Information</h3>
                <p style="line-height: 1.5; color: var(--text-muted);">
                    Method: <strong>Royal Mock Payment Simulator</strong><br>
                    Status: <strong style="color: var(--status-success);">Paid &amp; Verified</strong><br>
                    Delivery: <strong>Complimentary Royal Courier</strong>
                </p>
            </div>
        </div>

        <!-- Line Items Table -->
        <h3 style="font-size: 1.15rem; margin-bottom: 1rem; border-top: 1px solid var(--border-light); padding-top: 1.5rem;">Purchased Items</h3>
        <div class="table-responsive" style="margin-bottom: 1.5rem;">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Product Details</th>
                        <th>Unit Price</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                        <th>Review</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${order.items}">
                        <tr>
                            <td>
                                <div style="display: flex; align-items: center; gap: 1rem;">
                                    <img src="<c:out value='${item.productImageUrl}'/>" alt="" style="width: 50px; height: 50px; object-fit: cover; border-radius: var(--radius-sm);">
                                    <div>
                                        <a href="${pageContext.request.contextPath}/product?id=${item.productId}" style="font-weight: 700; color: var(--text-main);">
                                            <c:out value="${item.productName}"/>
                                        </a>
                                        <div style="font-size: 0.75rem; color: var(--text-muted);">Merchant: <c:out value="${item.sellerName}"/></div>
                                    </div>
                                </div>
                            </td>
                            <td>$<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                            <td><c:out value="${item.quantity}"/></td>
                            <td><strong>$<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></strong></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/product?id=${item.productId}" class="btn btn-secondary btn-sm" style="font-size: 0.75rem;">
                                    Review Product ★
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div style="display: flex; justify-content: flex-end; border-top: 1px solid var(--border-color); padding-top: 1rem;">
            <div style="width: 260px; display: flex; flex-direction: column; gap: 0.5rem; font-size: 0.95rem;">
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">Subtotal:</span>
                    <span>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">Shipping:</span>
                    <span style="color: var(--status-success);">FREE</span>
                </div>
                <div style="display: flex; justify-content: space-between; font-size: 1.25rem; font-weight: 800; border-top: 1px solid var(--border-color); padding-top: 0.5rem;">
                    <span>Total:</span>
                    <span class="text-gold">$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
