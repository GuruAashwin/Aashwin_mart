package com.aashwin.aashwinmart.service.strategy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock NetBanking payment confirmation strategy.
 */
public class MockNetBankingPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult processPayment(BigDecimal amount, String paymentDetails) {
        String txId = "TXN-NB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, txId, "Mock NetBanking transfer of $" + amount + " authorized");
    }
}
