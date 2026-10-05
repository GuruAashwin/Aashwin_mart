package com.aashwin.aashwinmart.service.strategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock UPI / Digital Wallet payment confirmation strategy.
 */
public class MockUpiPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(BigDecimal amount, String paymentDetails) {
        String txId = "TXN-UPI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String upi = (paymentDetails != null && !paymentDetails.trim().isEmpty()) ? paymentDetails.trim() : "user@royalupi";
        return new PaymentResult(true, txId, "Mock UPI Payment of $" + amount + " confirmed (" + upi + ")");
    }
}
