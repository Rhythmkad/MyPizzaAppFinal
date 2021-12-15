package com.rhythm.mypizzaapp.getterAndSetterClasses;

import java.io.Serializable;

/** Bean class for Pizza Menu fragment
 * */

public class PizzaMenuBeanClass implements Serializable{
    private String pizzaName, pizzaDetails, pizzaPrice;
//    private  int pizzaIv, pizzaLabelTagIv;
    private  String pizzaIv, pizzaLabelTagIv;

    public PizzaMenuBeanClass(String mPizzaName, String mPizzaDetails, String mPizzaIv, String mPizzaLabelTagIv, String pizzaPrice) {
        this.pizzaName = mPizzaName;
        this.pizzaDetails = mPizzaDetails;
        this.pizzaIv = mPizzaIv;
        this.pizzaLabelTagIv = mPizzaLabelTagIv;
        this.pizzaPrice = pizzaPrice;
    }

    public String getPizzaPrice() {
        return pizzaPrice;
    }

    public void setPizzaPrice(String pizzaPrice) {
        this.pizzaPrice = pizzaPrice;
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
