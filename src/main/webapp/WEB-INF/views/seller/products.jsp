<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="My Products — AASHWIN MART" />
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
                <li><a href="${pageContext.request.contextPath}/seller/products" class="sidebar-link active">My Product Catalog</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products/add" class="sidebar-link">+ Add New Listing</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/orders" class="sidebar-link">Incoming Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">My Product Catalog</h1>
                    <p>Manage product details, stock inventory, and active listings</p>
                </div>
                <a href="${pageContext.request.contextPath}/seller/products/add" class="btn btn-primary">
                    + Add New Listing
                </a>
            </div>

            <c:if test="${param.created == 'true'}">
                <div class="alert alert-success">✨ Product listing successfully created and published!</div>
            </c:if>
            <c:if test="${param.updated == 'true'}">
                <div class="alert alert-success">✓ Product listing successfully updated.</div>
            </c:if>
            <c:if test="${param.deleted == 'true'}">
                <div class="alert alert-info">Product listing removed.</div>
            </c:if>
            <c:if test="${param.error != null}">
                <div class="alert alert-danger"><c:out value="${param.error}"/></div>
            </c:if>

            <c:choose>
                <c:when test="${not empty products}">
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Item</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Stock Qty</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="prod" items="${products}">
                                    <tr>
                                        <td>
                                            <div style="display: flex; align-items: center; gap: 1rem;">
                                                <img src="<c:out value='${prod.imageUrl}'/>" alt="" style="width: 50px; height: 50px; object-fit: cover; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
                                                <div>
                                                    <a href="${pageContext.request.contextPath}/product?id=${prod.id}" style="font-weight: 700; color: var(--text-main);">
                                                        <c:out value="${prod.name}"/>
                                                    </a>
                                                    <div style="font-size: 0.75rem; color: var(--gold-dark);">★ <fmt:formatNumber value="${prod.averageRating}" pattern="0.0"/> (<c:out value="${prod.reviewCount}"/> reviews)</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td><span class="product-category-tag" style="position: static;"><c:out value="${prod.category}"/></span></td>
                                        <td><strong>₹<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></strong></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${prod.stockQty <= 5}">
                                                    <span style="color: var(--status-danger); font-weight: 700;"><c:out value="${prod.stockQty}"/> (Low)</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="font-weight: 600;"><c:out value="${prod.stockQty}"/></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${prod.active}">
                                                    <span class="badge" style="background-color: #ECFDF5; color: #065F46;">Active</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge" style="background-color: #F3F4F6; color: #6B7280;">Inactive</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div style="display: flex; gap: 0.5rem;">
                                                <a href="${pageContext.request.contextPath}/seller/products/edit?id=${prod.id}" class="btn btn-secondary btn-sm">Edit</a>
                                                <form action="${pageContext.request.contextPath}/seller/products/delete" method="POST" onsubmit="return confirm('Are you sure you want to delete this product listing?');" style="display: inline;">
                                                    <input type="hidden" name="id" value="${prod.id}">
                                                    <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                                    <button type="submit" class="btn btn-danger btn-sm" style="padding: 0.4rem 0.6rem;">Delete</button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="text-align: center; padding: 4rem 1rem; background-color: #FFFFFF; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                        <h3>You have not created any listings yet</h3>
                        <p style="margin-bottom: 1.5rem;">Publish your luxury products to start receiving customer orders.</p>
                        <a href="${pageContext.request.contextPath}/seller/products/add" class="btn btn-primary">+ Add First Product</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
