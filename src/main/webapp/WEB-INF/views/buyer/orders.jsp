<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Order History — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding-top: 2.5rem; padding-bottom: 4rem;">
    <h1 style="font-size: 2.2rem; margin-bottom: 0.25rem;">My Order History</h1>
    <p style="margin-bottom: 2rem;">Track live fulfillment statuses and view past purchases</p>

    <c:choose>
        <c:when test="${not empty orders}">
            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Date Placed</th>
                            <th>Items</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td><strong>#ORD-<c:out value="${order.id}"/></strong></td>
                                <td><fmt:formatDate value="${order.createdAt}" pattern="MMM dd, yyyy, hh:mm a"/></td>
                                <td>
                                    <div style="font-weight: 600;"><c:out value="${order.items.size()}"/> item(s)</div>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">
                                        <c:forEach var="item" items="${order.items}" varStatus="loop">
                                            <c:out value="${item.productName}"/><c:if test="${!loop.last}">, </c:if>
                                        </c:forEach>
                                    </div>
                                </td>
                                <td><strong>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong></td>
                                <td>
                                    <span class="badge badge-${fn:toLowerCase(order.status)}"><c:out value="${order.status}"/></span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/order/detail?id=${order.id}" class="btn btn-secondary btn-sm">
                                        View Details &amp; Receipt
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div style="text-align: center; padding: 4rem 1rem; background-color: #FFFFFF; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="color: var(--text-muted); margin-bottom: 1rem;">
                    <circle cx="12" cy="12" r="10"/><path d="M16 16s-1.5-2-4-2-4 2-4 2"/><line x1="9" y1="9" x2="9.01" y2="9"/><line x1="15" y1="9" x2="15.01" y2="9"/>
                </svg>
                <h3 style="margin-bottom: 0.5rem;">No orders placed yet</h3>
                <p style="margin-bottom: 1.5rem;">Start exploring luxury items from our premier merchants.</p>
                <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-sm">Explore Shop</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
