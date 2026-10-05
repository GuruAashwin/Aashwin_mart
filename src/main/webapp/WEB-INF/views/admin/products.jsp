<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Catalog Moderation — AASHWIN MART" />
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
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link">Global Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products" class="sidebar-link active">Catalog Moderation</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">Product Catalog Moderation</h1>
                <p>Oversight of all merchant listings across categories with moderation controls (F7)</p>
            </div>

            <c:if test="${param.deleted == 'true'}">
                <div class="alert alert-info">Listing removed by Administrator.</div>
            </c:if>
            <c:if test="${param.toggled == 'true'}">
                <div class="alert alert-success">✓ Listing visibility updated.</div>
            </c:if>

            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Merchant</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="prod" items="${products}">
                            <tr>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 0.75rem;">
                                        <img src="<c:out value='${prod.imageUrl}'/>" alt="" style="width: 44px; height: 44px; object-fit: cover; border-radius: var(--radius-sm);">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/product?id=${prod.id}" target="_blank" style="font-weight: 700; color: var(--text-main);">
                                                <c:out value="${prod.name}"/> ↗
                                            </a>
                                        </div>
                                    </div>
                                </td>
                                <td><strong><c:out value="${prod.sellerName}"/></strong></td>
                                <td><span class="product-category-tag" style="position: static;"><c:out value="${prod.category}"/></span></td>
                                <td>$<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></td>
                                <td><c:out value="${prod.stockQty}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${prod.active}">
                                            <span class="badge" style="background-color: #ECFDF5; color: #065F46;">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge" style="background-color: #FEE2E2; color: #991B1B;">Disabled</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div style="display: flex; gap: 0.4rem;">
                                        <form action="${pageContext.request.contextPath}/admin/products/toggle" method="POST" style="display: inline;">
                                            <input type="hidden" name="id" value="${prod.id}">
                                            <input type="hidden" name="active" value="${!prod.active}">
                                            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                            <button type="submit" class="btn btn-secondary btn-sm" style="padding: 0.3rem 0.6rem; font-size: 0.75rem;">
                                                <c:out value="${prod.active ? 'Disable' : 'Enable'}"/>
                                            </button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/admin/products/delete" method="POST" onsubmit="return confirm('Moderator Action: Delete this listing from marketplace?');" style="display: inline;">
                                            <input type="hidden" name="id" value="${prod.id}">
                                            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                            <button type="submit" class="btn btn-danger btn-sm" style="padding: 0.3rem 0.6rem; font-size: 0.75rem;">Delete</button>
                                        </form>
                                    </div>
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
