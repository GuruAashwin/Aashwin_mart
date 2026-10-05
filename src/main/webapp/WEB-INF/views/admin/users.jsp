<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="User Management — AASHWIN MART" />
</jsp:include>

<div class="container">
    <div class="dashboard-layout">
        <!-- Sidebar -->
        <aside class="dashboard-sidebar">
            <div class="sidebar-profile">
                <div class="avatar-badge" style="background: linear-gradient(135deg, #111 0%, #333 100%);">&#128081;</div>
                <h3 style="font-size: 1.1rem; margin-bottom: 0.2rem;"><c:out value="${sessionScope.currentUser.name}"/></h3>
                <p style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${sessionScope.currentUser.email}"/></p>
                <span class="badge badge-admin" style="margin-top: 0.5rem;">Marketplace Admin</span>
            </div>

            <ul class="sidebar-menu">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-link">KPI Overview</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users" class="sidebar-link active">User Directory</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="sidebar-link">Global Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products" class="sidebar-link">Catalog Moderation</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: var(--status-danger);">Sign Out</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <div>
            <div style="margin-bottom: 2rem;">
                <h1 style="font-size: 2rem; margin-bottom: 0.25rem;">User Directory</h1>
                <p>Manage all registered Buyers, Sellers, and Administrators in the marketplace</p>
            </div>

            <c:if test="${param.status == 'deactivated'}">
                <div class="alert alert-danger" style="margin-bottom:1rem;">User account has been deactivated successfully.</div>
            </c:if>
            <c:if test="${param.status == 'activated'}">
                <div class="alert alert-success" style="margin-bottom:1rem;">User account has been reactivated successfully.</div>
            </c:if>
            <c:if test="${param.error == 'cannotModifySelf'}">
                <div class="alert alert-danger" style="margin-bottom:1rem;">You cannot modify your own admin account status.</div>
            </c:if>
            <c:if test="${param.error == 'failed'}">
                <div class="alert alert-danger" style="margin-bottom:1rem;">Action failed. Please try again.</div>
            </c:if>

            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Name</th>
                            <th>Email Address</th>
                            <th>Role Tier</th>
                            <th>Status</th>
                            <th>Joined Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td><strong>#USR-<c:out value="${user.id}"/></strong></td>
                                <td><strong><c:out value="${user.name}"/></strong></td>
                                <td><c:out value="${user.email}"/></td>
                                <td>
                                    <span class="badge badge-${fn:toLowerCase(user.role)}"><c:out value="${user.role}"/></span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.active}">
                                            <span class="badge" style="background-color:#ECFDF5;color:#065F46;border:1px solid #A7F3D0;">&#10003; Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge" style="background-color:#FEF2F2;color:#991B1B;border:1px solid #FCA5A5;">&#8855; Suspended</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td><fmt:formatDate value="${user.createdAt}" pattern="MMM dd, yyyy"/></td>
                                <td>
                                    <c:if test="${user.id != sessionScope.currentUser.id}">
                                        <c:choose>
                                            <c:when test="${user.active}">
                                                <form method="POST" action="${pageContext.request.contextPath}/admin/users/toggle"
                                                      style="display:inline;"
                                                      onsubmit="return confirm('Suspend account for ${user.name}?');">
                                                    <input type="hidden" name="userId" value="${user.id}">
                                                    <input type="hidden" name="active" value="false">
                                                    <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                                    <button type="submit" class="btn btn-sm"
                                                            style="background-color:#FEE2E2;color:#991B1B;border:1px solid #FCA5A5;padding:0.25rem 0.6rem;font-size:0.78rem;">
                                                        Suspend
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form method="POST" action="${pageContext.request.contextPath}/admin/users/toggle"
                                                      style="display:inline;"
                                                      onsubmit="return confirm('Reactivate account for ${user.name}?');">
                                                    <input type="hidden" name="userId" value="${user.id}">
                                                    <input type="hidden" name="active" value="true">
                                                    <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
                                                    <button type="submit" class="btn btn-sm"
                                                            style="background-color:#D1FAE5;color:#065F46;border:1px solid #6EE7B7;padding:0.25rem 0.6rem;font-size:0.78rem;">
                                                        Reactivate
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <c:if test="${user.id == sessionScope.currentUser.id}">
                                        <span style="font-size:0.78rem;color:var(--text-muted);">Current Admin</span>
                                    </c:if>
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
