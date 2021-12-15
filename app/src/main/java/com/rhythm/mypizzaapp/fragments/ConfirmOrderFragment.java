package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PaymentCardListBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;
import com.rhythm.mypizzaapp.utils.ValidationsClass;
import com.rhythm.mypizzaapp.utils.localdatabase.PaymentCardDatabaseHelper;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * ConfirmOrder fragment is called when, user place an order from the cart
 * the very next screen is ConfirmOrder screen, where
 * user will see the Amount of the pizza, select payment mode and delivery address.
 */
public class ConfirmOrderFragment extends Fragment implements View.OnClickListener, AlertDialogInterface, RadioGroup.OnCheckedChangeListener {

    private TextView mSubTotalTv, mTaxTv, mTotalTv, mProceedBtnTv, mAddCardTv;
    private EditText mAddressEdt, mCvvEdt;
    private Context mContext;
    private RadioGroup mPaymentModeRadioGrp;
    private RadioButton mPaymentRadioBtn;
    private ArrayList<OrderHistoryBeanClass> mCompleteOrderList;
    private Spinner mCardNumberSpinner;
    private String[] cardNumber, cardCvv;
    private RelativeLayout mCardPaymentLayout;
    private String mSubTotalAmount, mPaymentModeString, mPaymentValue;
    private Dialog mDialog;

    public ConfirmOrderFragment() {
        // Required empty public constructor
    }

    // here in this constructor we are getting the complete order list and
    // sub total value of the order list
    public ConfirmOrderFragment(ArrayList<OrderHistoryBeanClass> completeOrderList, String subTotalAmount) {
        mCompleteOrderList = completeOrderList;
        mSubTotalAmount = subTotalAmount;
    }

    // fragment lifecycle methods
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        // setting title
        ((CommonActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.CONFIRM_ORDER_FRAGMENT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* initializing values */
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

    /* initializing values */
    private void initialize(View view) {

        View proceedBtnIncludeLayout = view.findViewById(R.id.proceed_btn);

        mSubTotalTv = view.findViewById(R.id.payment_sub_total_value_tv);  // sub total text view
        mTaxTv = view.findViewById(R.id.payment_tax_value_tv);  // tax text view
        mTotalTv = view.findViewById(R.id.payment_total_value_tv); // total text view
        mAddressEdt = view.findViewById(R.id.customer_address_edt); // total text view
        mAddCardTv = view.findViewById(R.id.add_card_tv); // total text view
        mPaymentModeRadioGrp = view.findViewById(R.id.payment_mode_radio_grp); // payment mode radio group
        mCvvEdt = view.findViewById(R.id.cvv_edit_text); // cvv edit text
        mCardNumberSpinner = view.findViewById(R.id.card_spinner); // card spinner
        mCardPaymentLayout = view.findViewById(R.id.payment_layout); // card payment layout
        mProceedBtnTv = proceedBtnIncludeLayout.findViewById(R.id.common_btn); // proceed btn

        /*
        * run time changes
        * */
        mProceedBtnTv.setText(mContext.getResources().getString(R.string.confirm_order));
        mProceedBtnTv.setOnClickListener(this);
        mPaymentModeRadioGrp.setOnCheckedChangeListener(this);
        mAddCardTv.setOnClickListener(this);

        // set total amount
        setTotalAmount();
    }

    // setting total amount after calculating the tax amount
    private void setTotalAmount() {
        mSubTotalTv.setText(mSubTotalAmount.substring(mSubTotalAmount.indexOf(" ")+1));
        String subTotalString = mSubTotalTv.getText().toString();
        double subTotalIntegerValue = Integer.parseInt(subTotalString.substring(subTotalString.indexOf("$") + 1));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double taxAmount = Double.parseDouble(decimalFormat.format((subTotalIntegerValue *10)/100)); // 10% for tax
        double totalAmount = subTotalIntegerValue + taxAmount;
        mTaxTv.setText("$" + taxAmount);
        mTotalTv.setText("$" + totalAmount);
    }

    /*
    * OnClick method
    * */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.common_btn:
                confirmOrder();
                break;
            case R.id.add_card_tv:
                mDialog = new AlertDialogClass().showAlertDialog(mContext,"Add card","Add card and come back to place your order.", this,false,"OK","",1);
                if (mDialog != null){
                    mDialog.show();
                }
              break;
        }
    }

    // method to confirm order, when user click on the place order button
    private void confirmOrder() {
        // checking if payment mode radio group item is selected or not
        if (mPaymentRadioBtn != null) {
            // checking address empty or not
            if (new ValidationsClass().checkStringNull(mAddressEdt.getText().toString())) {
                // checking payment mode and calling showing or hiding view of
                // list card according to it
                if (mPaymentModeString.equals("By Card")) {
                    for (int i = 0; i < cardNumber.length; i++) {
                        if (mPaymentValue.equals(cardNumber[i])) {
                            // checking CVV of the selected card
                            if (mCvvEdt.getText().toString().equals(cardCvv[i])) {
                                callIntent();
                            }else{
                                mDialog = new AlertDialogClass().showAlertDialog(mContext, "Order Confirmation Failed",
                                        "Entered CVV is not valid.", this,
                                        false, "OK", "", 0);
                                if (mDialog != null) {
                                    mDialog.show();
                                }
                            }
                        }
                    }
                }else if (mPaymentModeString.equals("Cash")){
                    callIntent();
                }
            } else {
                mDialog = new AlertDialogClass().showAlertDialog(mContext, "Order Confirmation Failed",
                        "Please enter delivery Address.", this,
                        false, "OK", "", 0);
                if (mDialog != null) {
                    mDialog.show();
                }
            }
        }else{
            mDialog = new AlertDialogClass().showAlertDialog(mContext, "Order Confirmation Failed",
                    "Please select delivery mode.", this,
                    false, "OK", "", 0);
            if (mDialog != null) {
                mDialog.show();
            }
        }
    }

