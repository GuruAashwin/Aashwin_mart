<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Seller Merchant Portal — AASHWIN MART" />
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
                <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="sidebar-link active">Overview</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products" class="sidebar-link">My Product Catalog</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products/add" class="sidebar-link">+ Add New Listing</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/orders" class="sidebar-link">Incoming Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">Merchant Portal</h1>
                    <p>Track store performance, inventory levels, and customer order fulfillment</p>
                </div>
                <a href="${pageContext.request.contextPath}/seller/products/add" class="btn btn-primary">
                    + Add New Product
                </a>
            </div>

            <!-- Stats Grid (O3 Seller Sales Dashboard) -->
            <div class="stat-grid">
                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value text-gold">₹<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/></div>
                        <div class="stat-label">Total Store Revenue</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-6Z"/><path d="M3 6h18"/><path d="M16 10a4 4 0 0 1-8 0"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${totalOrdersCount}"/></div>
                        <div class="stat-label">Incoming Orders</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="m7.5 4.27 9 5.15"/><path d="M21 8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16Z"/><path d="m3.3 7 8.7 5 8.7-5"/><path d="M12 22V12"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${activeListings}"/></div>
                        <div class="stat-label">Active Listings</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon" style="color: var(--status-danger); background-color: #FEF2F2; border-color: #FCA5A5;">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${lowStockCount}"/></div>
                        <div class="stat-label">Low Stock Alerts (&le;5)</div>
                    </div>
                </div>
            </div>

            <!-- Recent Incoming Orders -->
            <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.5rem; box-shadow: var(--shadow-sm);">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem;">
                    <h2 style="font-size: 1.3rem;">Recent Customer Orders</h2>
                    <a href="${pageContext.request.contextPath}/seller/orders" class="btn btn-secondary btn-sm">View All Orders</a>
                </div>

                <c:choose>
                    <c:when test="${not empty incomingOrders}">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Buyer</th>
                                        <th>Items Ordered</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="order" items="${incomingOrders}">
                                        <tr>
                                            <td><strong>#ORD-<c:out value="${order.id}"/></strong></td>
                                            <td>
                                                <div style="font-weight: 600;"><c:out value="${order.buyerName}"/></div>
                                                <div style="font-size: 0.75rem; color: var(--text-muted);"><c:out value="${order.buyerEmail}"/></div>
                                            </td>
                                            <td>
                                                <c:forEach var="item" items="${order.items}">
                                                    <div style="font-size: 0.85rem;">• <c:out value="${item.productName}"/> (Qty: <c:out value="${item.quantity}"/>)</div>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <span class="badge badge-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/seller/orders" class="btn btn-secondary btn-sm">Update Status</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div style="text-align: center; padding: 2rem; color: var(--text-muted);">
                            <p>No incoming orders for your listings yet.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
