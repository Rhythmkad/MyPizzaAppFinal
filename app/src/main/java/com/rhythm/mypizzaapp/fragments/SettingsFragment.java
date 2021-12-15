package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;

/**
 * Settings fragment that show notification switch button,
 * privacy policy, and terms & conditions.
 * */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    protected RelativeLayout mPrivacyPolicyLayout, mTermsAndConditionLayout;
    protected Context mContext;
    private Switch mNotificationSwitch;
    private TextView mPolicyTv, mTermsAndConditionsTv;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.SETTINGS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initializing views
        initialize(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // initializing views
    private void initialize(View view) {

        mNotificationSwitch = view.findViewById(R.id.notification_switch_btn);
        mPrivacyPolicyLayout = view.findViewById(R.id.privacy_policy_layout);
        mTermsAndConditionLayout = view.findViewById(R.id.terms_condition_layout);

        mPolicyTv = view.findViewById(R.id.policy_tv);
        mTermsAndConditionsTv = view.findViewById(R.id.terms_tv);
        /* click listeners */
        setClickListeners();
    }

    // setting click listeners
    private void setClickListeners() {
        mPrivacyPolicyLayout.setOnClickListener(this);
        mTermsAndConditionLayout.setOnClickListener(this);
    }

    /*
     * OnClickListener method
     * */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.privacy_policy_layout:
                intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, mContext.getResources().getString(R.string.terms_conditions));
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.AGREEMENT);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.ONLINE_DOCUMENT_TYPE, mPolicyTv.getText().toString());  // sending which policy is clicked on screen
                mContext.startActivity(intent);
                break;
            case R.id.terms_condition_layout:
                intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, mContext.getResources().getString(R.string.privacy_policy_txt));
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.AGREEMENT);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.ONLINE_DOCUMENT_TYPE, mTermsAndConditionsTv.getText().toString());  // sending which policy is clicked on screen
                mContext.startActivity(intent);
                break;
        }
    }
}