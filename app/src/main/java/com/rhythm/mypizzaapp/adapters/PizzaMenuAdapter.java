package com.rhythm.mypizzaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PizzaMenuBeanClass;
import com.rhythm.mypizzaapp.utils.RecyclerViewClickListener;

import java.util.ArrayList;

/**
 * PizzaMenu adapter class to show the list of pizzas, in the PizzaMenu fragment
 * */

public class PizzaMenuAdapter extends RecyclerView.Adapter<PizzaMenuAdapter.ViewHolder>  {

    private Context mContext;
    private ArrayList<PizzaMenuBeanClass> mPizzaList;
    private RecyclerViewClickListener mRecyclerViewClickListener;

    public PizzaMenuAdapter(Context mContext, ArrayList<PizzaMenuBeanClass> pizzaList, RecyclerViewClickListener recyclerViewClickListener) {
        this.mContext = mContext;
        mPizzaList = pizzaList;
        mRecyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pizza_menu_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    // binding view This method internally calls onBindViewHolder(ViewHolder, int) to update the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.mPizzaNameIv.setImageResource(Integer.parseInt(mPizzaList.get(position).getPizzaIv()));
        viewHolder.mPizzaLabelIv.setImageResource(Integer.parseInt(mPizzaList.get(position).getPizzaLabelTagIv()));
        viewHolder.mPizzaNameTv.setText(mPizzaList.get(position).getPizzaName());
        viewHolder.mPizzaDetailTv.setText(mPizzaList.get(position).getPizzaDetails());

        viewHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerViewClickListener.itemClickListener(view,viewHolder.getAdapterPosition());
           }
        });

    }

    // total number of items in the list
    @Override
    public int getItemCount() {
        return mPizzaList.size();
    }

    // ViewHolder class that holds the layout views
    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPizzaNameIv, mPizzaLabelIv;
        private TextView mPizzaNameTv, mPizzaDetailTv;
        private ConstraintLayout mParentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPizzaNameIv = itemView.findViewById(R.id.pizza_iv); // Pizza name ImageView
            mPizzaLabelIv = itemView.findViewById(R.id.veg_non_veg_label_tag_iv); // pizza label(Veg or non-veg) Iv
            mPizzaNameTv = itemView.findViewById(R.id.pizza_name_tv); // pizza name textView
            mPizzaDetailTv = itemView.findViewById(R.id.pizza_description_tv); // pizza description
            mParentLayout = itemView.findViewById(R.id.pizza_menu_parent_layout); // parent layout for clicking
        }
    }
}
