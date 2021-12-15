package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rhythm.mypizzaapp.AuthenticationActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;

/** Forgot password Fragment
 * that takes phoneNumber or email id of the user
 * to send the password.
 *  */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, AlertDialogInterface {

    private Context mContext;
    private EditText mPhoneNumberEdt, mOtpEdt;
    private TextView mSendBtnTv, mSubmitBtn;
    private LinearLayout mEnterOtpLayout;
    private Dialog mDialog;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void initialize(View view) {

        View mIncludeEmailEdt = view.findViewById(R.id.custom_edittext_forgot_password);
        View mIncludeSubmitBtn = view.findViewById(R.id.submit_custom_attribute_forgot_btn);
        mEnterOtpLayout = view.findViewById(R.id.otp_edt_layout);
        mPhoneNumberEdt = mIncludeEmailEdt.findViewById(R.id.forgot_password_edt);
        mSendBtnTv = mIncludeEmailEdt.findViewById(R.id.send_forgot_password_tv);
        mSubmitBtn = mIncludeSubmitBtn.findViewById(R.id.common_btn);
        mOtpEdt = view.findViewById(R.id.otp_edit_text);

        /*
         * Runtime changes
         * */
        setRuntimeChanges();

        /*
         * Click listeners
         * */
        setClickListeners();

    }


    /*
     * Runtime changes
     * */
    private void setRuntimeChanges() {
        mEnterOtpLayout.setVisibility(View.INVISIBLE);
        mSubmitBtn.setText(mContext.getResources().getString(R.string.submit_btn_txt));
        mPhoneNumberEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);  // setting phone edit text font style
        mPhoneNumberEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)}); // setting phone number length to maximum 10 digits.
        mPhoneNumberEdt.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));  // setting phone edit text font style

    }

    /*
     * Click listener method
     * */
    private void setClickListeners() {
        mSendBtnTv.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        AlertDialogClass alertDialogClass = new AlertDialogClass();
        switch (v.getId()) {
            case R.id.send_forgot_password_tv:
                if (!mPhoneNumberEdt.getText().toString().isEmpty()) {
                    if (mPhoneNumberEdt.length() < 10) {
                        mDialog = alertDialogClass.showAlertDialog(mContext,"", getString(R.string.enter_valid_phoneNumber), this, false, "OK", "", 0);
                        if (mDialog != null) {
                            mDialog.show();
                        }
                    }else {
                        mEnterOtpLayout.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    mDialog = alertDialogClass.showAlertDialog(mContext,"", getString(R.string.enter_phone_number), this, false, "OK", "", 0);
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }
                break;
            case R.id.common_btn: // submitBtn
                // calling login fragment
                ((AuthenticationActivity) mContext).callFragments(GlobalConstants.FRAGMENT.LOGIN, GlobalConstants.FRAGMENT.LOGIN, 0, false);
                break;
        }
    }


    @Override
    public void onDialogConfirmAction(int position) {
        if (mDialog != null){
            mDialog.dismiss();
        }
    }

    @Override
    public void onDialogCancelAction(int position) {

    }
}