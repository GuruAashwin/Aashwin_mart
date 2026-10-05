<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="401 Sign In Required — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding: 6rem 1rem; text-align: center; max-width: 600px;">
    <div style="font-family: var(--font-serif); font-size: 5rem; font-weight: 800; color: var(--gold-primary); line-height: 1; margin-bottom: 1rem;">
        401
    </div>
    <h1 style="font-size: 2rem; margin-bottom: 1rem;">Authentication Required</h1>
    <p style="margin-bottom: 2rem; color: var(--text-muted);">
        Please sign in to your AASHWIN MART account to access this page.
    </p>
    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">Sign In Now</a>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
