package com.aashwin.aashwinmart.service.strategy;

import java.math.BigDecimal;

/**
 * Strategy interface implementing the Strategy Design Pattern for swappable Mock Payment confirmation channels.
 */
public interface PaymentStrategy {

    /**
     * Executes mock payment confirmation.
     *
     * @param amount Monetary total
     * @param paymentDetails Channel-specific payment identifier / metadata
     * @return Payment confirmation result
     */
    PaymentResult processPayment(BigDecimal amount, String paymentDetails);

    class PaymentResult {
        private final boolean success;
        private final String transactionId;
        private final String message;

        public PaymentResult(boolean success, String transactionId, String message) {
            this.success = success;
            this.transactionId = transactionId;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getMessage() {
            return message;
        }
    }
}
