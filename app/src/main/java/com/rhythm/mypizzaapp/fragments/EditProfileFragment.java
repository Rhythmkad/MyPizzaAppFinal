package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.SignupBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;
import com.rhythm.mypizzaapp.utils.ValidationsClass;
import com.rhythm.mypizzaapp.utils.localdatabase.SignupDatabaseHelper;

/**
 * Fragment to edit profile of the user
 * */

public class EditProfileFragment extends Fragment implements AlertDialogInterface, View.OnClickListener {

    private EditText mEmailEdt, mAddressEdt, mPhoneEdt, mNameEdt, mPasswordEdt, mConfirmPasswordEdt;
    private ImageView mAddressIconIv, mNameIconIv, mPhoneIconIv, mPasswordIconIv, mConfirmPasswordIconIv;
    private Button mSaveProfileBtn;
    private Context mContext;
    private View mIncludeSaveProfile;
    private String mUserPreviousEmail, mUserGender;
    private Dialog mDialog;

    public EditProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
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
    public void onResume() {
        super.onResume();
        // setting toolbar title
        ((CommonActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.EDIT_PROFILE);
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

    private void initialize(View view) {
        /* fetching include layouts */
        View mIncludeEmailEdt = view.findViewById(R.id.enter_email_edt);
        View mIncludeAddressEdt = view.findViewById(R.id.enter_address_edt);
        mIncludeSaveProfile = view.findViewById(R.id.save_profile_btn);
        View mIncludeNameEdt = view.findViewById(R.id.enter_name_edt);
        View mIncludePhoneEdt = view.findViewById(R.id.enter_phone_edt);
        View mIncludePasswordEdt = view.findViewById(R.id.enter_password_edt);
        View mIncludeConfirmEdt = view.findViewById(R.id.enter_confirm_password_edt);

        /* fetching text view id's */
        mSaveProfileBtn = mIncludeSaveProfile.findViewById(R.id.common_btn); //sign btn text view

        /* fetching edit text id's */
        mEmailEdt = mIncludeEmailEdt.findViewById(R.id.common_edit_text);  // email edit text
        mAddressEdt = mIncludeAddressEdt.findViewById(R.id.common_edit_text);  // city edit text
        mNameEdt = mIncludeNameEdt.findViewById(R.id.common_edit_text);  // name edit text
        mPhoneEdt = mIncludePhoneEdt.findViewById(R.id.common_edit_text);  // phone edit text
        mPasswordEdt = mIncludePasswordEdt.findViewById(R.id.common_edit_text);  // phone edit text
        mConfirmPasswordEdt = mIncludeConfirmEdt.findViewById(R.id.common_edit_text);  // phone edit text

        /* fetching imageView icon id */
        mAddressIconIv = mIncludeAddressEdt.findViewById(R.id.common_edit_text_icon);  // city edit text icon
        mNameIconIv = mIncludeNameEdt.findViewById(R.id.common_edit_text_icon);  // name edit text icon
        mPhoneIconIv = mIncludePhoneEdt.findViewById(R.id.common_edit_text_icon);  // phone edit text icon
        mPasswordIconIv = mIncludePasswordEdt.findViewById(R.id.common_edit_text_icon);  // phone edit text icon
        mConfirmPasswordIconIv = mIncludeConfirmEdt.findViewById(R.id.common_edit_text_icon);  // phone edit text icon

        /*
         * Runtime changes
         * */
        setDataAtRunTime();

        /*
         * Setting text font at runtime
         * */
        setTypeFaceAtRunTime();

        setRuntimeChanges();

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
        mSaveProfileBtn.setText(mContext.getResources().getString(R.string.update_profile));
        mSaveProfileBtn.setAllCaps(true);

        mEmailEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);  // setting email edit text input type
        mEmailEdt.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));  // setting email edit text font style
        mEmailEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting email edit text color

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

