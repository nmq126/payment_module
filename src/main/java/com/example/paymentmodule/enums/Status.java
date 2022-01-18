package com.example.paymentmodule.enums;

public enum Status {
    ACTIVE,DELETE;

    public enum TransactionStatus {
        SUCCESS, FAIL
    }

    public enum PaymentStatus {
        PAID, UNPAID, REFUND, REFUNDED
    }

    public enum OrderStatus {
        CANCEL, PENDING, PROCESSING, DONE, REFUND
    }
}