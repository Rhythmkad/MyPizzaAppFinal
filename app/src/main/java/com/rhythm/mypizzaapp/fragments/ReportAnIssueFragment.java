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
import android.widget.Toast;

import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;

/**
 * Report an issue fragment that takes an
 * issue from the user if there is any.
 * */

public class ReportAnIssueFragment extends Fragment implements View.OnClickListener, AlertDialogInterface {

    private EditText mIssueEdt;
    private Button mSubmitIssueBtn;
    private Context mContext;
    private Dialog mDialog;

    public ReportAnIssueFragment() {
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
        ((CommonActivity)mContext).setToolbarTitle(GlobalConstants.FRAGMENT.REPORT_AN_ISSUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_an_issue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void initialize(View view) {
        mIssueEdt = view.findViewById(R.id.report_issue_edt);
        mSubmitIssueBtn = view.findViewById(R.id.submit_your_issue_btn);

        mSubmitIssueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_your_issue_btn){
            if (!mIssueEdt.getText().toString().isEmpty()) {
                mDialog = new AlertDialogClass().showAlertDialog(mContext, "Thank You!", "Your feedback helps make Pizza All Day app better for everyone.",
                        this, false, "Close", "", 0);
                if (mDialog != null) {
                    mDialog.show();
                }
            }else{
                Toast.makeText(mContext, "Please enter a description.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onDialogConfirmAction(int position) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MENU);
        intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MENU);
        startActivity(intent);
        ((CommonActivity) mContext).finish();
    }

    @Override
    public void onDialogCancelAction(int position) {

    }
}