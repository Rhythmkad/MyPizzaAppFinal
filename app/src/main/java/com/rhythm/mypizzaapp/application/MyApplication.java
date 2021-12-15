package com.rhythm.mypizzaapp.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.rhythm.mypizzaapp.utils.TypefaceUtil;

/**
 * The Application class in Android is the base class within an Android app that contains all other
 * components such as activities and services. The Application class,
 * or any subclass of the Application class, is instantiated before any other class when the
 * process for your application/package is created.
 * */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        /* Override FontStyle from Assets */
        TypefaceUtil.getInstance().overrideFont(getApplicationContext(), "sans_serif", "fonts/rounded_elegance.ttf"); // font from assets: "assets/fonts/rounded_elegance.ttf
    }

    public static synchronized MyApplication getApplication() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}