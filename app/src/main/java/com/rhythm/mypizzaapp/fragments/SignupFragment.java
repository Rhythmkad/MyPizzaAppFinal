package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rhythm.mypizzaapp.AuthenticationActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.SignupBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;
import com.rhythm.mypizzaapp.utils.ValidationsClass;
import com.rhythm.mypizzaapp.utils.localdatabase.SignupDatabaseHelper;

import java.util.ArrayList;

/** Signup fragment is where user enters its details like
 * name, email, phone, address, password to signup
 * in the application.
 * */

public class SignupFragment extends Fragment implements View.OnClickListener, AlertDialogInterface {

    private TextView mAlreadyHaveAccountTv, mLoginTv;
    private Button mSignUpBtn;
    private EditText mEmailEdt, mPasswordEdt, mAddressEdt, mPhoneEdt, mNameEdt, mConfirmPasswordEdt;
    private ImageView mPasswordIconIv, mAddressIconIv, mNameIconIv, mPhoneIconIv, mConfirmPasswordIconIv;
    private RadioGroup mSexRadioGrp;
    private RadioButton mGenderRadioBtn;
    private Context mContext;
    private View mIncludeSignUpTv;
    private ValidationsClass mValidationClass;
    private Dialog mDialog;
    private ArrayList<SignupBeanClass> signUpDataList;
    private SignupDatabaseHelper signupDatabaseHelper;
    private SignupBeanClass signupBeanClass;

