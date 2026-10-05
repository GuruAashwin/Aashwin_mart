package com.aashwin.aashwinmart.service.strategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock Credit/Debit Card payment confirmation strategy.
 */
public class MockCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(BigDecimal amount, String paymentDetails) {
        String txId = "TXN-CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String maskedCard = paymentDetails != null && paymentDetails.length() >= 4
                ? "•••• •••• •••• " + paymentDetails.substring(paymentDetails.length() - 4)
                : "•••• 4242";
        return new PaymentResult(true, txId, "Mock Card Payment of $" + amount + " confirmed (" + maskedCard + ")");
    }
}
