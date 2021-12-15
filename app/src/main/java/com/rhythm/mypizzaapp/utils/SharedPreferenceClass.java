package com.rhythm.mypizzaapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.getterAndSetterClasses.SignupBeanClass;

import java.util.ArrayList;

/** Shared Preferences is the way in which one can store and retrieve small amounts of primitive data
 * as key/value pairs to a file on the device storage
 * such as String, int, float, Boolean that make up your preferences in an XML file
 * inside the app on the device storage.
 *
 * */

public class SharedPreferenceClass {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;;
    private ArrayList<SignupBeanClass> signupDataList;

    public SharedPreferenceClass(Context context, ArrayList<SignupBeanClass> dataList){
        this.context = context;
        signupDataList = dataList;
        setSharedPreferences();
    }

    public SharedPreferenceClass(Context context) {
        this.context = context;
    }

    // creating sharedPreferences for signup list
    private void setSharedPreferences(){
        sharedPreferences = context.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (signupDataList != null) {
            for (int i = 0; i < signupDataList.size(); i++) {
                editor.putString(GlobalConstants.SHARED_PREFERENCE.KEY_NAME, signupDataList.get(i).getName());
                editor.putString(GlobalConstants.SHARED_PREFERENCE.KEY_EMAIL, signupDataList.get(i).getEmail());
                editor.putString(GlobalConstants.SHARED_PREFERENCE.KEY_ADDRESS, signupDataList.get(i).getAddress());
                editor.putString(GlobalConstants.SHARED_PREFERENCE.KEY_PHONENUMBER, signupDataList.get(i).getPhoneNumber());
                editor.putString(GlobalConstants.SHARED_PREFERENCE.KEY_PASSWORD, signupDataList.get(i).getPassword());
                editor.putString(GlobalConstants.SHARED_PREFERENCE.KEY_PASSWORD, signupDataList.get(i).getPassword());
                editor.putString(GlobalConstants.SHARED_PREFERENCE.KEY_GENDER, signupDataList.get(i).getGender());
            }
            editor.apply();
        }
    }

    // setting shared Preferences
    public void setSharedPreferences(String sharedPrefKey, String key, String value){
        sharedPreferences = context.getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // getting shared Preferences for subTotal, Tax and TotalAmount
    public String getPreferences(String mainKey, String valueKey) {
        SharedPreferences preferences = context.getSharedPreferences(mainKey, Context.MODE_PRIVATE);
        return preferences.getString(valueKey, "");
    }

    // setting logged in boolean value, if user has logged in set to true
    // than user did not need to enter the login details again, it will skip login screen
    // and show the home screen, and if false than it show the login screen.
    public void setLoggedIn(boolean isLoggedIn){
        sharedPreferences = context.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(GlobalConstants.SHARED_PREFERENCE.IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }


    public void putString(String mObjectKey, String mObjectValue) {
        SharedPreferences mPreferences = context.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREF_PAYMENT_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString(mObjectKey, mObjectValue);
        mEditor.apply();

    }

    // this method clear the entire data of shared preference
    public void clearSharedPreferences(){
        sharedPreferences = context.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
