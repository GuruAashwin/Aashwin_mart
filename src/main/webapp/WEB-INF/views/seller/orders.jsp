<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Incoming Orders — AASHWIN MART" />
</jsp:include>

<div class="container">
    <div class="dashboard-layout">
        <!-- Sidebar -->
        <aside class="dashboard-sidebar">
            <div class="sidebar-profile">
                <div class="avatar-badge">
                    ${fn:substring(sessionScope.currentUser.name, 0, 1)}
                </div>
                <h3 style="font-size: 1.1rem; margin-bottom: 0.2rem;"><c:out value="${sessionScope.currentUser.name}"/></h3>
                <p style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${sessionScope.currentUser.email}"/></p>
                <span class="badge badge-seller" style="margin-top: 0.5rem;">Verified Merchant</span>
            </div>

            <ul class="sidebar-menu">
                <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="sidebar-link">Overview</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products" class="sidebar-link">My Product Catalog</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products/add" class="sidebar-link">+ Add New Listing</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/orders" class="sidebar-link active">Incoming Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">Incoming Customer Orders</h1>
                <p>Manage fulfillment status transitions for orders containing your products (O2 Status Workflow)</p>
            </div>

            <c:if test="${param.statusUpdated == 'true'}">
                <div class="alert alert-success">✓ Order fulfillment status successfully updated!</div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger"><c:out value="${param.error}"/></div>
            </c:if>

            <c:choose>
                <c:when test="${not empty orders}">
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Order</th>
                                    <th>Customer &amp; Address</th>
                                    <th>Ordered Line Items</th>
                                    <th>Status</th>
                                    <th>Update Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td>
                                            <strong>#ORD-<c:out value="${order.id}"/></strong><br>
                                            <span style="font-size: 0.75rem; color: var(--text-muted);">
                                                <fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy"/>
                                            </span>
                                        </td>
                                        <td>
                                            <strong><c:out value="${order.buyerName}"/></strong><br>
                                            <span style="font-size: 0.75rem; color: var(--text-muted);"><c:out value="${order.buyerEmail}"/></span><br>
                                            <span style="font-size: 0.75rem; color: var(--text-muted); white-space: pre-line;"><c:out value="${order.shippingAddress}"/></span>
                                        </td>
                                        <td>
                                            <c:forEach var="item" items="${order.items}">
                                                <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.4rem;">
                                                    <img src="<c:out value='${item.productImageUrl}'/>" alt="" style="width: 32px; height: 32px; object-fit: cover; border-radius: var(--radius-sm);">
                                                    <div style="font-size: 0.85rem;">
                                                        <c:out value="${item.productName}"/> &times; <strong><c:out value="${item.quantity}"/></strong>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <span class="badge badge-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/seller/orders/status" method="POST" style="display: flex; gap: 0.4rem; align-items: center;">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                                <select name="status" class="form-control" style="padding: 0.35rem 0.6rem; font-size: 0.8rem; width: auto;">
                                                    <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                                    <option value="CONFIRMED" ${order.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                                    <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                                    <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                                    <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                                </select>
                                                <button type="submit" class="btn btn-secondary btn-sm" style="padding: 0.35rem 0.6rem;">Update</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="text-align: center; padding: 4rem 1rem; background-color: #FFFFFF; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                        <h3>No incoming orders yet</h3>
                        <p>Customer orders for your products will appear here for fulfillment.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
