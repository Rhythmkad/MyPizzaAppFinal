package com.rhythm.mypizzaapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;

/**
 * This fragment is called when order is placed by the user;
 * to show thank you text.
 */
public class OrderPlacedSuccessfullyFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    public OrderPlacedSuccessfullyFragment() {
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
        return inflater.inflate(R.layout.fragment_order_placed_success_fully, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CommonActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.ORDER_PLACED_SUCCESS);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    private void initialize(View view) {
        LinearLayout mCloseScreenLayout = view.findViewById(R.id.close_screen_layout);
        View includeLayout = view.findViewById(R.id.order_placed_layout);
        ImageView mOrderDoneIv = includeLayout.findViewById(R.id.empty_bag_iv);
        TextView mOrderPlaceTv = includeLayout.findViewById(R.id.empty_cart_tv);
        TextView mThankYouTv = includeLayout.findViewById(R.id.add_item_tv);

        Glide.with(this).load(R.drawable.order_placed_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mOrderDoneIv);

        mOrderPlaceTv.setText("Order Placed Successfully");
        mOrderPlaceTv.setTextSize(20);
        mThankYouTv.setText(getString(R.string.thank_you_txt));
        mThankYouTv.setTextSize(14);

        mCloseScreenLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close_screen_layout){
            Intent intent = new Intent((CommonActivity) mContext, MainActivity.class);
            intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MENU);
            intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MENU);
            startActivity(intent);
            ((CommonActivity)mContext).finish();
        }
    }
}