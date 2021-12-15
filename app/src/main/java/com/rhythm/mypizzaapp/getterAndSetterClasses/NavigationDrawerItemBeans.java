package com.rhythm.mypizzaapp.getterAndSetterClasses;

/**
 * Bean class for navigation drawer items, used in HomeActivity, CustomNavigationDrawer
 */

public class NavigationDrawerItemBeans {

    private String ItemName;
    private int image;

    public NavigationDrawerItemBeans(String itemName, int image) {
        super();
        ItemName = itemName;
        this.image = image;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
