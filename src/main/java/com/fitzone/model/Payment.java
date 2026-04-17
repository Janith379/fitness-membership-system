package com.fitzone.model;

import java.time.LocalDate;

public class Payment {
    private String paymentId;
    private String memberId;
    private double amount;
    private LocalDate paymentDate;
    private String cardLast4;
    private String status;  // "SUCCESS" or "FAILED"

    public Payment() {
    }

    public Payment(String paymentId, String memberId, double amount,
                   LocalDate paymentDate, String cardLast4, String status) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.cardLast4 = cardLast4;
        this.status = status;
    }

    // --- Getters and Setters (ENCAPSULATION) ---

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Converts this payment to a CSV line for file storage.
     */
    public String toCsvLine() {
        return String.join(",",
                safe(paymentId),
                safe(memberId),
                String.format("%.2f", amount),
                paymentDate != null ? paymentDate.toString() : "",
                safe(cardLast4),
                safe(status)
        );
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    @Override
    public String toString() {
        return "Payment [" + paymentId + "] Member=" + memberId + " Amount=" + amount + " Status=" + status;
    }
}
