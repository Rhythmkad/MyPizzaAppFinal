package com.rhythm.mypizzaapp.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

/** This is the common alert dialog class
 * which shows the AlertDialog when showAlertDialog method is called */
public class AlertDialogClass {

    // show alert dialog message if user enters any invalid details in the login section
    public Dialog showAlertDialog(Context context, String title, String message, AlertDialogInterface dialogInterface, boolean isCancelable, String submitBtnTxt, String cancelBtnTxt, int recyclerViewItemPosition){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(isCancelable);
        if (!title.isEmpty())
            builder.setTitle(title);
        if (!message.isEmpty())
            builder.setMessage(message);
        builder.setPositiveButton(submitBtnTxt, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
//                dialog.dismiss();
                // performs action on positive button click
                dialogInterface.onDialogConfirmAction(recyclerViewItemPosition);
            }
        });
        builder.setNegativeButton(cancelBtnTxt, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                // performs action on negative button click
                dialogInterface.onDialogCancelAction(recyclerViewItemPosition);
            }
        });
// Create the AlertDialog object and return it
        return builder.create();
    }
}
