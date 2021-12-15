package com.rhythm.mypizzaapp.utils;

import android.view.View;

/** ClickListener interface for recyclerView item click
 *
 * */
public interface RecyclerViewClickListener {
    // here we are sending item click position, and view of the item
    void itemClickListener(View view, int position);

    // this method is used to send any kind of integer value, we can send subTotal, we can send item click position
    // or we can send any other integer value according to our need.
    void clickListenerWithStringAndPosition(int position, String string);
}