    // calling intent to start new activity
    private void callIntent() {
        // setting shared preference
        setSharedPreference();

        // calling intent
        Intent intent = new Intent(mContext, CommonActivity.class);
        intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.ORDER_PLACED_SUCCESS);
        intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.ORDER_PLACED_SUCCESS);

        // getting sharedPreference
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);

        // here getting the saved list if any
        // if saved list is not empty
        // then we are updating the list
        // by adding the new order
        // and then again saving it in the shared preferences.
        Gson gson = new Gson();
        String jsonText = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.MY_ORDER_FRAGMENT_DETAIL, null);
        Type type = new TypeToken<ArrayList<OrderHistoryBeanClass>>() {}.getType();
        if (mCompleteOrderList != null && jsonText != null) {
            mCompleteOrderList.addAll(gson.fromJson(jsonText, type));  //EDIT: gso to gson
        }

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        String putJsonText = gson.toJson(mCompleteOrderList);
        prefsEditor.putString(GlobalConstants.SHARED_PREFERENCE.MY_ORDER_FRAGMENT_DETAIL, putJsonText);
        prefsEditor.apply();

        startActivity(intent);

        // here removing the saved preferences of the MyCart
        // because after placing the order, we are emptying the cart/
        sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(GlobalConstants.SHARED_PREFERENCE.COMPLETE_ORDER_DETAIL).apply();
        Toast.makeText(mContext, "Order placed successfully", Toast.LENGTH_SHORT).show();

        ((CommonActivity) mContext).finish();
    }

    // setting sharedPreference for delivery address and payment mode
    private void setSharedPreference() {
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(mContext);
        sharedPreferenceClass.setSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,GlobalConstants.SHARED_PREFERENCE.ADDRESS_FOR_DELIVERY, mAddressEdt.getText().toString());
        sharedPreferenceClass.setSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,GlobalConstants.SHARED_PREFERENCE.PAYMENT_MODE, mPaymentModeString);
    }

    // Alert Dialog interface methods
    // here if position is 1, that means
    // our payment mode is by card
    // and there are no cards in the list, so we need to add a card first than place an order again.
    @Override
    public void onDialogConfirmAction(int position) {
        if (position == 1){
            Intent intent = new Intent((CommonActivity) mContext, MainActivity.class);
            intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.PAYMENT);
            intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.PAYMENT);
            startActivity(intent);
            ((CommonActivity)mContext).finish();
        }
        mDialog.dismiss();
    }

    @Override
    public void onDialogCancelAction(int position) {

    }

    // radio group check changed methods
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (radioGroup.getId()){
            case R.id.payment_mode_radio_grp:
                mPaymentRadioBtn = radioGroup.findViewById(id);
                mPaymentModeString = mPaymentRadioBtn.getText().toString();
                break;
        }

        showAndHideView(mPaymentModeString);
    }

    // here if user has selected By Card payment mode
    // then cards list and CVV edit text layout will be visible
    // otherwise it will set to INVISIBLE/GONE
    private void showAndHideView(String paymentModeString) {
        switch (paymentModeString){
            case "By Card":
                mPaymentModeString = "By Card";
                //setting adapter and applying OnItemSelectedListener on it
                //Creating the ArrayAdapter instance having the score list
                ArrayList<PaymentCardListBeanClass> cardList;

                // getting all the saved cards list from the SQL database.
                cardList = new PaymentCardDatabaseHelper(mContext).getAllCards();

                if (!cardList.isEmpty()) {
                    mCardPaymentLayout.setVisibility(View.VISIBLE);
                    mAddCardTv.setVisibility(View.INVISIBLE);
                    cardNumber = new String[cardList.size()];
                    cardCvv = new String[cardList.size()];
                    for (int data = 0; data < cardList.size();data++){
                        // getting list of card numbers and CVV
                        cardNumber[data] = String.valueOf(new StringFormatterClass().getCardTransformation(cardList.get(data).getCardNumber()));
                        cardCvv[data] = String.valueOf(new StringFormatterClass().getCardTransformation(cardList.get(data).getCardCvv()));
                    }

                    // setting spinner adapter
                    ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, cardNumber);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    mCardNumberSpinner.setAdapter(adapter);
                    mCardNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            mPaymentValue = adapterView.getItemAtPosition(position).toString();
                            for (int i = 0; i < cardNumber.length;i++){
                                if (mPaymentValue.equals(cardNumber[i])){
                                    // setting Max length of editText according to the CVV
                                    mCvvEdt.setFilters(new InputFilter[] {new InputFilter.LengthFilter(cardCvv[i].length())});
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }else{
                    mCardPaymentLayout.setVisibility(View.GONE);
                    mAddCardTv.setVisibility(View.VISIBLE);
                }
                break;
            case "Cash on delivery":
                mCardPaymentLayout.setVisibility(View.GONE);
                mAddCardTv.setVisibility(View.INVISIBLE);
                mPaymentModeString = "Cash";
                mPaymentValue = paymentModeString;
                break;
        }
    }
}
//thanks for placing a delivery order with Tony’s Pizzeria!
// Your pie should be home with you in around 30-45 minutes. Text STOP to stop receiving texts.