    public SignupFragment() {
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
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    protected void initialize(View view) {

        signupDatabaseHelper = new SignupDatabaseHelper(mContext);
        signupBeanClass = new SignupBeanClass();

        /* fetching include layouts */
        View mIncludeEmailEdt = view.findViewById(R.id.enter_email_include_tv);
        View mIncludePasswordEdt = view.findViewById(R.id.enter_password_include_tv);
        View mIncludeConfirmPasswordEdt = view.findViewById(R.id.enter_confirm_password_include_tv);
        View mIncludeAddressEdt = view.findViewById(R.id.enter_city_include_tv);
        mIncludeSignUpTv = view.findViewById(R.id.sign_up_include_layout);
        View mIncludeNameEdt = view.findViewById(R.id.enter_name_include_tv);
        View mIncludePhoneEdt = view.findViewById(R.id.enter_phone_include_tv);

        /* fetching text view id's */
        mSignUpBtn = mIncludeSignUpTv.findViewById(R.id.common_btn); //sign btn text view
        mAlreadyHaveAccountTv = view.findViewById(R.id.already_have_account_tv); // already have an account edt
        mLoginTv = view.findViewById(R.id.login_tv);  // login text

        /* fetching edit text id's */
        mEmailEdt = mIncludeEmailEdt.findViewById(R.id.common_edit_text);  // email edit text
        mPasswordEdt = mIncludePasswordEdt.findViewById(R.id.common_edit_text);  // password edit text
        mConfirmPasswordEdt = mIncludeConfirmPasswordEdt.findViewById(R.id.common_edit_text);  // password edit text
        mAddressEdt = mIncludeAddressEdt.findViewById(R.id.common_edit_text);  // city edit text
        mNameEdt = mIncludeNameEdt.findViewById(R.id.common_edit_text);  // name edit text
        mPhoneEdt = mIncludePhoneEdt.findViewById(R.id.common_edit_text);  // phone edit text

        /* fetching imageView icon id */
        mPasswordIconIv = mIncludePasswordEdt.findViewById(R.id.common_edit_text_icon);  // password edit text icon
        mConfirmPasswordIconIv = mIncludeConfirmPasswordEdt.findViewById(R.id.common_edit_text_icon);  // password edit text icon
        mAddressIconIv = mIncludeAddressEdt.findViewById(R.id.common_edit_text_icon);  // city edit text icon
        mNameIconIv = mIncludeNameEdt.findViewById(R.id.common_edit_text_icon);  // name edit text icon
        mPhoneIconIv = mIncludePhoneEdt.findViewById(R.id.common_edit_text_icon);  // phone edit text icon

        /* fetching RadioGroup id */
        mSexRadioGrp = view.findViewById(R.id.sex_radioGrp);

        /*
         * Runtime changes
         * */
        setDataAtRunTime();

        /*
         * Setting text font at runtime
         * */
        setTypeFaceAtRunTime();

        /*
         * setClick listeners
         * */
        setClickListeners();
    }

    /*
     * Runtime changes
     * here we are setting EditText Icons, hints and typeface at runtime
     * because we have created a common edit text to use it at various locations
     * of the applications.
     * */
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    protected void setDataAtRunTime() {
        mSignUpBtn.setText(mContext.getResources().getString(R.string.signup_login_screen_txt));
        mSignUpBtn.setAllCaps(true);

        mEmailEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);  // setting email edit text input type
        mEmailEdt.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));  // setting email edit text font style
        mEmailEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting email edit text color

        mPasswordEdt.setHint(mContext.getResources().getString(R.string.password_txt));  // setting password edit text hint
        mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // setting password edit text input type
        mPasswordEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting password edit text color
        mPasswordIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_password));  // setting password edit text icon

        mConfirmPasswordEdt.setHint(mContext.getResources().getString(R.string.confirm_password_hint_txt));  // setting password edit text hint
        mConfirmPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // setting password edit text input type
        mConfirmPasswordEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting password edit text color
        mConfirmPasswordIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_password));  // setting password edit text icon

        mAddressIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_location));  // setting address edit text icon
        mAddressEdt.setHint(mContext.getResources().getString(R.string.address_txt));  // setting address edit text hint
        mAddressEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);  // setting address edit text input type
        mAddressEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting address edit text color
        mAddressEdt.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));  // setting address edit text font style

        mNameIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_my_profile));  // setting name edit text icon
        mNameEdt.setHint(mContext.getResources().getString(R.string.name_hint_txt));  // setting name edit text hint
        mNameEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting name edit text color

        mPhoneIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_phone));  // setting phone edit text icon
        mPhoneEdt.setHint(mContext.getResources().getString(R.string.phone_number_hint_txt));  // setting phone edit text hint
        mPhoneEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);  // setting phone edit text input type
        mPhoneEdt.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));  // setting phone edit text font style
        mPhoneEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});  // setting phone edit text length filter
        mPhoneEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting phone edit text color

    }

    /*
     * Setting text font at runtime
     * Changing fonts of edit texts
     * */
    protected void setTypeFaceAtRunTime() {
        TypefaceUtil typefaceUtil = TypefaceUtil.getInstance();
        mSignUpBtn.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mLoginTv.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mAlreadyHaveAccountTv.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mEmailEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mPasswordEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mConfirmPasswordEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mAddressEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mNameEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mPhoneEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
    }

    /*
     * setClick listeners
     * */
    protected void setClickListeners() {
        mIncludeSignUpTv.setOnClickListener(this);
        mLoginTv.setOnClickListener(this);

        mSexRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                mGenderRadioBtn = radioGroup.findViewById(id);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        mValidationClass = new ValidationsClass();
        switch (view.getId()) {
            case R.id.login_tv:
                // call to login fragment
                ((AuthenticationActivity) mContext).callFragments(GlobalConstants.FRAGMENT.LOGIN, GlobalConstants.FRAGMENT.LOGIN, 0, false);
                break;
            case R.id.sign_up_include_layout:
                signUpUser();
                break;
        }
    }

    private void signUpUser() {
        AlertDialogClass alertDialogClass = new AlertDialogClass();
        String userName = mNameEdt.getText().toString();
        String userEmail = mEmailEdt.getText().toString();
        String phoneNumber = mPhoneEdt.getText().toString();
        String password = mPasswordEdt.getText().toString();
        String confirmPassword = mConfirmPasswordEdt.getText().toString();
        String address = mAddressEdt.getText().toString();

        if (!mValidationClass.checkStringNull(userName) || !mValidationClass.checkStringNull(password) || !mValidationClass.checkStringNull(userEmail)
                || !mValidationClass.checkStringNull(phoneNumber) || !mValidationClass.checkStringNull(address) || !mValidationClass.checkStringNull(confirmPassword)) {
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.signup_failed),getString(R.string.sign_up_fields_cannot_be_empty), this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        } else if (!mValidationClass.isValidEmail(userEmail)) {
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.signup_failed),getString(R.string.enter_valid_email), this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        }else if (!mValidationClass.isValidPassword(password)) {
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.signup_failed),getString(R.string.password_validation_txt), this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        } else if (!password.equals(confirmPassword)) {
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.signup_failed),getString(R.string.password_confirm_not_match),this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        } else {
            String gender = "";
            // here we are checking whether the gender radio button is selected or not
            if (mGenderRadioBtn != null) {
                // here we are storing the user the details in the local sql Lite database
                gender = mGenderRadioBtn.getText().toString();
                if (!signupDatabaseHelper.checkUser(userEmail.trim())) {
                    signupBeanClass.setName(userName);
                    signupBeanClass.setEmail(userEmail);
                    signupBeanClass.setPassword(password);
                    signupBeanClass.setPhoneNumber(phoneNumber);
                    signupBeanClass.setGender(gender);
                    signupBeanClass.setAddress(address);
                    signupDatabaseHelper.addUser(signupBeanClass);
                    // Toast to show success message that record saved successfully
                    ((AuthenticationActivity) mContext).callFragments(GlobalConstants.FRAGMENT.LOGIN, GlobalConstants.FRAGMENT.LOGIN, 0, false);
                    Toast.makeText(mContext, "Successfully Signed-up", Toast.LENGTH_SHORT).show();
                }else{
                    // here if email already exists than it will show email already exist, alert dialog
                    mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.signup_failed), getString(R.string.email_already_exist), this, false, "OK", "", 0);
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }
            } else {
                mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.signup_failed), getString(R.string.select_gender), this, false, "OK", "", 0);
                if (mDialog != null) {
                    mDialog.show();
                }
            }
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