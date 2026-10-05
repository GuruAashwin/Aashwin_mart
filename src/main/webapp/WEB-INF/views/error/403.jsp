<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="403 Access Forbidden — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding: 6rem 1rem; text-align: center; max-width: 600px;">
    <div style="font-family: var(--font-serif); font-size: 5rem; font-weight: 800; color: var(--gold-primary); line-height: 1; margin-bottom: 1rem;">
        403
    </div>
    <h1 style="font-size: 2rem; margin-bottom: 1rem;">Access Restricted</h1>
    <p style="margin-bottom: 2rem; color: var(--text-muted);">
        <c:out value="${errorMessage != null ? errorMessage : 'You do not have the required permissions or role tier to access this portal.'}"/>
    </p>
    <div style="display: flex; justify-content: center; gap: 1rem;">
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Return to Homepage</a>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">Switch Account</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
