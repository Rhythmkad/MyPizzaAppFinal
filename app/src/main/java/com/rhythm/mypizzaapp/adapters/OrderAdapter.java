package com.rhythm.mypizzaapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.utils.RecyclerViewClickListener;
import com.rhythm.mypizzaapp.utils.TypefaceUtil;

import java.util.ArrayList;

/** A common Adapter for OrderHistory, MyCart and MyOrder fragment
 *
 * */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements View.OnClickListener  {

    private final Context mContext;
    private final ArrayList<OrderHistoryBeanClass> mOrderList;
    private RecyclerViewClickListener mRecyclerClickListener;
    private int mSubtotalAmount = 0;
    private String calling_fragment_name;

    // constructor
    public OrderAdapter(Context mContext, ArrayList<OrderHistoryBeanClass> pizzaList, RecyclerViewClickListener recyclerViewClickListener, String calling_fragment_name) {
        this.mContext = mContext;
        mOrderList = pizzaList;
        this.calling_fragment_name = calling_fragment_name;
        mRecyclerClickListener = recyclerViewClickListener;
    }

    // creating view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_list_layout, parent, false);
        return new ViewHolder(view);
    }

    // binding view This method internally calls onBindViewHolder(ViewHolder, int) to update the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (mOrderList != null) {
            viewHolder.mPizzaNameIv.setImageResource(Integer.parseInt(mOrderList.get(position).getPizzaIv()));
            viewHolder.mPizzaLabelIv.setImageResource(Integer.parseInt(mOrderList.get(position).getPizzaLabelTagIv()));
            viewHolder.mPizzaNameTv.setText(mOrderList.get(position).getPizzaName());
            viewHolder.mPizzaDetailTv.setText(mOrderList.get(position).getPizzaDetails());
            viewHolder.mSizeAndCrustTv.setText(mOrderList.get(position).getPizzaCustomizeItems());
            String pizzaQuantity = "Quantity: " + mOrderList.get(position).getPizzaQuantity();
            viewHolder.mPizzaQuantityTv.setText(pizzaQuantity);
            viewHolder.mDateTv.setText(mOrderList.get(position).getOrderDate());

            viewHolder.mDeleteOrderTv.setTag(viewHolder.getAdapterPosition());
            viewHolder.mDeleteOrderTv.setOnClickListener(this);

            // calling method to set fragments data
            setDataAccordingToFragment(viewHolder, position);
        }

    }

    // here we are setting some data according to the fragment
    // from where this adapter class is called (e.g : MyCart, OrderHistory, MyOrder
    private void setDataAccordingToFragment(ViewHolder viewHolder, int position) {
        switch (calling_fragment_name){
            case GlobalConstants.FRAGMENT.MY_CART_FRAGMENT:
                // if adapter is called from MyCart fragment
                // then we set some data according to that
                viewHolder.mOrderCompleteTv.setVisibility(View.INVISIBLE);
                viewHolder.mDeleteOrderTv.setText(R.string.delete_order);
                viewHolder.mPizzaPriceTv.setText(mOrderList.get(position).getPizzaPriceTotal());

                for (OrderHistoryBeanClass i : mOrderList){
                String pizzaAmount = i.getPizzaPriceTotal();
                int indexOfAmount = pizzaAmount.indexOf(" ");
                int totalAmount = Integer.parseInt(pizzaAmount.substring(indexOfAmount + 1));
                mSubtotalAmount += totalAmount;
            }
                mRecyclerClickListener.clickListenerWithStringAndPosition(mSubtotalAmount/mOrderList.size(), "");

                break;
            case GlobalConstants.FRAGMENT.ORDER_HISTORY:

                // if adapter is called from OrderHistory fragment
                // then we set some data according to that
                viewHolder.mParentLayout.setTag(viewHolder.getAdapterPosition());
                viewHolder.mParentLayout.setOnClickListener(this);
                viewHolder.mOrderCompleteTv.setVisibility(View.VISIBLE);
                viewHolder.mOrderCompleteTv.setText(R.string.feedback_txt);
                viewHolder.mOrderCompleteTv.setTextColor(mContext.getApplicationContext().getColor(R.color.text_color));
                viewHolder.mOrderCompleteTv.setTypeface(TypefaceUtil.getInstance().getRegularTypeFace(mContext), Typeface.BOLD);
                viewHolder.mDeleteOrderTv.setText(R.string.delete_order);
                viewHolder.mPizzaPriceTv.setText(mOrderList.get(position).getPizzaPriceTotal());

                viewHolder.mOrderCompleteTv.setTag(viewHolder.getAdapterPosition());
                viewHolder.mOrderCompleteTv.setOnClickListener(this);
                break;
            case GlobalConstants.FRAGMENT.MY_ORDERS:
                // if adapter is called from MyOrders fragment
                // then we set some data according to that
                viewHolder.mParentLayout.setTag(viewHolder.getAdapterPosition());
                viewHolder.mParentLayout.setOnClickListener(this);
                viewHolder.mOrderCompleteTv.setVisibility(View.VISIBLE);
                viewHolder.mDeleteOrderTv.setVisibility(View.INVISIBLE);
                viewHolder.mOrderCompleteTv.setText(R.string.order_on_its_way_txt);
                viewHolder.mPizzaPriceTv.setText(mOrderList.get(position).getPizzaPriceTotal());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_order_tv:
                // here we are sending the position of the click,
                // to cancel or delete the particular order
                int position = (int) view.getTag();
                mRecyclerClickListener.itemClickListener(view,position);
                break;
            case R.id.cart_list_parent_layout:
                // here we are sending only the position of the click,
                // as here on click we are sending the request to open OrderStatusFragment.
                position = (int) view.getTag();
                mRecyclerClickListener.clickListenerWithStringAndPosition(position, GlobalConstants.FRAGMENT.PLACE_ORDER_FRAGMENT);
                break;
            case R.id.order_complete_tv:
                // here we are sending only the position of the click,
                // as here on click we are sending the request to open FeedBackFragment.
                position = (int)view.getTag();
                mRecyclerClickListener.clickListenerWithStringAndPosition(position, GlobalConstants.FRAGMENT.FEEDBACK_FRAGMENT);
                break;
        }
    }

    // ViewHolder class that extends RecyclerViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mPizzaNameIv, mPizzaLabelIv;
        private final TextView mPizzaNameTv, mPizzaDetailTv, mSizeAndCrustTv, mOrderCompleteTv, mDeleteOrderTv,
                mPizzaPriceTv, mPizzaQuantityTv, mDateTv;
        private final ConstraintLayout mParentLayout;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mParentLayout = itemView.findViewById(R.id.cart_list_parent_layout); // parent layout
            mPizzaNameIv = itemView.findViewById(R.id.pizza_order_iv); // pizza Image view
            mPizzaLabelIv = itemView.findViewById(R.id.pizza_veg_non_veg_label_iv); // pizza label (veg or non-veg) image view
            mPizzaNameTv = itemView.findViewById(R.id.pizza_order_name_tv); // pizza name text view
            mPizzaDetailTv = itemView.findViewById(R.id.pizza_order_detail_tv); // pizza description text view
            mSizeAndCrustTv = itemView.findViewById(R.id.crust_size_order_tv); // pizza size and crust
            mOrderCompleteTv = itemView.findViewById(R.id.order_complete_tv); // order complete textview that show feedback text, order status text
            mDeleteOrderTv = itemView.findViewById(R.id.delete_order_tv); // delete order text view
            mPizzaPriceTv = itemView.findViewById(R.id.price_tv_cart); // pizza price text view
            mPizzaQuantityTv = itemView.findViewById(R.id.quantity_tv); // pizza quantity text view
            mDateTv = itemView.findViewById(R.id.date_tv); // order date text view
        }
    }
}
