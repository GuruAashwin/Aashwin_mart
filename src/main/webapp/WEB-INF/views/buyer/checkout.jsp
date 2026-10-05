<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="pageTitle" value="Royal Checkout — AASHWIN MART" />
</jsp:include>

<div class="container" style="padding-top: 2.5rem; padding-bottom: 4rem;">
    <div style="text-align: center; margin-bottom: 2.5rem;">
        <span class="hero-badge">👑 Step 2 of 2: Delivery &amp; Payment Confirmation</span>
        <h1 style="font-size: 2.2rem; margin-top: 0.5rem;">Royal Checkout</h1>
        <p>Complete your delivery destination and confirm mock payment</p>
    </div>

    <c:if test="${errorMessage != null}">
        <div class="alert alert-danger">
            <c:out value="${errorMessage}" />
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/checkout" method="POST">
        <input type="hidden" name="_csrf" value="${sessionScope._csrfToken}">
        <div style="display: grid; grid-template-columns: 1.3fr 0.9fr; gap: 2.5rem; align-items: start;">
            <!-- Left: Shipping & Mock Payment Details -->
            <div style="display: flex; flex-direction: column; gap: 2rem;">
                <!-- 1. Shipping Address -->
                <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 2rem; box-shadow: var(--shadow-sm);">
                    <h2 style="font-size: 1.3rem; margin-bottom: 1.5rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span style="width: 28px; height: 28px; border-radius: var(--radius-full); background-color: var(--gold-light); border: 1px solid var(--gold-border); display: flex; align-items: center; justify-content: center; font-size: 0.85rem; color: var(--gold-dark); font-weight: 700;">1</span>
                        Shipping Destination
                    </h2>

                    <div class="form-group">
                        <label class="form-label" for="fullName">Recipient Full Name</label>
                        <input type="text" id="fullName" name="fullName" class="form-control" value="${checkoutReq != null ? checkoutReq.fullName : user.name}" placeholder="Alex Sterling" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="addressLine">Street Address / Estate</label>
                        <input type="text" id="addressLine" name="addressLine" class="form-control" value="${checkoutReq != null ? checkoutReq.addressLine : '742 Evergreen Terrace'}" placeholder="Apt 4B, 100 Luxury Blvd" required>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1rem;">
                        <div class="form-group">
                            <label class="form-label" for="city">City</label>
                            <input type="text" id="city" name="city" class="form-control" value="${checkoutReq != null ? checkoutReq.city : 'Springfield'}" placeholder="City" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label" for="state">State / Province</label>
                            <input type="text" id="state" name="state" class="form-control" value="${checkoutReq != null ? checkoutReq.state : 'OR'}" placeholder="State" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label" for="postalCode">Postal Code</label>
                            <input type="text" id="postalCode" name="postalCode" class="form-control" value="${checkoutReq != null ? checkoutReq.postalCode : '97477'}" placeholder="Postal Code" required>
                        </div>
                    </div>

                    <div class="form-group" style="margin-bottom: 0;">
                        <label class="form-label" for="phone">Contact Phone Number</label>
                        <input type="tel" id="phone" name="phone" class="form-control" value="${checkoutReq != null ? checkoutReq.phone : '+1 (555) 019-2834'}" placeholder="+1 555-0199" required>
                    </div>
                </div>

                <!-- 2. Mock Payment Confirmation -->
                <div style="background-color: #FFFFFF; border: 1px solid var(--gold-border); border-radius: var(--radius-md); padding: 2rem; box-shadow: var(--shadow-sm);">
                    <h2 style="font-size: 1.3rem; margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem;">
                        <span style="width: 28px; height: 28px; border-radius: var(--radius-full); background: var(--gold-gradient); display: flex; align-items: center; justify-content: center; font-size: 0.85rem; color: #FFFFFF; font-weight: 700;">2</span>
                        Mock Payment Confirmation
                    </h2>
                    <p style="font-size: 0.85rem; margin-bottom: 1.5rem;">
                        ✨ <em>Mock Payment Simulation (No real charges will occur). Choose your payment channel:</em>
                    </p>

                    <div style="display: flex; gap: 1rem; margin-bottom: 1.5rem;">
                        <label style="flex: 1; padding: 1rem; border: 2px solid var(--gold-primary); border-radius: var(--radius-sm); cursor: pointer; background-color: var(--gold-light); display: flex; align-items: center; gap: 0.5rem; font-weight: 600;">
                            <input type="radio" name="paymentMethod" value="CARD" checked onchange="togglePaymentFields('CARD')">
                            Credit/Debit Card
                        </label>
                        <label style="flex: 1; padding: 1rem; border: 1px solid var(--border-color); border-radius: var(--radius-sm); cursor: pointer; background-color: #FAFAFA; display: flex; align-items: center; gap: 0.5rem; font-weight: 600;">
                            <input type="radio" name="paymentMethod" value="UPI" onchange="togglePaymentFields('UPI')">
                            Instant UPI
                        </label>
                        <label style="flex: 1; padding: 1rem; border: 1px solid var(--border-color); border-radius: var(--radius-sm); cursor: pointer; background-color: #FAFAFA; display: flex; align-items: center; gap: 0.5rem; font-weight: 600;">
                            <input type="radio" name="paymentMethod" value="NETBANKING" onchange="togglePaymentFields('NETBANKING')">
                            NetBanking
                        </label>
                    </div>

                    <!-- Card Fields -->
                    <div id="card-fields">
                        <div class="form-group">
                            <label class="form-label">Card Number</label>
                            <input type="text" name="cardNumber" class="form-control" value="4532 •••• •••• 8892" placeholder="Card Number">
                        </div>
                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                            <div class="form-group">
                                <label class="form-label">Expiry (MM/YY)</label>
                                <input type="text" class="form-control" value="12/28" placeholder="MM/YY">
                            </div>
                            <div class="form-group">
                                <label class="form-label">CVV</label>
                                <input type="password" class="form-control" value="888" placeholder="CVV">
                            </div>
                        </div>
                    </div>

                    <!-- UPI Fields -->
                    <div id="upi-fields" style="display: none;">
                        <div class="form-group">
                            <label class="form-label">UPI ID / Virtual Payment Address</label>
                            <input type="text" name="upiId" class="form-control" value="buyer.alex@royalupi" placeholder="username@okhdfcbank">
                        </div>
                    </div>

                    <!-- NetBanking Fields -->
                    <div id="netbanking-fields" style="display: none;">
                        <div class="form-group">
                            <label class="form-label">Select Bank</label>
                            <select class="form-control">
                                <option>HDFC Bank Premier</option>
                                <option>ICICI Bank Wealth</option>
                                <option>State Bank of India</option>
                                <option>Axis Bank Burgundy</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right: Order Items Review & Final CTA -->
            <div style="background-color: #FFFFFF; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 1.75rem; box-shadow: var(--shadow-sm); position: sticky; top: 100px;">
                <h3 style="font-size: 1.25rem; margin-bottom: 1.25rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.75rem;">Order Review</h3>

                <!-- Cart Mini Items List -->
                <div style="display: flex; flex-direction: column; gap: 1rem; margin-bottom: 1.5rem; max-height: 240px; overflow-y: auto;">
                    <c:forEach var="item" items="${cartItems}">
                        <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.9rem;">
                            <div style="display: flex; align-items: center; gap: 0.75rem;">
                                <img src="<c:out value='${item.productImageUrl}'/>" alt="" style="width: 44px; height: 44px; object-fit: cover; border-radius: var(--radius-sm);">
                                <div>
                                    <div style="font-weight: 600; line-height: 1.3;"><c:out value="${item.productName}"/></div>
                                    <div style="font-size: 0.75rem; color: var(--text-muted);">Qty: <c:out value="${item.quantity}"/> &times; ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></div>
                                </div>
                            </div>
                            <div style="font-weight: 700;">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></div>
                        </div>
                    </c:forEach>
                </div>

                <div style="border-top: 1px solid var(--border-color); padding-top: 1rem; display: flex; flex-direction: column; gap: 0.5rem; font-size: 0.95rem;">
                    <div style="display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Items Subtotal:</span>
                        <span>₹<fmt:formatNumber value="${subtotal}" pattern="#,##0.00"/></span>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Royal Delivery:</span>
                        <span style="color: var(--status-success); font-weight: 600;">FREE</span>
                    </div>
                    <div style="display: flex; justify-content: space-between; font-size: 1.3rem; font-weight: 800; border-top: 1px solid var(--border-color); padding-top: 0.75rem; margin-top: 0.5rem;">
                        <span>Grand Total:</span>
                        <span class="text-gold">₹<fmt:formatNumber value="${total}" pattern="#,##0.00"/></span>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary btn-block btn-lg" style="margin-top: 1.5rem;">
                    Confirm &amp; Place Order 👑
                </button>

                <div style="text-align: center; margin-top: 1rem; font-size: 0.75rem; color: var(--text-muted);">
                    By clicking Confirm, your order is recorded transactionally.
                </div>
            </div>
        </div>
    </form>
</div>

<script>
function togglePaymentFields(method) {
    document.getElementById('card-fields').style.display = method === 'CARD' ? 'block' : 'none';
    document.getElementById('upi-fields').style.display = method === 'UPI' ? 'block' : 'none';
    document.getElementById('netbanking-fields').style.display = method === 'NETBANKING' ? 'block' : 'none';
}
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
