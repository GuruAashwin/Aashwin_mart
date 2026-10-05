<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Buyer Dashboard — AASHWIN MART" />
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
                <span class="badge badge-buyer" style="margin-top: 0.5rem;">Verified Buyer</span>
            </div>

            <ul class="sidebar-menu">
                <li><a href="${pageContext.request.contextPath}/buyer/dashboard" class="sidebar-link active">Overview</a></li>
                <li><a href="${pageContext.request.contextPath}/orders" class="sidebar-link">Order History</a></li>
                <li><a href="${pageContext.request.contextPath}/cart" class="sidebar-link">Shopping Cart (${cartCount})</a></li>
                <li><a href="${pageContext.request.contextPath}/shop" class="sidebar-link">Browse Marketplace</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">Member Dashboard</h1>
                <p>Manage your luxury orders, active cart, and verified reviews.</p>
            </div>

            <!-- Stats Overview -->
            <div class="stat-grid">
                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-6Z"/><path d="M3 6h18"/><path d="M16 10a4 4 0 0 1-8 0"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${totalOrdersCount}"/></div>
                        <div class="stat-label">Total Orders Placed</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/>
                            <path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${cartCount}"/></div>
                        <div class="stat-label">Items in Bag</div>
                    </div>
                </div>
            </div>

            <!-- Recent Orders Section -->
            <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.5rem; box-shadow: var(--shadow-sm);">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem;">
                    <h2 style="font-size: 1.3rem;">Recent Orders</h2>
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary btn-sm">View All Orders</a>
                </div>

                <c:choose>
                    <c:when test="${not empty recentOrders}">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Date</th>
                                        <th>Items</th>
                                        <th>Total</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="order" items="${recentOrders}">
                                        <tr>
                                            <td><strong>#ORD-<c:out value="${order.id}"/></strong></td>
                                            <td><fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy"/></td>
                                            <td><c:out value="${order.items.size()}"/> item(s)</td>
                                            <td><strong>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong></td>
                                            <td>
                                                <span class="badge badge-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/order/detail?id=${order.id}" class="btn btn-secondary btn-sm">View Details</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div style="text-align: center; padding: 2rem; color: var(--text-muted);">
                            <p>You haven't placed any orders yet.</p>
                            <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-sm" style="margin-top: 1rem;">Start Shopping</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
