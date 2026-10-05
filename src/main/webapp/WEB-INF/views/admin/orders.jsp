<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Global Orders — AASHWIN MART" />
</jsp:include>

<div class="container">
    <div class="dashboard-layout">
        <!-- Sidebar -->
        <aside class="dashboard-sidebar">
            <div class="sidebar-profile">
                <div class="avatar-badge" style="background: linear-gradient(135deg, #111 0%, #333 100%);">👑</div>
                <h3 style="font-size: 1.1rem; margin-bottom: 0.2rem;"><c:out value="${sessionScope.currentUser.name}"/></h3>
                <p style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${sessionScope.currentUser.email}"/></p>
                <span class="badge badge-admin" style="margin-top: 0.5rem;">Marketplace Admin</span>
            </div>

            <ul class="sidebar-menu">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link">KPI Overview</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users" class="sidebar-link">User Directory</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link active">Global Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products" class="sidebar-link">Catalog Moderation</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">Global Marketplace Orders</h1>
                <p>Complete ledger of all customer orders across all sellers with status controls</p>
            </div>

            <c:if test="${param.statusUpdated == 'true'}">
                <div class="alert alert-success">✓ Order status updated successfully!</div>
            </c:if>

            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Buyer</th>
                            <th>Items</th>
                            <th>Total</th>
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
                                    <span style="font-size: 0.75rem; color: var(--text-muted);"><c:out value="${order.buyerEmail}"/></span>
                                </td>
                                <td>
                                    <div style="font-weight: 600;"><c:out value="${order.items.size()}"/> item(s)</div>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">
                                        <c:forEach var="item" items="${order.items}">
                                            • <c:out value="${item.productName}"/> (Qty: <c:out value="${item.quantity}"/>)<br>
                                        </c:forEach>
                                    </div>
                                </td>
                                <td><strong>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong></td>
                                <td><span class="badge badge-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/admin/orders/status" method="POST" style="display: flex; gap: 0.3rem;">
                                        <input type="hidden" name="orderId" value="${order.id}">
                                        <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                        <select name="status" class="form-control" style="padding: 0.3rem 0.5rem; font-size: 0.8rem; width: auto;">
                                            <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                            <option value="CONFIRMED" ${order.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                            <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                            <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                            <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                        </select>
                                        <button type="submit" class="btn btn-secondary btn-sm" style="padding: 0.3rem 0.5rem;">Save</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