        mPasswordEdt.setHint(mContext.getResources().getString(R.string.password_txt));  // setting password edit text hint
        mPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // setting password edit text input type
        mPasswordEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting password edit text color
        mPasswordIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_password));  // setting password edit text icon

        mConfirmPasswordEdt.setHint(mContext.getResources().getString(R.string.confirm_password_hint_txt));  // setting password edit text hint
        mConfirmPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // setting password edit text input type
        mConfirmPasswordEdt.setTextColor(ContextCompat.getColor(mContext, R.color.black));  // setting password edit text color
        mConfirmPasswordIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_password));  // setting password edit text icon

    }

    /*
     * Setting text font at runtime
     * Changing fonts of edit texts
     * */
    protected void setTypeFaceAtRunTime() {
        TypefaceUtil typefaceUtil = TypefaceUtil.getInstance();
        mSaveProfileBtn.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mEmailEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mAddressEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mNameEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mPhoneEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mPasswordEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
        mConfirmPasswordEdt.setTypeface(typefaceUtil.getRegularTypeFace(mContext));
    }

    /*
     * Run time changes
     * */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setRuntimeChanges() {
        // here we are getting the previous details of the user through sharedPreferences
        // and setting it in editText
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,Context.MODE_PRIVATE);
        if (sharedPreferences != null){
            String userName = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_NAME,"");
            mUserPreviousEmail = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_EMAIL,"");
            mUserGender = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_GENDER, "");
            String userAddress = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_ADDRESS,"");
            String userPhone = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_PHONENUMBER,"");
            String password = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_PASSWORD,"");

            mNameEdt.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(userName));
            mEmailEdt.setText(mUserPreviousEmail);
            mPhoneEdt.setText(userPhone);
            mAddressEdt.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(userAddress));
            mPasswordEdt.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(password));
            mConfirmPasswordEdt.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(password));
        }else{
            // if shared preference is null we set dummy data
            mPhoneEdt.setText(new StringFormatterClass().getEditedPhoneNumber(mContext.getResources().getString(R.string.dummy_phone_number)));
            mEmailEdt.setText(mContext.getResources().getString(R.string.dummy_email));
            mAddressEdt.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(mContext.getResources().getString(R.string.dummy_address)));
            mNameEdt.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(mContext.getResources().getString(R.string.dummy_name)));
        }
        mAddressEdt.setMaxLines(2);
    }

    /*
     * setClick listeners
     * */
    protected void setClickListeners() {
        mIncludeSaveProfile.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_profile_btn:
                saveProfile();
                break;
        }
    }

    // saving and updating new profile
    private void saveProfile() {
        ValidationsClass mValidationClass = new ValidationsClass();
        AlertDialogClass alertDialogClass = new AlertDialogClass();
        String userName = mNameEdt.getText().toString();
        String userEmail = mEmailEdt.getText().toString();
        String phoneNumber = mPhoneEdt.getText().toString();
        String address = mAddressEdt.getText().toString();
        String password = mPasswordEdt.getText().toString();
        String confirmPassword = mConfirmPasswordEdt.getText().toString();
        SignupDatabaseHelper signupDatabaseHelper = new SignupDatabaseHelper(mContext);
        SignupBeanClass signupBeanClass = new SignupBeanClass();

        // checking if edit text is null or empty
        if (!mValidationClass.checkStringNull(userName) || !mValidationClass.checkStringNull(userEmail) || !mValidationClass.checkStringNull(password)
                || !mValidationClass.checkStringNull(confirmPassword) || !mValidationClass.checkStringNull(phoneNumber) || !mValidationClass.checkStringNull(address)) {
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.updation_failed), getString(R.string.sign_up_fields_cannot_be_empty), this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        } else if (!mValidationClass.isValidEmail(userEmail)) { // checking if email is valid or not
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.updation_failed), getString(R.string.enter_valid_email), this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        }else if (!mValidationClass.isValidPassword(password)) { // checking is password entered is valid or not
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.updation_failed),getString(R.string.password_validation_txt), this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        } else if (!password.equals(confirmPassword)) { // checking password and confirm password are equal or not
            mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.updation_failed),getString(R.string.password_confirm_not_match),this, false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        } else {

            // here we are setting the Bean class data, and calling updateUser method of
            // databaseHelper class to update the user details.
            if (!userEmail.equalsIgnoreCase(mUserPreviousEmail)){
                // here if user has changed email ID than it will check if
                // new email ID is already in the Database or not.
                if (!signupDatabaseHelper.checkUser(userEmail.trim())) {
                    signupBeanClass.setName(userName);
                    signupBeanClass.setEmail(userEmail);
                    signupBeanClass.setPhoneNumber(phoneNumber);
                    signupBeanClass.setPassword(password);
                    signupBeanClass.setGender(mUserGender);
                    signupBeanClass.setAddress(address);
                    signupDatabaseHelper.updateUser(signupBeanClass, mUserPreviousEmail);

                    // Toast to show success message that profile updated successfully
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MY_PROFILE);
                    intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MY_PROFILE);
                    startActivity(intent);
                    ((CommonActivity) mContext).finish();
                    Toast.makeText(mContext, "Successfully updated", Toast.LENGTH_SHORT).show();
                    new SharedPreferenceClass(mContext, signupDatabaseHelper.getUserDetail(userEmail));

                } else {
                    // here if email already exists than it will show email already exist, alert dialog
                    mDialog = alertDialogClass.showAlertDialog(mContext, getString(R.string.updation_failed), getString(R.string.email_already_exist), this, false, "OK", "", 0);
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }
            }else{
                // here if user has not changed its email ID than it is going
                // to update only the rest of the data of that user.
                signupBeanClass.setName(userName);
                signupBeanClass.setEmail(userEmail);
                signupBeanClass.setPhoneNumber(phoneNumber);
                signupBeanClass.setPassword(password);
                signupBeanClass.setGender(mUserGender);
                signupBeanClass.setAddress(address);
                signupDatabaseHelper.updateUser(signupBeanClass, mUserPreviousEmail);

                // Toast to show success message that profile updated successfully
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MY_PROFILE);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MY_PROFILE);
                startActivity(intent);
                ((CommonActivity) mContext).finish();
                Toast.makeText(mContext, "Successfully updated", Toast.LENGTH_SHORT).show();
                new SharedPreferenceClass(mContext, signupDatabaseHelper.getUserDetail(userEmail));
            }

        }
    }

    @Override
    public void onDialogConfirmAction(int position) {
        mDialog.dismiss();
    }

    @Override
    public void onDialogCancelAction(int position) {
    }
}