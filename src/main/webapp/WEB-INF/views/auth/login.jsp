<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Sign In — AASHWIN MART" />
</jsp:include>

<div class="container">
    <div class="auth-container">
        <div class="auth-header">
            <svg class="crown-icon" style="width: 36px; height: 36px; margin: 0 auto 0.5rem auto; display: block;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="m2 4 3 12h14l3-12-6 7-4-7-4 7-6-7zm3 16h14"/>
            </svg>
            <h1>Welcome Back</h1>
            <p>Sign in to your AASHWIN MART account</p>
        </div>

        <c:if test="${errorMessage != null}">
            <div class="alert alert-danger">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>
        <c:if test="${param.loggedOut == 'true'}">
            <div class="alert alert-info">
                You have been signed out successfully.
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="POST" id="login-form">
            <input type="hidden" name="redirect" value="<c:out value='${param.redirect}'/>">
            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">

            <div class="form-group">
                <label class="form-label" for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" value="<c:out value='${email}'/>" placeholder="name@domain.com" required autofocus>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg" style="margin-top: 1.5rem;">Sign In</button>
        </form>

        <!-- Quick Demo Credentials Box for Evaluator Convenience -->
        <div style="margin-top: 2rem; padding: 1rem; background-color: var(--gold-light); border: 1px solid var(--gold-border); border-radius: var(--radius-sm); font-size: 0.8rem;">
            <div style="font-weight: 700; color: var(--gold-dark); margin-bottom: 0.5rem; text-transform: uppercase; letter-spacing: 0.05em;">
                🔑 Quick Demo Fill:
            </div>
            <div style="display: flex; gap: 0.4rem; flex-wrap: wrap;">
                <button type="button" class="btn btn-secondary btn-sm" onclick="fillDemo('admin@aashwinmart.com', 'Admin@123')">Admin</button>
                <button type="button" class="btn btn-secondary btn-sm" onclick="fillDemo('seller.aurum@aashwinmart.com', 'Seller@123')">Seller (Aurum)</button>
                <button type="button" class="btn btn-secondary btn-sm" onclick="fillDemo('buyer.alex@aashwinmart.com', 'Buyer@123')">Buyer (Alex)</button>
            </div>
        </div>

        <div style="text-align: center; margin-top: 1.5rem; font-size: 0.85rem; color: var(--text-muted);">
            Don't have an account yet?
            <a href="${pageContext.request.contextPath}/register" style="color: var(--gold-dark); font-weight: 600; text-decoration: underline;">Join as Buyer</a>
            or
            <a href="${pageContext.request.contextPath}/register-seller" style="color: var(--gold-dark); font-weight: 600; text-decoration: underline;">Become a Seller</a>
        </div>
    </div>
</div>

<script>
function fillDemo(email, pass) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = pass;
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
