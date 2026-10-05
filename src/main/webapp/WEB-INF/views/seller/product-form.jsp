<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="${isEdit ? 'Edit Product' : 'Add New Listing'} — AASHWIN MART" />
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
                <li><a href="${pageContext.request.contextPath}/seller/products/add" class="sidebar-link ${!isEdit ? 'active' : ''}">+ Add New Listing</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/orders" class="sidebar-link">Incoming Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div style="max-width: 700px;">
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">
                    <c:out value="${isEdit ? 'Edit Product Listing' : 'Add New Luxury Listing'}" />
                </h1>
                <p>Provide detailed specifications and high-resolution imagery for your marketplace listing.</p>
            </div>

            <c:if test="${errorMessage != null}">
                <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
            </c:if>

            <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 2rem; box-shadow: var(--shadow-sm);">
                <form action="${pageContext.request.contextPath}/seller/products/${isEdit ? 'edit' : 'add'}" method="POST">
                    <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                    <c:if test="${isEdit}">
                        <input type="hidden" name="id" value="${product.id}">
                    </c:if>

                    <div class="form-group">
                        <label class="form-label" for="name">Product Title</label>
                        <input type="text" id="name" name="name" class="form-control" value="<c:out value='${product != null ? product.name : param.name}'/>" placeholder="e.g. Crown Royal Chronograph Automatic" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.25rem;">
                        <div class="form-group">
                            <label class="form-label" for="category">Category</label>
                            <input type="text" id="category" name="category" class="form-control" list="category-list" value="<c:out value='${product != null ? product.category : param.category}'/>" placeholder="e.g. Electronics, Fashion, Home..." required>
                            <datalist id="category-list">
                                <c:forEach var="cat" items="${categories}">
                                    <option value="<c:out value='${cat}'/>">
                                </c:forEach>
                            </datalist>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="price">Price (₹ INR)</label>
                            <input type="number" id="price" name="price" step="0.01" min="0.01" class="form-control" value="<c:out value='${product != null ? product.price : param.price}'/>" placeholder="499.00" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="stockQty">Available Stock Quantity</label>
                        <input type="number" id="stockQty" name="stockQty" min="0" class="form-control" value="<c:out value='${product != null ? product.stockQty : (param.stockQty != null ? param.stockQty : 10)}'/>" placeholder="25" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="imageUrl">High-Resolution Image URL</label>
                        <input type="url" id="imageUrl" name="imageUrl" class="form-control" value="<c:out value='${product != null ? product.imageUrl : param.imageUrl}'/>" placeholder="https://images.unsplash.com/..." required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="description">Detailed Description &amp; Specifications</label>
                        <textarea id="description" name="description" class="form-control" rows="5" placeholder="Highlight the artisanal craftsmanship, materials, technical specifications, and luxury finish..." required><c:out value='${product != null ? product.description : param.description}'/></textarea>
                    </div>

                    <c:if test="${isEdit}">
                        <div class="form-group" style="display: flex; align-items: center; gap: 0.5rem;">
                            <input type="checkbox" id="active" name="active" ${product.active ? 'checked' : ''}>
                            <label for="active" style="font-weight: 600; font-size: 0.9rem;">Active Listing (Visible to buyers on marketplace)</label>
                        </div>
                    </c:if>

                    <div style="display: flex; gap: 1rem; margin-top: 2rem;">
                        <button type="submit" class="btn btn-primary btn-lg" style="flex: 1;">
                            <c:out value="${isEdit ? 'Save Changes' : 'Publish Product Listing'}" />
                        </button>
                        <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-secondary btn-lg">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
