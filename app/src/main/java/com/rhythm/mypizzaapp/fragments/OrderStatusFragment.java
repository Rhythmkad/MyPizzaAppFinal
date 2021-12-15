package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;

import java.text.DecimalFormat;

/**
 * This fragment shows the order status, delivery address, amount, payment mode
 * and a help button to report an issue
 * */

public class OrderStatusFragment extends Fragment implements View.OnClickListener{

    private TextView mSubTotalTv, mTaxTv, mTotalTv, mCustomerAddressTv;
    private Context mContext;
    private SeekBar mSeekBar;
    private ImageView mDeliveryModeIv;

    public OrderStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CommonActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.ORDER_STATUS_FRAGMENT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* initializing values */
        initialize(view);
    }

    /* initializing values */
    private void initialize(View view) {

        mCustomerAddressTv = view.findViewById(R.id.customer_address_tv);  // sub total text view
        TextView helpCenterTv = view.findViewById(R.id.help_center_tv);  // sub total text view
        mSubTotalTv = view.findViewById(R.id.sub_total_value_tv);  // sub total text view
        mTaxTv = view.findViewById(R.id.tax_value_tv);  // tax text view
        mTotalTv = view.findViewById(R.id.total_value_tv); // total text view
        mSeekBar = view.findViewById(R.id.seekbar_progress); // total text view
        mDeliveryModeIv = view.findViewById(R.id.delivery_mode_iv); // total text view

        helpCenterTv.setOnClickListener(this);

        // setting values here
        setValues();
    }

    // getting values from sharedPreferences and setting textViews here
    @SuppressLint("ClickableViewAccessibility")
    private void setValues() {
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(mContext);

        String subTotal = sharedPreferenceClass.getPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, GlobalConstants.SHARED_PREFERENCE.SUB_TOTAL_AMOUNT_KEY);
        String address = sharedPreferenceClass.getPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, GlobalConstants.SHARED_PREFERENCE.ADDRESS_FOR_DELIVERY);
        String paymentMode = sharedPreferenceClass.getPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, GlobalConstants.SHARED_PREFERENCE.PAYMENT_MODE);

        // setting tax amount, and grand total amount
        double subTotalIntegerValue = Integer.parseInt(subTotal.substring(subTotal.indexOf("$") + 2));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double taxAmount = Double.parseDouble(decimalFormat.format((subTotalIntegerValue *10)/100));
        double totalAmount = subTotalIntegerValue + taxAmount;
        mSubTotalTv.setText(subTotal);
        mTaxTv.setText("$" + taxAmount);
        mTotalTv.setText("$" + totalAmount);
        mCustomerAddressTv.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(address));

        // setting payment mode image view
        // if it is cash it will show cash image
        // else it will show credit card image
        if (paymentMode.equalsIgnoreCase("Cash")){
            Glide.with(this).load(R.drawable.cash_payment)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mDeliveryModeIv);
        }else{
            Glide.with(this).load(R.drawable.debit_card)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mDeliveryModeIv);
        }

        // this method returns true if we want to stop the seekbar dragging.
        mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    // onClick methods
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.help_center_tv){
            ((CommonActivity)mContext).callFragments(GlobalConstants.FRAGMENT.REPORT_AN_ISSUE, GlobalConstants.FRAGMENT.REPORT_AN_ISSUE,0, false,true,null,null,"");
        }
    }
}