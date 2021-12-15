package com.rhythm.mypizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.fragments.AddCardFragment;
import com.rhythm.mypizzaapp.fragments.AgreementFragment;
import com.rhythm.mypizzaapp.fragments.EditProfileFragment;
import com.rhythm.mypizzaapp.fragments.FeedbackFragment;
import com.rhythm.mypizzaapp.fragments.MyCartFragment;
import com.rhythm.mypizzaapp.fragments.OrderHistoryFragment;
import com.rhythm.mypizzaapp.fragments.ConfirmOrderFragment;
import com.rhythm.mypizzaapp.fragments.OrderPlacedSuccessfullyFragment;
import com.rhythm.mypizzaapp.fragments.OrderStatusFragment;
import com.rhythm.mypizzaapp.fragments.PlaceYourOrderFragment;
import com.rhythm.mypizzaapp.fragments.ReportAnIssueFragment;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PizzaMenuBeanClass;
import com.rhythm.mypizzaapp.utils.ValidationsClass;

import java.util.ArrayList;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private ImageView mBackArrowIcon;
    private Toolbar mToolbar;

    /*
     * Toolbar Components
     * */
    private TextView mToolbarCustomTxt;

    /*
     * Validation class instance
     * */
    private ValidationsClass mValidationsClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        /* Initialize */
        initialize();

        checkIntent();
    }

    /* Initialize */
    public void initialize() {

        /* Initialized Validation class */
        mValidationsClass = new ValidationsClass();

        /* Initialize Toolbar */
        mToolbar = findViewById(R.id.my_toolbar);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        findViewById(R.id.my_appbar).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolbarCustomTxt = findViewById(R.id.home_activity_title_txt);
        mBackArrowIcon = mToolbar.findViewById(R.id.back_button);
        mBackArrowIcon.setOnClickListener(this);
    }

    // checking intent passed
    protected void checkIntent() {
        if (getIntent() != null && getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG) != null && !getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG).isEmpty()) {
            String title = getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE);
            String fragmentTag = getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG);
            boolean backStack = getIntent().getBooleanExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_BACK_STACK, false);
            Bundle menuBundleData = getIntent().getBundleExtra(GlobalConstants.MENU_BUNDLE_DATA_FOR_PLACE_ORDER);
            ArrayList<PizzaMenuBeanClass> mSelectedItemData = new ArrayList<>();
            // here we are getting the Pizza description list from Menu fragment as a Bundle
            // and parsing this bundle into ArrayList of type PizzaMenuBeanClass and sending this list
            // as a perimeter to PlaceOrderFragment.
            if (menuBundleData != null) {
                mSelectedItemData = (ArrayList<PizzaMenuBeanClass>) menuBundleData.getSerializable(GlobalConstants.PIZZA_DESCRIPTION);
            }

            /* Update Toolbar Title */
            if (mValidationsClass.checkStringNull(title)) {
                /* Call Fragment as per requirement */
                callFragments(fragmentTag, title, 0, false, backStack,null, mSelectedItemData, "");
            }
        }
    }

    // calling fragments
    public void callFragments(String fragTag, String fragName, int removeFragCount, boolean removeFragCheck, boolean addToBackStack, ArrayList<OrderHistoryBeanClass> completeOrderList, ArrayList<PizzaMenuBeanClass> modifyOrderList, String stringValues) {

        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (fragTag) {
            case GlobalConstants.FRAGMENT.PLACE_ORDER_FRAGMENT:
                mFragment = new PlaceYourOrderFragment(modifyOrderList);
                break;
            case GlobalConstants.FRAGMENT.MY_CART_FRAGMENT:
                mFragment = new MyCartFragment();
                break;
            case GlobalConstants.FRAGMENT.ORDER_HISTORY:
                mFragment = new OrderHistoryFragment();
                break;
            case GlobalConstants.FRAGMENT.CONFIRM_ORDER_FRAGMENT:
                mFragment = new ConfirmOrderFragment(completeOrderList, stringValues);
                break;
            case GlobalConstants.FRAGMENT.AGREEMENT:
                String title = getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.ONLINE_DOCUMENT_TYPE);
                mFragment = new AgreementFragment(title);
                break;
            case GlobalConstants.FRAGMENT.ORDER_STATUS_FRAGMENT:
                mFragment = new OrderStatusFragment();
                break;
            case GlobalConstants.FRAGMENT.REPORT_AN_ISSUE:
                mFragment = new ReportAnIssueFragment();
                break;
            case GlobalConstants.FRAGMENT.FEEDBACK_FRAGMENT:
                mFragment = new FeedbackFragment();
                break;
            case GlobalConstants.FRAGMENT.ADD_CARD_FRAGMENT:
                mFragment = new AddCardFragment();
                break;
            case GlobalConstants.FRAGMENT.EDIT_PROFILE:
                mFragment = new EditProfileFragment();
                break;
            case GlobalConstants.FRAGMENT.ORDER_PLACED_SUCCESS:
                mFragment = new OrderPlacedSuccessfullyFragment();
                break;

        }
        if (mFragment != null) {
            // removeFragCheck : if You want to remove fragment before open new fragment
            if (removeFragCheck) {
                mFragmentManager.popBackStackImmediate(mFragmentManager.getBackStackEntryCount() - removeFragCount, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            if (addToBackStack){
                fragmentTransaction.addToBackStack(fragTag);
            }
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.common_activity_Frame, mFragment, fragTag);
            fragmentTransaction.commit();
        }

    }

    // setting toolbar title
    public void setToolbarTitle(String title) {
        if (mValidationsClass.checkStringNull(title)) {
            mToolbarCustomTxt.setText(title);
        }
    }

//    onClick method
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
        }
    }

//    called when user pressed the back icon
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStackImmediate(mFragmentManager.getBackStackEntryCount() - 1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}