package com.rhythm.mypizzaapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardForm;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PaymentCardListBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;
import com.rhythm.mypizzaapp.utils.localdatabase.PaymentCardDatabaseHelper;

import java.util.Calendar;
import java.util.Objects;

/**
 * AddCardFragment to add new cards for payment.
 *
 */
public class AddCardFragment extends Fragment implements View.OnClickListener, AlertDialogInterface {

    private Context mContext;
    private CardForm mCardForm;
    private Dialog mDialog;
    private TextView mSaveCardTv;

    // constructor
    public AddCardFragment() {
        // Required empty public constructor
    }

    // fragment lifecycle method
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_card, container, false);
    }

    // fragment lifecycle method
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // fragment lifecycle method
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    // fragment lifecycle method
    @Override
    public void onResume() {
        super.onResume();
        if (mContext instanceof CommonActivity){
            // setting toolbar title
            ((CommonActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.ADD_CARD_FRAGMENT);
        }
    }

    // fragment lifecycle method
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initializing views
        initialize(view);
    }

    // initializing views
    private void initialize(View view) {

        mCardForm = view.findViewById(R.id.add_card_form);
        mSaveCardTv = view.findViewById(R.id.save_card_layout).findViewById(R.id.common_btn);

        mSaveCardTv.setText(mContext.getResources().getString(R.string.add_card_txt));

        /* set card form */
        setCardForm();

        /* set click listeners*/
        mSaveCardTv.setOnClickListener(this);
    }

    /* set card form */
    private void setCardForm() {

        if (mContext instanceof CommonActivity) {
            mCardForm.cardRequired(true)
                    .expirationRequired(true)
                    .cvvRequired(true)
                    .postalCodeRequired(false) // if you want to set postalCode, set it to True
                    .mobileNumberRequired(false) // if you want mobile number, set it to True
                    .cardholderName(CardForm.FIELD_REQUIRED)
                    .actionLabel("Add CardDetails")
                    .setup((CommonActivity) mContext);
        }

        /*
         * setting CardForm editText dynamically
         * */
        mCardForm.getCardholderNameEditText().setTextSize(14);
        mCardForm.getCardEditText().setTextSize(14);
        mCardForm.getExpirationDateEditText().setTextSize(14);
        mCardForm.getCvvEditText().setTextSize(14);
        mCardForm.getCvvEditText().setPadding(4, 0, 0, 0);
        mCardForm.getCardholderNameEditText().setPadding(4, 0, 0, 0);
        mCardForm.getCardEditText().setPadding(4, 0, 0, 8);
        mCardForm.getExpirationDateEditText().setPadding(4, 0, 0, 0);
        mCardForm.getCardEditText().setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));
        mCardForm.getCardholderNameEditText().setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));
        mCardForm.getCvvEditText().setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));
        mCardForm.getExpirationDateEditText().setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.common_btn){
            if (!mCardForm.getCardholderName().isEmpty() && !mCardForm.getCardNumber().isEmpty()
            && !mCardForm.getCvv().isEmpty() && !(mCardForm.getExpirationDateEditText().getText()).toString().isEmpty()) {
                // here storing card details in sharedPreferences
                PaymentCardDatabaseHelper paymentCardDatabaseHelper = new PaymentCardDatabaseHelper(mContext);
                PaymentCardListBeanClass paymentCardListBeanClass = new PaymentCardListBeanClass();

                // here checking cardNumber if it is already present in the Database or not
                // if present it will show AlertDialog message that card already exists and
                // if not it will proceed further.
                if (!paymentCardDatabaseHelper.checkCard(mCardForm.getCardNumber())){
                    paymentCardListBeanClass.setCardNumber(mCardForm.getCardNumber()); // setting card number in beanClass
                    paymentCardListBeanClass.setCardHolderName(mCardForm.getCardholderName()); // setting card holder name in beanClass
                    paymentCardListBeanClass.setCardCvv(mCardForm.getCvv()); // setting card cvv number in beanClass
                    // setting card expiry date and year in beanClass
                    paymentCardListBeanClass.setCardExpiryDate(mCardForm.getExpirationMonth() + mCardForm.getExpirationYear());

                    // adding card into the dataBase
                    paymentCardDatabaseHelper.addCard(paymentCardListBeanClass);

                    // calling intent
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.PAYMENT);
                    intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.PAYMENT);
                    startActivity(intent);
                    ((CommonActivity)mContext).finishAffinity(); // finishing commonActivity

                }else{
                    mDialog = new AlertDialogClass().showAlertDialog(mContext, "ERROR!", "Card already exist.", this, false, "OK", "", 0);
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }
            }else{
                mDialog = new AlertDialogClass().showAlertDialog(mContext,"ERROR!", "Please enter valid card details.", this,false,"OK","",0);
                if (mDialog != null){
                    mDialog.show();
                }
            }
        }
    }

    // Alert Dialog interface methods
    @Override
    public void onDialogConfirmAction(int position) {
        mDialog.dismiss();
    }

    @Override
    public void onDialogCancelAction(int position) {

    }
}
