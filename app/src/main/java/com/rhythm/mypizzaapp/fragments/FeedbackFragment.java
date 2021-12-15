package com.rhythm.mypizzaapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;

/**
 * Fragment to send feedback
 * */

public class FeedbackFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private Button mSubmitButton;
    private RatingBar mFeedbackRating;
    private EditText mCommentsEdt;
    private Dialog mDialog;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CommonActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.FEEDBACK_FRAGMENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * Initializing values
         * */
        initializing(view);

    }
    /*
     * Initializing values
     * */
    private void initializing(View view) {

        mCommentsEdt = view.findViewById(R.id.feedback_edt);
        mFeedbackRating = view.findViewById(R.id.feedback_rating);
        View submitButtonLayout = view.findViewById(R.id.submit_button);
        mSubmitButton = submitButtonLayout.findViewById(R.id.common_btn);

        mSubmitButton.setText(mContext.getResources().getString(R.string.submit_btn_txt));

        /* click listener */
        mSubmitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.common_btn) {
            if (mFeedbackRating.getRating() < 1){
                Toast.makeText(mContext, "Please give at least 1 star.", Toast.LENGTH_SHORT).show();
            }else {
                ((CommonActivity) mContext).callFragments(GlobalConstants.FRAGMENT.ORDER_HISTORY,
                        GlobalConstants.FRAGMENT.ORDER_HISTORY, 1, true, false,
                        null, null, "");
                Toast.makeText(mContext, "Thank you! for your feedback.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}