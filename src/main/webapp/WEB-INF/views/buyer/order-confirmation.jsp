<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Order Confirmed — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding-top: 3.5rem; padding-bottom: 5rem; max-width: 760px;">
    <!-- Order Success Box -->
    <div style="background-color: #FFFFFF; border: 1px solid var(--gold-border); border-radius: var(--radius-lg); padding: 3rem; box-shadow: var(--shadow-gold); text-align: center;">
        <div style="width: 72px; height: 72px; border-radius: var(--radius-full); background-color: #ECFDF5; border: 2px solid #059669; display: flex; align-items: center; justify-content: center; margin: 0 auto 1.5rem auto; color: #059669;">
            <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="20 6 9 17 4 12"/>
            </svg>
        </div>

        <span class="hero-badge" style="margin-bottom: 0.75rem;">👑 Payment Authorized &amp; Order Placed</span>
        <h1 style="font-size: 2.2rem; margin-bottom: 0.5rem;">Thank You for Your Royal Order</h1>
        <p style="margin-bottom: 2rem;">Your order has been recorded in the AASHWIN MART ledger and is now in preparation.</p>

        <!-- Receipt Summary Card -->
        <div style="background-color: var(--bg-secondary); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.75rem; text-align: left; margin-bottom: 2.5rem;">
            <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem; margin-bottom: 1rem;">
                <div>
                    <div style="font-size: 0.8rem; color: var(--text-muted);">ORDER IDENTIFIER</div>
                    <div style="font-size: 1.2rem; font-weight: 800; font-family: var(--font-serif);">#ORD-<c:out value="${order.id}"/></div>
                </div>
                <div style="text-align: right;">
                    <div style="font-size: 0.8rem; color: var(--text-muted);">ORDER STATUS</div>
                    <span class="badge badge-confirmed"><c:out value="${order.status}"/></span>
                </div>
            </div>

            <!-- Ordered Items List -->
            <div style="margin-bottom: 1.5rem;">
                <div style="font-size: 0.85rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; margin-bottom: 0.75rem;">Items Summary</div>
                <c:forEach var="item" items="${order.items}">
                    <div style="display: flex; justify-content: space-between; align-items: center; padding: 0.5rem 0; border-bottom: 1px solid var(--border-light); font-size: 0.9rem;">
                        <div style="display: flex; align-items: center; gap: 0.75rem;">
                            <img src="<c:out value='${item.productImageUrl}'/>" alt="" style="width: 40px; height: 40px; object-fit: cover; border-radius: var(--radius-sm);">
                            <div>
                                <strong><c:out value="${item.productName}"/></strong>
                                <div style="font-size: 0.75rem; color: var(--text-muted);">Qty: <c:out value="${item.quantity}"/> &times; $<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></div>
                            </div>
                        </div>
                        <div style="font-weight: 700;">$<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></div>
                    </div>
                </c:forEach>
            </div>

            <div style="display: flex; justify-content: space-between; font-size: 1.25rem; font-weight: 800; border-top: 1px solid var(--border-color); padding-top: 0.75rem; color: var(--text-main);">
                <span>Total Charged:</span>
                <span class="text-gold">$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
            </div>
        </div>

        <div style="display: flex; justify-content: center; gap: 1rem; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary btn-lg">View Order History</a>
            <a href="${pageContext.request.contextPath}/shop" class="btn btn-primary btn-lg">Continue Shopping</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
