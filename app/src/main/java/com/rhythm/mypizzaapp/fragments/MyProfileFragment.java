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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rhythm.mypizzaapp.AuthenticationActivity;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.SignupBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;
import com.rhythm.mypizzaapp.utils.ValidationsClass;
import com.rhythm.mypizzaapp.utils.localdatabase.PaymentCardDatabaseHelper;
import com.rhythm.mypizzaapp.utils.localdatabase.SignupDatabaseHelper;

/**
 * This fragment shows the user details
 * */

public class MyProfileFragment extends Fragment implements View.OnClickListener, AlertDialogInterface {

    private ImageView mUserProfileIv, mPhoneIcon, mAddressIcon;
    private TextView mUserNameTv, mPhoneNumberTv, mEmailTv, mAddressTv, mDeleteAccountTv, mEditAccountTv;
    private Context mContext;
    private Dialog mDialog;

    public MyProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initializing IDs
        initialize(view);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.MY_PROFILE);
    }


    /*
     * Initializing id's
     * */
    private void initialize(View view) {

        View includePhoneLayout = view.findViewById(R.id.phone_layout);
        View includeEmailLayout = view.findViewById(R.id.email_layout);
        View includeAddressLayout = view.findViewById(R.id.address_layout);

        /* TextView */
        mPhoneNumberTv = includePhoneLayout.findViewById(R.id.aboutTv);
        mEmailTv = includeEmailLayout.findViewById(R.id.aboutTv);
        mAddressTv = includeAddressLayout.findViewById(R.id.aboutTv);
        mUserNameTv = view.findViewById(R.id.user_name_tv);
        mDeleteAccountTv = view.findViewById(R.id.delete_account_tv);
        mEditAccountTv = view.findViewById(R.id.edit_account_tv);
        mDeleteAccountTv.setOnClickListener(this);
        mEditAccountTv.setOnClickListener(this);

        /* imageView */
        mUserProfileIv = view.findViewById(R.id.user_profile_iv);
        mPhoneIcon = includePhoneLayout.findViewById(R.id.aboutIv);
        mAddressIcon = includeAddressLayout.findViewById(R.id.aboutIv);

        /* runtime changes */
        setRuntimeChanges();
    }

    /*
     * Run time changes
     * */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setRuntimeChanges() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,Context.MODE_PRIVATE);
        String gender = "";
        if (sharedPreferences != null){
            // setting user data from SharedPreferences
            String userName = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_NAME,"");
            String userEmail = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_EMAIL,"");
            String userAddress = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_ADDRESS,"");
            String userPhone = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_PHONENUMBER,"");
            gender = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_GENDER,"");

            mPhoneNumberTv.setText(new StringFormatterClass().getEditedPhoneNumber(userPhone));
            mEmailTv.setText(userEmail);
            mAddressTv.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(userAddress));
            mUserNameTv.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(userName));
        }else{
            mPhoneNumberTv.setText(new StringFormatterClass().getEditedPhoneNumber(mContext.getResources().getString(R.string.dummy_phone_number)));
            mEmailTv.setText(mContext.getResources().getString(R.string.dummy_email));
            mAddressTv.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(mContext.getResources().getString(R.string.dummy_address)));
            mUserNameTv.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(mContext.getResources().getString(R.string.dummy_name)));
        }
        mPhoneNumberTv.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        mEmailTv.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        mAddressTv.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        mPhoneIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_phone));
        mAddressIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_location));

        mAddressTv.setMaxLines(2);

        /*
         * setting profile image
         * */
        if (gender.equalsIgnoreCase("Male")) {
            Glide.with(this)
                    .load(R.drawable.male_avatar)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile_white)
                            .circleCrop())
                    .into(mUserProfileIv);
        }else{
            Glide.with(this)
                    .load(R.drawable.female_avatar)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile_white)
                            .circleCrop())
                    .into(mUserProfileIv);
        }
    }

    // onClick method
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_account_tv:
                mDialog = new AlertDialogClass().showAlertDialog(mContext,"Delete Account?","Are you sure you want to delete your account", this,false,"Yes","No",0);
                if (mDialog != null){
                    mDialog.show();
                }
                break;
            case R.id.edit_account_tv:
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.EDIT_PROFILE);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.EDIT_PROFILE);
                startActivity(intent);
                break;
        }
    }

    // AlertDialog interface methods
    @Override
    public void onDialogConfirmAction(int position) {
        SignupDatabaseHelper databaseHelper = new SignupDatabaseHelper(mContext);
        if (databaseHelper.checkUser(mEmailTv.getText().toString())) {
            // deleting user account
            if (databaseHelper.deleteUser(mEmailTv.getText().toString())) {
                // deleting payment table from the data base
                new PaymentCardDatabaseHelper(mContext).deleteTable();
                // setting logged in to false
                SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(mContext);
                sharedPreferenceClass.setLoggedIn(false);
                // clearing shared preferences
                sharedPreferenceClass.clearSharedPreferences();
                Intent intent = new Intent(mContext, AuthenticationActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.LOGIN);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.LOGIN);
                startActivity(intent);
                ((MainActivity) mContext).finishAffinity();
            }else{
                Toast.makeText(mContext, "User not found.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(mContext, "User not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogCancelAction(int position) {
        mDialog.dismiss();
    }

}