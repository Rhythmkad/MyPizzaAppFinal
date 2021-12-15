package com.rhythm.mypizzaapp.GlobalConstants;

/** Global Constants class stores all the constant values
 * which we can use any where in the application **/

public class GlobalConstants {

    public static final String PIZZA_DESCRIPTION = "PIZZA_DETAIL";
    public static final String MENU_BUNDLE_DATA_FOR_CART = "MENU_BUNDLE_DATA_FOR_CART";
    public static final String MENU_BUNDLE_DATA_FOR_PLACE_ORDER= "MENU_BUNDLE_DATA_FOR_PLACE_ORDER";
    public static final String MENU_BUNDLE_DATA_FOR_MY_ORDERS = "MENU_BUNDLE_DATA_FOR_MY_ORDERS";
    public static final String COMPLETE_ORDER_DATA_FOR_CART = "COMPLETE_ORDER_DATA_FOR_CART";
    public static final String COMPLETE_ORDER_DATA_FOR_MY_ORDERS = "COMPLETE_ORDER_DATA_FOR_MY_ORDERS";


    /* Fragments */
    public interface FRAGMENT_INTENT_DATA {

        String FRAGMENT_TAG = "FRAGMENT_TAG";
        String TITLE = "TITLE";
        String ONLINE_DOCUMENT_TYPE = "ONLINE_DOCUMENT";;
        String FRAGMENT_BACK_STACK = "BACK_STACK";
    }
    /* Shared preference keys
     * used in shared preference for storing classes and other shared pref keys */
    public interface SHARED_PREFERENCE {
        String KEY_NAME = "name", KEY_EMAIL = "email", KEY_ADDRESS = "address", KEY_PHONENUMBER = "phoneNumber",
                KEY_PASSWORD = "password", KEY_GENDER = "sex";
        String IS_LOGGED_IN = "isLoggedIn";

        String SHARED_PREFERENCE_KEY = "MyPizzaApp";
        String SUB_TOTAL_AMOUNT_KEY = "SUB_TOTAL_AMOUNT_KEY";
        String TAX_AMOUNT_KEY = "TAX_AMOUNT_KEY";
        String TOTAL_AMOUNT_KEY = "TOTAL_AMOUNT_KEY";
        String ADDRESS_FOR_DELIVERY = "ADDRESS_FOR_DELIVERY";

        String SHARED_PREF_PAYMENT_KEY = "SHARED_PREF_PAYMENT_KEY";
        String CARD_HOLDER_NAME = "CARD_HOLDER_NAME";
        String GET_CARD_HOLDER_NAME = "GET_CARD_HOLDER_NAME";
        String CARD_NUMBER = "CARD_NUMBER";
        String CARD_LIST = "CARD_LIST";
        String GET_CARD_NUMBER = "GET_CARD_NUMBER";
        String PAYMENT_MODE = "PAYMENT_MODE";
        String COMPLETE_ORDER_DETAIL = "COMPLETE_ORDER_DETAIL";
        String MY_ORDER_FRAGMENT_DETAIL = "MY_ORDER_FRAGMENT_DETAIL";
    }

    /* Fragments TAG*/
    public interface FRAGMENT {

        String MENU = "My Pizza's Menu";
        String SETTINGS = "Settings";
        String MY_PROFILE = "My Profile";
        String LOGIN = "Login";
        String SIGN_UP = "Sign up";
        String FORGOT_PASSWORD = "Forgot Password";
        String ABOUT_US = "About us";
        String ORDER_HISTORY = "Order History";
        String MY_ORDERS = "My Orders";
        String FEEDBACK_FRAGMENT = "Feedback";
        String PLACE_ORDER_FRAGMENT = "Place Order";
        String MY_CART_FRAGMENT = "My Cart";
        String CONFIRM_ORDER_FRAGMENT = "Confirm Order";
        String AGREEMENT = "Agreement";
        String ORDER_STATUS_FRAGMENT = "My Order Status";
        String REPORT_AN_ISSUE = "Report an Issue";
        String EDIT_PROFILE = "Edit Profile";
        String PAYMENT = "Payment";
        String ADD_CARD_FRAGMENT = "Add Card";
        String ORDER_PLACED_SUCCESS = "Order Placed";
    }
}
