package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.adapters.OrderAdapter;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.RecyclerViewClickListener;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * CartFragment that show list of pizzas to order
 * and show subTotal value of the whole list
 * */

public class MyCartFragment extends Fragment implements View.OnClickListener, RecyclerViewClickListener, AlertDialogInterface {

    private Context mContext;
    private RecyclerView mMyCartRv;
    private Button mSubmitOrderBtn;
    private View emptyListIncludeLayout;
    private OrderAdapter myOrdersAdapter;
    private ImageView emptyBagIv;
    private TextView emptyOrderTv, mSubTotalTv;
    private Dialog mDialog;
    private AlertDialogClass mAlertDialog;
    private ArrayList<OrderHistoryBeanClass> mCompleteOrderList;
    private RelativeLayout subTotalLayout;
    private SharedPreferences sharedPreferences;


    public MyCartFragment() {
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
        return inflater.inflate(R.layout.fragment_my_cart, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CommonActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.MY_CART_FRAGMENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    /*
     * Initializing values
     * */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initialize(View view) {

        emptyListIncludeLayout = view.findViewById(R.id.empty_list_layout);
        emptyBagIv = emptyListIncludeLayout.findViewById(R.id.empty_bag_iv);
        emptyOrderTv = emptyListIncludeLayout.findViewById(R.id.empty_cart_tv);
        subTotalLayout = view.findViewById(R.id.relativeLayout);
        mSubTotalTv = view.findViewById(R.id.subtotal_tv);
        mCompleteOrderList = new ArrayList<>();
        // adding GIF image through Glide dependency
        Glide.with(this).load(R.drawable.empty_bag_gif)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(emptyBagIv);
        mAlertDialog = new AlertDialogClass();
        mMyCartRv = view.findViewById(R.id.my_cart_rv);
        mSubmitOrderBtn = view.findViewById(R.id.submit_order_btn);
        mSubmitOrderBtn.setOnClickListener(this);

        // Adding order list data
//        mCompleteOrderList.add(new OrderHistoryBeanClass(getString(R.string.veg_peppy_paneer),getString(R.string.veg_peppy_paneer_details), R.drawable.veg_peppy_paneer,R.drawable.veg_label, "12", "Regular | Hand tossed", false));
//        mCompleteOrderList.add(new OrderHistoryBeanClass(getString(R.string.chicken_pepper_barbecue),getString(R.string.chicken_pepper_barbecue_details), R.drawable.chicken_pepper_barbeque,R.drawable.non_veg_label, "21", "Regular | Cheese Burst | Extra Cheese",false));

        // getting order list from sharedPreference
        sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonText = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.COMPLETE_ORDER_DETAIL, null);
        Type type = new TypeToken<ArrayList<OrderHistoryBeanClass>>() {}.getType();
        if (mCompleteOrderList != null && jsonText != null) {
            mCompleteOrderList.addAll(gson.fromJson(jsonText, type));  //EDIT: gso to gson
        }
        // updating list
        updateList(mCompleteOrderList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit_order_btn:
                ((CommonActivity)mContext).callFragments(GlobalConstants.FRAGMENT.CONFIRM_ORDER_FRAGMENT, GlobalConstants.FRAGMENT.CONFIRM_ORDER_FRAGMENT, 0,false, true,mCompleteOrderList, null, mSubTotalTv.getText().toString());
                break;
        }
    }

    // recycler view item click listener interface methods
    @Override
    public void itemClickListener(View view, int position) {

        mDialog = mAlertDialog.showAlertDialog(mContext, "Order Cancel Confirmation","Are you sure you want to cancel your order?",this, false, "Yes", "No", position);
        if (mDialog != null){
            mDialog.show();
        }
    }

    @Override
    public void clickListenerWithStringAndPosition(int position, String string) {
        mSubTotalTv.setText("Subtotal $" + position); // here position is used as subTotal value.
    }

    // updating the orderList
    private void updateList(ArrayList<OrderHistoryBeanClass> myOrdersList) {
        if (myOrdersList != null && myOrdersList.size() > 0){
            mMyCartRv.setVisibility(View.VISIBLE);
            subTotalLayout.setVisibility(View.VISIBLE);
            emptyListIncludeLayout.setVisibility(View.INVISIBLE);
            myOrdersAdapter = new OrderAdapter(mContext, myOrdersList, this, GlobalConstants.FRAGMENT.MY_CART_FRAGMENT);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,true);
            linearLayoutManager.setStackFromEnd(true);
            mMyCartRv.setLayoutManager(linearLayoutManager);
            mMyCartRv.setAdapter(myOrdersAdapter);
        }else{
            // if list is empty it will show the empty data layout.
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(GlobalConstants.SHARED_PREFERENCE.COMPLETE_ORDER_DETAIL).apply();

            mMyCartRv.setVisibility(View.INVISIBLE);
            subTotalLayout.setVisibility(View.INVISIBLE);
            emptyListIncludeLayout.setVisibility(View.VISIBLE);
            emptyOrderTv.setText("Your Cart is Empty");
        }
    }

    // alert dialog interface methods
    @Override
    public void onDialogConfirmAction(int position) {
        mCompleteOrderList.remove(position);
        myOrdersAdapter.notifyItemRemoved(position);
        myOrdersAdapter.notifyItemChanged(position, mCompleteOrderList.size());

        // here we are updating the list in sharedPreferences
        // every time user clicks on the cancel order text
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String putJsonText = gson.toJson(mCompleteOrderList);
        prefsEditor.putString(GlobalConstants.SHARED_PREFERENCE.COMPLETE_ORDER_DETAIL, putJsonText);
        prefsEditor.apply();
        updateList(mCompleteOrderList);
    }

    @Override
    public void onDialogCancelAction(int position) {
        if (mDialog != null){
            mDialog.dismiss();
        }
    }
}