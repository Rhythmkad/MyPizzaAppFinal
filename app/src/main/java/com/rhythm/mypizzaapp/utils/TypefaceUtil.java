package com.rhythm.mypizzaapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/** Typeface util class is used to set the default typeface or we can say default font
 * of the application.
 * we are using this class to change the font style at of edittext, textview through
 * programmatically.
 * We used typeface to override the inbuilt font styles of android to our own
 * Font styles in the assets folder.
 * **/
public class TypefaceUtil {

    private static TypefaceUtil mInstance = null;

    private TypefaceUtil() {
    }

    public static TypefaceUtil getInstance() {
        if (mInstance == null)
            mInstance = new TypefaceUtil();
        return mInstance;
    }

    public void overrideFont(Context mContext, String defaultFontNameToOverride, String customFontFileNameInAssets) {

        final Typeface customFontTypeface = Typeface.createFromAsset(mContext.getAssets(), customFontFileNameInAssets);

        Map<String, Typeface> newMap = new HashMap<>();
        //  newMap.put("serif", customFontTypeface);
        newMap.put("sans_serif", customFontTypeface);
        try {
            @SuppressLint("DiscouragedPrivateApi") final Field staticField = Typeface.class
                    .getDeclaredField("sSystemFontMap");
            staticField.setAccessible(true);
            staticField.set(null, newMap);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Using reflection to override default typeface
     *
     *
     * @param mContext                   to work with assets
     * @param customFontFileNameInAssets file name of the font from assets
     */

    private Typeface getCustomTypeFace(Context mContext, String customFontFileNameInAssets) {
        return (Typeface.createFromAsset(mContext.getAssets(), customFontFileNameInAssets));
    }

    // setting font style
    public Typeface getRegularTypeFace(Context mContext) {
        return (getCustomTypeFace(mContext, "fonts/rounded_elegance.ttf"));
    }

}
