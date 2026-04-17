package com.fitzone.model;

import jakarta.validation.constraints.*;

/**
 * Form-backing object for the fake payment form.
 * OOP CONCEPT — ENCAPSULATION: Private fields with getters/setters.
 */
public class PaymentForm {

    @NotBlank(message = "Cardholder name is required")
    private String cardholderName;

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "Card number must be 13-19 digits")
    private String cardNumber;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Expiry must be in MM/YY format")
    private String expiryDate;

    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV must be 3 or 4 digits")
    private String cvv;

    // --- Getters and Setters ---

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * Returns the last 4 digits of the card number for receipt display.
     */
    public String getLast4Digits() {
        if (cardNumber != null && cardNumber.length() >= 4) {
            return cardNumber.substring(cardNumber.length() - 4);
        }
        return "0000";
    }
}
