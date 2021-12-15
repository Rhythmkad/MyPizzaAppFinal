package com.rhythm.mypizzaapp.utils;

import android.graphics.drawable.Drawable;

import com.braintreepayments.cardform.utils.CardType;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Validation Class here is used to check
 * the validation of email and password entered by the user
 * and we also created a stringNull method to check
 * the entered string is null or not.
 * */
public class ValidationsClass {

    private static final String EMAIL_PATTERN =
            "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                    "(?:[\\x01-\\x07\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01" +
                    "-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x07\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public ValidationsClass() {
    }

    public boolean checkStringNull(String string) {
        return string != null && !string.equals("") && !string.isEmpty();
    }

    public boolean isValidEmail(String mEmail) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mEmail);
        return matcher.matches();
    }

    public boolean isValidPassword(String mPassword) {
        return mPassword.matches( "^.{8,15}$" );
    }

}
