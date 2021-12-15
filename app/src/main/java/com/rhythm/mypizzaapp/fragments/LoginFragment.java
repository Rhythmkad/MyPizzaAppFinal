package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rhythm.mypizzaapp.AuthenticationActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.SignupBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;
import com.rhythm.mypizzaapp.utils.ValidationsClass;
import com.rhythm.mypizzaapp.utils.localdatabase.SignupDatabaseHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

/** Login fragment where user enters its email
 * and password to logged in to application.
 * */

public class LoginFragment extends Fragment implements View.OnClickListener, AlertDialogInterface {

    private Context mContext;
    private TextView mForgotPasswordTv, mSignUpTv;
    private Button mLoginBtn;
    private EditText mEmailEdt, mPasswordEdt;
    private ImageView mPasswordIconIv;
    private View mIncludeLoginBtn, mIncludeEmailEdt, mIncludePasswordEdt;
    private Dialog mDialog;
    private SharedPreferenceClass sharedPreferenceClass;
    private SharedPreferences sharedPreferences;
    private SignupDatabaseHelper databaseHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Fragment lifecycle method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // Fragment lifecycle method
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,Context.MODE_PRIVATE);

        if (sharedPreferences != null){
           boolean isAlreadyLoggedIn = sharedPreferences.getBoolean(GlobalConstants.SHARED_PREFERENCE.IS_LOGGED_IN, false);
           if (isAlreadyLoggedIn){
               Intent intent = new Intent(mContext, MainActivity.class);
               intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MENU);
               intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MENU);
               startActivity(intent);
               ((AuthenticationActivity) mContext).finish();
           }else{
               initialize(view);
           }
        }else{
            initialize(view);
        }

    }

    // Fragment lifecycle method
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // this is an abstract class whose implementation is provided by the Android system.
        // It allows access to application-specific resources and classes,
        // as well as up-calls for application-level operations such as launching activities,
        // broadcasting and receiving intents, etc.
        mContext = context; // initializing context
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // onDestroy we will dismiss the dialog and set it to null.
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    // initializing the ids
    protected void initialize(View view) {

        databaseHelper = new SignupDatabaseHelper(mContext);

        sharedPreferenceClass = new SharedPreferenceClass(mContext);
        sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,Context.MODE_PRIVATE);

        /* fetching include layouts */
        mIncludeEmailEdt = view.findViewById(R.id.enter_email_include_tv);
        mIncludePasswordEdt = view.findViewById(R.id.enter_password_include_tv);
        mIncludeLoginBtn = view.findViewById(R.id.login_include_layout);

        /* fetching text view id's */
        mForgotPasswordTv = view.findViewById(R.id.forgot_password_tv);
        mLoginBtn = mIncludeLoginBtn.findViewById(R.id.common_btn); //login btn text view
        mSignUpTv = view.findViewById(R.id.sign_up_tv);

        /* fetching edit text id's */
        mEmailEdt = mIncludeEmailEdt.findViewById(R.id.common_edit_text);  // email edit text
        mPasswordEdt = mIncludePasswordEdt.findViewById(R.id.common_edit_text);  // password edit text

        /* fetching imageView icon id */
        mPasswordIconIv = mIncludePasswordEdt.findViewById(R.id.common_edit_text_icon);  // password edit text icon

        /*
         * Runtime changes
         * */
        setDataAtRunTime();

        /*
         * setClick listeners
         * */
        setClickListeners();
    }

    /*
     * setClick listeners
     * */
    protected void setClickListeners() {
        mForgotPasswordTv.setOnClickListener(this);
        mSignUpTv.setOnClickListener(this);
        mIncludeLoginBtn.setOnClickListener(this);
    }

    /*
     * Runtime changes
     * */
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    protected void setDataAtRunTime() {
        mLoginBtn.setAllCaps(true); // show string on button in all caps
        mPasswordEdt.setHint(mContext.getResources().getString(R.string.password_txt));  // setting password edit text hint
        mPasswordIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_password));  // setting password edit text icon

        mEmailEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        mPasswordEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        mEmailEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mEmailEdt.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));
        // setting InputType for password edittext dynamically, because we are using
        // this edit text layout as common layout, which can be used in any other screen
        // we don't need to write the whole code again.
        mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordEdt.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext)); // setting the font style of the edittext

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.forgot_password_tv:
                // Call to forgot password fragment
                ((AuthenticationActivity) mContext).callFragments(GlobalConstants.FRAGMENT.FORGOT_PASSWORD, GlobalConstants.FRAGMENT.FORGOT_PASSWORD, 0, false);
                break;
            case R.id.login_include_layout:
                String userName = mEmailEdt.getText().toString();
                String userPassword = mPasswordEdt.getText().toString();
                checkValidations(userName, userPassword);
                break;
            case R.id.sign_up_tv:
                // here when user click sign up text, we will call signup fragment
                ((AuthenticationActivity) mContext).callFragments(GlobalConstants.FRAGMENT.SIGN_UP, GlobalConstants.FRAGMENT.SIGN_UP, 0, false);
                break;
        }
    }

//    checking login valdations
    private void checkValidations(String userName, String userPassword) {

        Intent intent;
        AlertDialogClass alertDialogClass = new AlertDialogClass();
        ValidationsClass mValidationCheck = new ValidationsClass();

        // here if both the edit text are empty it will show an alert dialog to the user.
        if (!mValidationCheck.checkStringNull(userName) && !mValidationCheck.checkStringNull(userPassword)){
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.login_failed),getString(R.string.login_fields_cannot_be_empty),this,false,"OK","",0);
            if (mDialog != null){
                mDialog.show();
            }
        } // here if entered email ID is empty or not valid it will show alert dialog message.
        else if (!mValidationCheck.checkStringNull(userName) || !mValidationCheck.isValidEmail(userName)){
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.login_failed),getString(R.string.enter_valid_email),this, false, "OK","",0);
            if (mDialog != null){
                mDialog.show();
            }
        } // here if entered password is empty or not valid it will show alert dialog message.
        else if (!mValidationCheck.checkStringNull(userPassword) || !mValidationCheck.isValidPassword(userPassword)){
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.login_failed),getString(R.string.enter_valid_password),this, false,"OK","",0);
            if (mDialog != null){
                mDialog.show();
            }
        }else {
            // here when user click login button, MainActivity will call and we send
            // in MainActivity which fragment we want to call, here we
            // will send putExtra to send which fragment we want to call..

            // here we are checking if the entered email and password is
            // equal to that which is stored in the Shared preference or not..
            // if not it will show an alert dialog and if both are true
            // user logged into the application and we set setLoggedIn() to true
            // so that when user close the application and opens it again..
            // he is already logged into the application, he do not need to re-enter his login details.
            if (databaseHelper.checkUser(userName.trim()
                    , userPassword.trim())) {
                sharedPreferenceClass.setLoggedIn(true);
                sharedPreferenceClass = new SharedPreferenceClass(mContext, databaseHelper.getUserDetail(userName));

                intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MENU);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MENU);
                startActivity(intent);
                ((AuthenticationActivity) mContext).finish();
            } else {
                mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.login_failed), "Incorrect email or password", this, false, "OK", "", 0);
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