package com.rhythm.mypizzaapp.utils;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/** This is a common string formatter class.
 * Which can be used to format string
 * */
public class StringFormatterClass {

    public StringFormatterClass() {
    }

    /* return "i am good" to "I Am Good" */
    public String firstLetterCapsInWholeSentence(String string) {

        if (string != null && !string.isEmpty()) {
            string = string.trim();
            if (!string.isEmpty()) {
                String[] splitString = string.split(" ");
                String newStringValue = "";
                for (String caps : splitString) {
                    newStringValue = newStringValue.concat(firstLetterCaps(caps)) + " ";
                }
                return newStringValue.trim();
            } else return "";
        } else return "";
    }

    /* this function return like "hello" to "Hello" */
    public String firstLetterCaps(String str) {
        if (str != null && !str.isEmpty()) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

    /* return "9111144444" to "91-111-44444" */
    public String getEditedPhoneNumber(String phoneNumber) {

        if (new ValidationsClass().checkStringNull(phoneNumber)) {
            String firstThree = phoneNumber.substring(0, 2);
            String midThree = phoneNumber.substring(2, 5);
            String lastFour = phoneNumber.substring(5);

            phoneNumber = firstThree + "-" + midThree + "-" + lastFour;
            return phoneNumber;
        } else {
            return phoneNumber;
        }
    }

    // return formatted date according to time zone and user format.
    public String getFormattedDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss", Locale.getDefault());
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("Canada/Atlantic"));

//        String[] timeZone = TimeZone.getAvailableIDs();
//        for (String i : timeZone){
        // to get different time zone
//            Log.println(Log.INFO,"LIST_OF_TIME_ZONES", i);
//        }

//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Atlantic
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Central
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/East-Saskatchewan
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Eastern
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Mountain
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Newfoundland
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Pacific
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Saskatchewan
//        2021-12-09 21:03:45.399 23136-23136/com.rhythm.mypizzaapp I/LIST_OF_TIME_ZONES: Canada/Yukon
        return (mSimpleDateFormat.format(mCalendar.getTime()));
    }

    public String getTodayDate(String mFormat) {
        Calendar mCalendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mFormat, Locale.getDefault());
        return (mSimpleDateFormat.format(mCalendar.getTime()));
    }

    /* Get first 4 characters of the string */
    public String getFirst4Characters(String string) {
        if (string != null && !string.isEmpty()) {
            if (string.length() <= 4) {
                return string;
            } else {
                return string.substring(0,4);
            }
        } else return "";
    }

    /* , it will transform "4111111111111111" to "●●●● ●●●● ●●●● 1111". */
    public String getCardTransformation(final String source) {

        final String FOUR_DOTS = "••••";
        if (source.length() > 4) {
            String result = FOUR_DOTS +
                    " " +
                    FOUR_DOTS +
                    " " +
                    FOUR_DOTS +
                    " " +
                    source.substring(source.length() - 4);
            return result;
        }

        return source;
    }

}
