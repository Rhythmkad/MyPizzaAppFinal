package com.rhythm.mypizzaapp.getterAndSetterClasses;

/**
 * Payment cards bean class
 * */

public class PaymentCardListBeanClass {

    private String cardNumber, cardHolderName, cardCvv, cardExpiryDate;

    public PaymentCardListBeanClass() {
    }

    public PaymentCardListBeanClass(String cardHolderName, String cardNumber) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    public PaymentCardListBeanClass(String cardNumber, String cardHolderName, String cardCvv, String cardExpiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardCvv = cardCvv;
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}
