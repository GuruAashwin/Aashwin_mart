<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Create Buyer Account — AASHWIN MART" />
</jsp:include>

<div class="container">
    <div class="auth-container">
        <div class="auth-header">
            <svg class="crown-icon" style="width: 36px; height: 36px; margin: 0 auto 0.5rem auto; display: block;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="m2 4 3 12h14l3-12-6 7-4-7-4 7-6-7zm3 16h14"/>
            </svg>
            <h1>Join AASHWIN MART</h1>
            <p>Create your private luxury shopping account</p>
        </div>

        <c:if test="${errorMessage != null}">
            <div class="alert alert-danger">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="POST">
            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
            <div class="form-group">
                <label class="form-label" for="name">Full Name</label>
                <input type="text" id="name" name="name" class="form-control" value="<c:out value='${name}'/>" placeholder="Alex Sterling" required autofocus>
            </div>

            <div class="form-group">
                <label class="form-label" for="email">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" value="<c:out value='${email}'/>" placeholder="alex@example.com" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password (min 6 characters)</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg" style="margin-top: 1.5rem;">Create Account</button>
        </form>

        <div style="text-align: center; margin-top: 1.5rem; font-size: 0.85rem; color: var(--text-muted);">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login" style="color: var(--gold-dark); font-weight: 600; text-decoration: underline;">Sign In</a>
            <br>
            Are you a merchant?
            <a href="${pageContext.request.contextPath}/register-seller" style="color: var(--gold-dark); font-weight: 600; text-decoration: underline;">Register as a Seller</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
