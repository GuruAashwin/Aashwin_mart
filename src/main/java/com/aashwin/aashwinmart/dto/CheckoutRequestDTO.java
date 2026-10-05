package com.aashwin.aashwinmart.dto;

import java.io.Serializable;

/**
 * DTO carrying checkout submission data including delivery details and mock payment method.
 */
public class CheckoutRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String phone;
    private String paymentMethod; // 'CARD', 'UPI', 'NETBANKING'
    private String cardNumber;
    private String upiId;

    public CheckoutRequestDTO() {
    }

    public String getFormattedAddress() {
        return fullName + "\n" +
                addressLine + "\n" +
                city + ", " + state + " " + postalCode + "\n" +
                "Phone: " + phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}
