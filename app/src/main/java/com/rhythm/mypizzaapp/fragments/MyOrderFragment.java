package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.adapters.OrderAdapter;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.utils.RecyclerViewClickListener;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This fragment show the current orders, ordered by the user.
 * */

public class MyOrderFragment extends Fragment implements View.OnClickListener, RecyclerViewClickListener {

    private Context mContext;
    private RecyclerView mMyOrdersRv;
    private Button mViewPastOrderBtn;
    private View emptyListIncludeLayout;
    private OrderAdapter myOrdersAdapter;
    private ImageView emptyBagIv;
    private TextView emptyOrderTv;
    private ArrayList<OrderHistoryBeanClass> mCompleteOrderList;

    public MyOrderFragment() {
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
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.MY_ORDERS);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initializing views
        initialize(view);
    }

    /*
     * Initializing values
     * */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initialize(View view) {
        mMyOrdersRv = view.findViewById(R.id.my_order_rv);
        mViewPastOrderBtn = view.findViewById(R.id.past_order_btn);
        mViewPastOrderBtn.setOnClickListener(this);
        mCompleteOrderList = new ArrayList<>();
        emptyListIncludeLayout = view.findViewById(R.id.empty_orders_include_layout);
        emptyBagIv = emptyListIncludeLayout.findViewById(R.id.empty_bag_iv);
        emptyOrderTv = emptyListIncludeLayout.findViewById(R.id.empty_cart_tv);

        // Adding order history list data
//        mMyOrdersList.add(new OrderHistoryBeanClass(getString(R.string.veg_peppy_paneer),getString(R.string.veg_peppy_paneer_details), R.drawable.veg_peppy_paneer,R.drawable.veg_label, "12", "Regular | Hand tossed", false));
//        mMyOrdersList.add(new OrderHistoryBeanClass(getString(R.string.chicken_pepper_barbecue),getString(R.string.chicken_pepper_barbecue_details), R.drawable.chicken_pepper_barbeque,R.drawable.non_veg_label, "21", "Regular | Cheese Burst | Extra Cheese",false));

        // getting list data from shared preferences
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonText = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.MY_ORDER_FRAGMENT_DETAIL, null);
        Type type = new TypeToken<ArrayList<OrderHistoryBeanClass>>() {}.getType();
        if (mCompleteOrderList != null && jsonText != null) {
            mCompleteOrderList.addAll(gson.fromJson(jsonText, type));
        }
        // updating order list
        updateList(mCompleteOrderList);
    }


    // updating myOrder list
    private void updateList(ArrayList<OrderHistoryBeanClass> myOrdersList) {
        if (myOrdersList != null && myOrdersList.size() > 0){
            mMyOrdersRv.setVisibility(View.VISIBLE);
            emptyListIncludeLayout.setVisibility(View.INVISIBLE);
            myOrdersAdapter = new OrderAdapter(mContext, myOrdersList, this, GlobalConstants.FRAGMENT.MY_ORDERS);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false);
            linearLayoutManager.setStackFromEnd(false);
            mMyOrdersRv.setLayoutManager(linearLayoutManager);
            mMyOrdersRv.setAdapter(myOrdersAdapter);
        } else{
            mMyOrdersRv.setVisibility(View.INVISIBLE);
            emptyListIncludeLayout.setVisibility(View.VISIBLE);
            emptyBagIv.setImageResource(R.drawable.pizza_delivery_boy);
            emptyOrderTv.setText("No Orders to deliver");
            emptyOrderTv.setTextSize(20);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.past_order_btn:
                Intent intent = new Intent((MainActivity)mContext, CommonActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.ORDER_HISTORY);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.ORDER_HISTORY);
                startActivity(intent);
                break;
        }
    }

    // recycler view click listener interface methods
    @Override
    public void itemClickListener(View view, int position) {
    }

    @Override
    public void clickListenerWithStringAndPosition(int position, String string) {

        // saving shared preference for pizza price total
        SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass(mContext);
        sharedPreferenceClass.setSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,GlobalConstants.SHARED_PREFERENCE.SUB_TOTAL_AMOUNT_KEY, mCompleteOrderList.get(position).getPizzaPriceTotal());

        // calling intent
        Intent intent = new Intent(((MainActivity)mContext), CommonActivity.class);
        intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.ORDER_STATUS_FRAGMENT);
        intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.ORDER_STATUS_FRAGMENT);
        startActivity(intent);
    }
}