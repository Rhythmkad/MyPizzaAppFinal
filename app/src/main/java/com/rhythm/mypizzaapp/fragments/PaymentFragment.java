package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.adapters.PaymentAdapter;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PaymentCardListBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.RecyclerViewClickListener;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;
import com.rhythm.mypizzaapp.utils.localdatabase.PaymentCardDatabaseHelper;

import java.util.ArrayList;

/**
 * This fragment show the card list and add a card option
 * */

public class PaymentFragment extends Fragment implements RecyclerViewClickListener, View.OnClickListener, AlertDialogInterface {

    private RecyclerView mPaymentRv;
    private Context mContext;
    private PaymentAdapter mAdapter;
    private String mCardNumber;
    private ImageView emptyCardIv, mDebitCardIv, mAddCardIv;
    private TextView mEmptyCardTxtTv, mAddACardTxtTv, mAddCardTv;
    private ArrayList<PaymentCardListBeanClass> mCardList;
    private View mEmptyCardListLayout;
    private Dialog mDialog;
    private PaymentCardDatabaseHelper mPaymentCardDatabaseHelper;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.PAYMENT);
        }
//        else {
//            ((CommonActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.PAYMENT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * Initializing values
         * */
        initialize(view);
    }

    /*
     * Initializing values
     * */
    private void initialize(View view) {
        mPaymentRv = view.findViewById(R.id.payment_recycler_view);
        mAddCardTv = view.findViewById(R.id.add_new_card_tv);
        mDebitCardIv = view.findViewById(R.id.debitCard_iv);
        mAddCardIv = view.findViewById(R.id.add_card_iv);
        mEmptyCardListLayout = view.findViewById(R.id.empty_card_list_layout);
        emptyCardIv = mEmptyCardListLayout.findViewById(R.id.empty_bag_iv);
        mEmptyCardTxtTv = mEmptyCardListLayout.findViewById(R.id.empty_cart_tv);
        mAddACardTxtTv = mEmptyCardListLayout.findViewById(R.id.add_item_tv);
        // adding GIF image through Glide dependency
        Glide.with(this).load(R.drawable.empty_wallet)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(emptyCardIv);
        Glide.with(this).load(R.drawable.debit_card)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mDebitCardIv);
        Glide.with(this).load(R.drawable.add_card)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mAddCardIv);


        /* setting card list */
        mCardList = new ArrayList<>();

        // initializing database helper and getting all the cards details.
        mPaymentCardDatabaseHelper = new PaymentCardDatabaseHelper(mContext);
        mCardList = mPaymentCardDatabaseHelper.getAllCards();

        /* set click listeners */
        mAddCardTv.setOnClickListener(this);

        // updating list
        updateList(mCardList);
    }

    // here if card list is empty it will show the empty card list layout
    // else it will show the all the cards added by the user.
    @SuppressLint("SetTextI18n")
    private void updateList(ArrayList<PaymentCardListBeanClass> cardList) {
        if (cardList != null && cardList.size() > 0){
            mPaymentRv.setVisibility(View.VISIBLE);
            mEmptyCardListLayout.setVisibility(View.INVISIBLE);

            /* setting recycler view adapter */
            mAdapter = new PaymentAdapter(mContext, this, cardList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,true);
            linearLayoutManager.setStackFromEnd(true);
            mPaymentRv.setLayoutManager(linearLayoutManager);
            mPaymentRv.setAdapter(mAdapter);
        }else{
            mPaymentRv.setVisibility(View.INVISIBLE);
            mEmptyCardListLayout.setVisibility(View.VISIBLE);
            mEmptyCardTxtTv.setText("Wallet is empty.");
            mAddACardTxtTv.setText("Please add a new card by clicking below.");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_new_card_tv) {
            if (mContext instanceof MainActivity) {
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.ADD_CARD_FRAGMENT);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.ADD_CARD_FRAGMENT);
                startActivity(intent);
            }
//            else {
//                ((CommonActivity) mContext).callFragments(GlobalConstants.FRAGMENT.ADD_CARD_FRAGMENT, GlobalConstants.FRAGMENT.ADD_CARD_FRAGMENT, 0, false, true,null, null, "");
//            }
        }
    }

    // recycler view clickListener interface methods
    @Override
    public void itemClickListener(View view, int position) {
    }

    @Override
    public void clickListenerWithStringAndPosition(int position, String string) {

        mCardNumber = string;
        mDialog = new AlertDialogClass().showAlertDialog(mContext, "Delete Card Confirmation","Are you sure you want to delete the card "+ new StringFormatterClass().getCardTransformation(mCardNumber) + " ?",this, false, "Yes", "No", position);
        if (mDialog != null){
            mDialog.show();
        }
    }

    // alert dialog interface methods
    @Override
    public void onDialogConfirmAction(int position) {
        // removing card from the list and updating the list
        mCardList.remove(position);
        mPaymentCardDatabaseHelper.deleteCard(mCardNumber);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemChanged(position, mCardList.size());
        updateList(mCardList);
    }

    @Override
    public void onDialogCancelAction(int position) {
        if (mDialog != null){
            mDialog.dismiss();
        }
    }
}