package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.adapters.OrderAdapter;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PizzaMenuBeanClass;
import com.rhythm.mypizzaapp.utils.RecyclerViewClickListener;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;

import java.util.ArrayList;

/**
 * Order history fragment to show order history of pizzas ordered by the user
 * */

public class OrderHistoryFragment extends Fragment implements RecyclerViewClickListener {

    private Context mContext;
    private RecyclerView mOrderHistoryRv;
    private ArrayList<OrderHistoryBeanClass> mOrderHistoryList;
    private OrderAdapter orderHistoryAdapter;
    private View emptyListIncludeLayout;
    private ImageView emptyBagIv;
    private TextView emptyOrderTv;

    public OrderHistoryFragment() {
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
        return inflater.inflate(R.layout.fragment_order_history, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        // setting toolbar title
        ((CommonActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.ORDER_HISTORY);
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
        mOrderHistoryRv = view.findViewById(R.id.order_history_rv);

        emptyListIncludeLayout = view.findViewById(R.id.empty_order_history_layout);
        emptyBagIv = emptyListIncludeLayout.findViewById(R.id.empty_bag_iv);
        emptyOrderTv = emptyListIncludeLayout.findViewById(R.id.empty_cart_tv);
        mOrderHistoryList = new ArrayList<>();
        // Adding order history list data
        mOrderHistoryList.add(new OrderHistoryBeanClass(getString(R.string.veg_peppy_paneer),getString(R.string.veg_peppy_paneer_details), String.valueOf(R.drawable.veg_peppy_paneer),String.valueOf(R.drawable.veg_label), "$ 12", "Regular | Hand tossed", "1","November 25, 2021 15:30:40"));
        mOrderHistoryList.add(new OrderHistoryBeanClass(getString(R.string.chicken_pepper_barbecue),getString(R.string.chicken_pepper_barbecue_details), String.valueOf(R.drawable.chicken_pepper_barbeque),String.valueOf(R.drawable.non_veg_label), "$ 12", "Regular | Hand tossed","1","November 25, 2021 15:30:40"));
        mOrderHistoryList.add(new OrderHistoryBeanClass(getString(R.string.non_veg_supreme),getString(R.string.non_veg_supreme_details), String.valueOf(R.drawable.non_veg_supreme),String.valueOf(R.drawable.non_veg_label),"$ 12", "Regular | Hand tossed", "1","December 04, 2021 10:10:30"));
        mOrderHistoryList.add(new OrderHistoryBeanClass(getString(R.string.new_fresh_veggie),getString(R.string.new_fresh_veggie_details), String.valueOf(R.drawable.new_fresh_veggie),String.valueOf(R.drawable.veg_label), "$ 12", "Regular | Hand tossed", "1","December 08, 2021 03:10:25"));

        // updating list
        updateList(mOrderHistoryList);
    }

    // recycler view click listener interface
    @Override
    public void itemClickListener(View view, int position) {
        mOrderHistoryList.remove(position);
        orderHistoryAdapter.notifyItemRemoved(position);
        orderHistoryAdapter.notifyItemChanged(position, mOrderHistoryList.size());
        updateList(mOrderHistoryList);
    }

    @Override
    public void clickListenerWithStringAndPosition(int position, String string) {
        switch (string){
            case GlobalConstants.FRAGMENT.FEEDBACK_FRAGMENT:
                ((CommonActivity)mContext).callFragments(GlobalConstants.FRAGMENT.FEEDBACK_FRAGMENT, GlobalConstants.FRAGMENT.FEEDBACK_FRAGMENT,0, false,true,null, null,"");
                break;
            case GlobalConstants.FRAGMENT.PLACE_ORDER_FRAGMENT:
                ArrayList<PizzaMenuBeanClass> modifyOrderList = new ArrayList<>();
                String pizzaName, pizzaDescription, pizzaPrice, pizzaImage, pizzaLabelTag;
                pizzaName = mOrderHistoryList.get(position).getPizzaName();
                pizzaDescription = mOrderHistoryList.get(position).getPizzaDetails();
                pizzaPrice = mOrderHistoryList.get(position).getPizzaPriceTotal();
                pizzaImage = mOrderHistoryList.get(position).getPizzaIv();
                pizzaLabelTag = mOrderHistoryList.get(position).getPizzaLabelTagIv();

                modifyOrderList.add(new PizzaMenuBeanClass(pizzaName,pizzaDescription,pizzaImage,pizzaLabelTag,pizzaPrice));
                ((CommonActivity)mContext).callFragments(GlobalConstants.FRAGMENT.PLACE_ORDER_FRAGMENT, GlobalConstants.FRAGMENT.PLACE_ORDER_FRAGMENT,0, false,true,null,modifyOrderList,"");
                break;
        }
    }

    // updating order history list
    private void updateList(ArrayList<OrderHistoryBeanClass> mOrderHistoryList) {
        if (!mOrderHistoryList.isEmpty()){
            mOrderHistoryRv.setVisibility(View.VISIBLE);
            emptyListIncludeLayout.setVisibility(View.INVISIBLE);
            orderHistoryAdapter = new OrderAdapter(mContext, mOrderHistoryList, this, GlobalConstants.FRAGMENT.ORDER_HISTORY);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,true);
            linearLayoutManager.setStackFromEnd(true);
            mOrderHistoryRv.setLayoutManager(linearLayoutManager);
            mOrderHistoryRv.setAdapter(orderHistoryAdapter);
        }else{
            mOrderHistoryRv.setVisibility(View.INVISIBLE);
            emptyListIncludeLayout.setVisibility(View.VISIBLE);
            emptyBagIv.setImageResource(R.drawable.pizza_box_stack);
            emptyOrderTv.setText("No past orders to show");
            emptyOrderTv.setTextSize(20);
        }
    }
}