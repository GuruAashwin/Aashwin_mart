<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Seller Registration — AASHWIN MART" />
</jsp:include>

<div class="container">
    <div class="auth-container" style="max-width: 500px;">
        <div class="auth-header">
            <svg class="crown-icon" style="width: 36px; height: 36px; margin: 0 auto 0.5rem auto; display: block;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="m2 4 3 12h14l3-12-6 7-4-7-4 7-6-7zm3 16h14"/>
            </svg>
            <h1>Become a Seller</h1>
            <p>Showcase and sell your luxury products on AASHWIN MART</p>
        </div>

        <c:if test="${errorMessage != null}">
            <div class="alert alert-danger">
                <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register-seller" method="POST">
            <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
            <div class="form-group">
                <label class="form-label" for="name">Brand / Merchant Name</label>
                <input type="text" id="name" name="name" class="form-control" value="<c:out value='${name}'/>" placeholder="Crown &amp; Co. Horology" required autofocus>
            </div>

            <div class="form-group">
                <label class="form-label" for="email">Business Email Address</label>
                <input type="email" id="email" name="email" class="form-control" value="<c:out value='${email}'/>" placeholder="seller@domain.com" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Password (min 6 characters)</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary btn-block btn-lg" style="margin-top: 1.5rem;">Launch Seller Store</button>
        </form>

        <div style="text-align: center; margin-top: 1.5rem; font-size: 0.85rem; color: var(--text-muted);">
            Already registered?
            <a href="${pageContext.request.contextPath}/login" style="color: var(--gold-dark); font-weight: 600; text-decoration: underline;">Sign In to Portal</a>
            <br>
            Looking to buy luxury?
            <a href="${pageContext.request.contextPath}/register" style="color: var(--gold-dark); font-weight: 600; text-decoration: underline;">Register as a Buyer</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
