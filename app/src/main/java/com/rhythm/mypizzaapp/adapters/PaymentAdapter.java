package com.rhythm.mypizzaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.utils.CardType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PaymentCardListBeanClass;
import com.rhythm.mypizzaapp.utils.RecyclerViewClickListener;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;

import java.util.ArrayList;

/**
 * This adapter is used to show Payment card list, in PaymentFragment
 * */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<PaymentCardListBeanClass> mPaymentCardList;
    private RecyclerViewClickListener mRecyclerViewClickListeners;

    public PaymentAdapter(Context mContext, RecyclerViewClickListener recyclerViewClickListeners, ArrayList<PaymentCardListBeanClass> cardList) {
        this.mContext = mContext;
        mRecyclerViewClickListeners = recyclerViewClickListeners;
        mPaymentCardList = cardList;

    }

    // creating view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.payment_adapter_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    // binding view This method internally calls onBindViewHolder(ViewHolder, int) to update the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if (mPaymentCardList != null) {

            /* setting card holder name*/
            viewHolder.mCardHolderNameTv.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(mPaymentCardList.get(position).getCardHolderName()));

            /* setting card number with last 4 digits visible */
            String cardLastDigits = String.valueOf(new StringFormatterClass().getCardTransformation(mPaymentCardList.get(position).getCardNumber()));
            viewHolder.mCardNumberTv.setText(cardLastDigits);
            viewHolder.mCardTypeIv.setImageResource(CardType.forCardNumber(mPaymentCardList.get(position).getCardNumber()).getFrontResource());
        }
        /*
         * click listener
         * */
        viewHolder.mDeleteIv.setTag(position);
        viewHolder.mDeleteIv.setOnClickListener(this);
    }

    // total number of items in the list
    @Override
    public int getItemCount() {
        return mPaymentCardList.size();
    }

    // onClick method
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.delete_card_iv:
                int position = (int) v.getTag();
                mRecyclerViewClickListeners.clickListenerWithStringAndPosition(position, mPaymentCardList.get(position).getCardNumber());
                break;
        }
    }

    // ViewHolder class that holds the layout views
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mCardNumberTv, mCardHolderNameTv;
        ImageView mCardTypeIv, mDeleteIv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCardNumberTv = itemView.findViewById(R.id.card_number_tv); // card number textView
            mCardHolderNameTv = itemView.findViewById(R.id.card_holder_name_tv); // card holder name textView
            mCardTypeIv = itemView.findViewById(R.id.card_type); // card type ImageView
            mDeleteIv = itemView.findViewById(R.id.delete_card_iv); // delete card imageView
            // Glide dependency to set the GIF file delete card image
            Glide.with(mContext).load(R.drawable.delete_card)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(mDeleteIv);
        }
    }
}
