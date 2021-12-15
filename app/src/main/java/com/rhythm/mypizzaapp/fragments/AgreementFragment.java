package com.rhythm.mypizzaapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;

import java.io.InputStream;

/**
 * This fragment shows the Terms and conditions of our application
 * and Privacy policies of our application.
 */
public class AgreementFragment extends Fragment {

    private TextView mAgreementTv;
    private String mTitle;
    private static final String PRIVACY_POLICY = "Privacy Policy";
    private static final String Terms_AND_CONDITIONS = "Terms & Conditions";
    private Context mContext;

    // constructor
    public AgreementFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AgreementFragment(String title) {
        // here we are getting the title of this fragment
        // because this fragment is common for Privacy policy and terms & conditions
        // so according to this string we are calling the
        // agreement files
        mTitle = title;
    }


    // fragment lifecycle method
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agreement, container, false);
    }

    // fragment lifecycle method
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initializing views
        initialize(view);
    }

    // fragment lifecycle method
    @Override
    public void onResume() {
        super.onResume();
        // setting title
        ((CommonActivity)mContext).setToolbarTitle(mTitle);
    }

    // fragment lifecycle method
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // initializing views
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initialize(View view) {
        mAgreementTv = view.findViewById(R.id.agreement_tv);

        /* setting typeface */
        setTypeFace();
        /* Setting agreement */
        setAgreement();
    }

    // setting typeface for the text view
    private void setTypeFace() {
        mAgreementTv.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));
    }

    // setting agreement file for the textView
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAgreement() {
        switch (mTitle){
            case PRIVACY_POLICY:
                try {
                    Resources res = getResources();
                    InputStream inputStream = res.openRawResource(R.raw.privacy_policy);
                    byte[] b = new byte[inputStream.available()];
                    inputStream.read(b);
                    mAgreementTv.setText(Html.fromHtml(new String(b)));
                } catch (Exception e) {
                    mAgreementTv.setText(R.string.dummy_agreement);
                }
                break;
            case Terms_AND_CONDITIONS:
                try {
                    Resources res = getResources();
                    InputStream inputStream = res.openRawResource(R.raw.terms_and_conditions);
                    byte[] b = new byte[inputStream.available()];
                    inputStream.read(b);
                    mAgreementTv.setText(Html.fromHtml(new String(b)));
                } catch (Exception e) {
                    mAgreementTv.setText(R.string.dummy_agreement);
                }
        }
    }
}
