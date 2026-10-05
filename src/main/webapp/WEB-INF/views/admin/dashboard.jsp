<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Admin Command Center — AASHWIN MART" />
</jsp:include>

<div class="container">
    <div class="dashboard-layout">
        <!-- Sidebar -->
        <aside class="dashboard-sidebar">
            <div class="sidebar-profile">
                <div class="avatar-badge" style="background: linear-gradient(135deg, #111 0%, #333 100%);">
                    👑
                </div>
                <h3 style="font-size: 1.1rem; margin-bottom: 0.2rem;"><c:out value="${sessionScope.currentUser.name}"/></h3>
                <p style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${sessionScope.currentUser.email}"/></p>
                <span class="badge badge-admin" style="margin-top: 0.5rem;">Marketplace Admin</span>
            </div>

            <ul class="sidebar-menu">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link active">KPI Overview</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users" class="sidebar-link">User Directory (${totalUsers})</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link">Global Orders (${totalOrders})</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products" class="sidebar-link">Catalog Moderation (${totalProducts})</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">Marketplace Administration</h1>
                <p>Global oversight, analytics, user roles, order flow, and product catalog moderation (F7)</p>
            </div>

            <!-- Stats Grid -->
            <div class="stat-grid">
                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value text-gold">$<fmt:formatNumber value="${grossRevenue}" pattern="#,##0.00"/></div>
                        <div class="stat-label">Gross Marketplace Sales</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${totalUsers}"/></div>
                        <div class="stat-label">Registered Accounts</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-6Z"/><path d="M3 6h18"/><path d="M16 10a4 4 0 0 1-8 0"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${totalOrders}"/></div>
                        <div class="stat-label">Total Orders Placed</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-icon">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="m7.5 4.27 9 5.15"/><path d="M21 8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16Z"/><path d="m3.3 7 8.7 5 8.7-5"/><path d="M12 22V12"/>
                        </svg>
                    </div>
                    <div>
                        <div class="stat-value"><c:out value="${totalProducts}"/></div>
                        <div class="stat-label">Catalog Listings</div>
                    </div>
                </div>
            </div>

            <!-- Recent Orders & Users in 2 Columns -->
            <div style="display: grid; grid-template-columns: 1.2fr 0.8fr; gap: 2rem;">
                <!-- Recent Orders -->
                <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.5rem; box-shadow: var(--shadow-sm);">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                        <h2 style="font-size: 1.2rem;">Latest Global Orders</h2>
                        <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary btn-sm">View All</a>
                    </div>
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Buyer</th>
                                    <th>Total</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${recentOrders}">
                                    <tr>
                                        <td>#ORD-<c:out value="${order.id}"/></td>
                                        <td><c:out value="${order.buyerName}"/></td>
                                        <td><strong>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong></td>
                                        <td><span class="badge badge-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Recent Registered Users -->
                <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.5rem; box-shadow: var(--shadow-sm);">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                        <h2 style="font-size: 1.2rem;">New Users</h2>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary btn-sm">View All</a>
                    </div>
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Role</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="user" items="${recentUsers}">
                                    <tr>
                                        <td>
                                            <div style="font-weight: 600;"><c:out value="${user.name}"/></div>
                                            <div style="font-size: 0.75rem; color: var(--text-muted);"><c:out value="${user.email}"/></div>
                                        </td>
                                        <td><span class="badge badge-${fn:toLowerCase(user.role)}"><c:out value="${user.role}"/></span></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
