package com.rhythm.mypizzaapp.getterAndSetterClasses;

import java.io.Serializable;

/** A common Bean class for OrderHistoryFragment, MyCartFragment, MyOrderFragment
 * */

public class OrderHistoryBeanClass implements Serializable {
    private String pizzaName, pizzaDetails, pizzaSubTotal, pizzaCustomizeItems, pizzaQuantity, orderDate;
//    private int pizzaIv, pizzaLabelTagIv;
    private String pizzaIv, pizzaLabelTagIv, customerAddress;

    public OrderHistoryBeanClass(String mPizzaName, String mPizzaDetails, String mPizzaIv, String mPizzaLabelTagIv, String subTotal, String customizeItems, String pizzaQuantity, String orderDate) {
        this.pizzaName = mPizzaName;
        this.pizzaDetails = mPizzaDetails;
        this.pizzaIv = mPizzaIv;
        this.pizzaLabelTagIv = mPizzaLabelTagIv;
        this.pizzaQuantity = pizzaQuantity;
        this.orderDate = orderDate;
        pizzaSubTotal = subTotal;
        pizzaCustomizeItems = customizeItems;
    }

    public OrderHistoryBeanClass() {
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPizzaQuantity() {
        return pizzaQuantity;
    }

    public void setPizzaQuantity(String pizzaQuantity) {
        this.pizzaQuantity = pizzaQuantity;
    }

    public String getPizzaSubTotal() {
        return pizzaSubTotal;
    }

    public String getPizzaPriceTotal() {
        return pizzaSubTotal;
    }

    public void setPizzaSubTotal(String pizzaSubTotal) {
        this.pizzaSubTotal = pizzaSubTotal;
    }

    public String getPizzaCustomizeItems() {
        return pizzaCustomizeItems;
    }

    public void setPizzaCustomizeItems(String pizzaCustomizeItems) {
        this.pizzaCustomizeItems = pizzaCustomizeItems;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaDetails() {
        return pizzaDetails;
    }

    public void setPizzaDetails(String pizzaDetails) {
        this.pizzaDetails = pizzaDetails;
    }

    public String getPizzaIv() {
        return pizzaIv;
    }

    public void setPizzaIv(String pizzaIv) {
        this.pizzaIv = pizzaIv;
    }

    public String getPizzaLabelTagIv() {
        return pizzaLabelTagIv;
    }

    public void setPizzaLabelTagIv(String pizzaLabelTagIv) {
        this.pizzaLabelTagIv = pizzaLabelTagIv;
    }
}
