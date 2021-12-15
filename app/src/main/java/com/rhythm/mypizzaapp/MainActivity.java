package com.rhythm.mypizzaapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.adapters.CustomNavigationDrawerAdapter;
import com.rhythm.mypizzaapp.fragments.AboutUsFragment;
import com.rhythm.mypizzaapp.fragments.MenuFragment;
import com.rhythm.mypizzaapp.fragments.MyProfileFragment;
import com.rhythm.mypizzaapp.fragments.MyOrderFragment;
import com.rhythm.mypizzaapp.fragments.PaymentFragment;
import com.rhythm.mypizzaapp.fragments.SettingsFragment;
import com.rhythm.mypizzaapp.getterAndSetterClasses.NavigationDrawerItemBeans;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.utils.SharedPreferenceClass;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;
import com.rhythm.mypizzaapp.utils.ValidationsClass;
import com.rhythm.mypizzaapp.utils.localdatabase.PaymentCardDatabaseHelper;
import com.rhythm.mypizzaapp.utils.localdatabase.SignupDatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;

/** Main activity shows the Home screen where pizza list will showed up,
 * a side menu that contains Order history, profile, settings, about us fragments
 * */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private ImageView mNavigationIcon, mProfilePicture, mCartIv;
    private TextView mToolbarTitle, mProfileName;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private ListView mMenuLayout;
    private NavigationView mNavigationView;
    private ArrayList<NavigationDrawerItemBeans> mSideMenuDataList;

    /*
     * Validation class instance
     * */
    private ValidationsClass mValidationsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialize */
        initialize();

        checkIntent();
    }

    /* Initialize */
    public void initialize() {

        /* Initialized Validation class */
        mValidationsClass = new ValidationsClass();
        mDrawer = findViewById(R.id.drawer_layout);
        mMenuLayout = findViewById(R.id.menu_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mProfileName = mNavigationView.getHeaderView(0).findViewById(R.id.userName);
        mProfilePicture = mNavigationView.getHeaderView(0).findViewById(R.id.userPic);
        mSideMenuDataList = new ArrayList<>();
        /* Initialize Toolbar */
        // I have created my own Toolbar, so here we are initializing the toolbar
        // setting background
        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        findViewById(R.id.my_appbar).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolbarTitle = findViewById(R.id.home_activity_title_txt);
        mNavigationIcon = mToolbar.findViewById(R.id.back_button);
        mCartIv = mToolbar.findViewById(R.id.cart_iv);


        /* navigation drawer close and open */
        mToggle = new ActionBarDrawerToggle(this, mDrawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(mToggle);
        setRuntimeChanges();
        clickListeners();
    }


    /*
     * Setting runtime changes
     * */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setRuntimeChanges() {

        mNavigationIcon.setImageDrawable(getResources().getDrawable(R.drawable.side_menu_));
        addItems(mSideMenuDataList);  // calling addItems method that is overrided in child class, that add's data into navigation drawer list.
        mMenuLayout.setAdapter(new CustomNavigationDrawerAdapter(this, R.layout.custom_navigation_layout, mSideMenuDataList));
        mToolbarTitle.setText(this.getResources().getString(R.string.menu));

        // getting name and gender from the stored shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY,Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_NAME, "");
        String gender = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.KEY_GENDER, "");


        if (!userName.isEmpty()){
            if (userName.contains(" ")){
                // here if user name contains a space, we have
                // split the first name and last name(surname)
                // and set only the first name of the user.
                String firstName = userName.substring(0,userName.indexOf(" "));
                mProfileName.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(firstName));
            }else {
                mProfileName.setText(new StringFormatterClass().firstLetterCapsInWholeSentence(userName));
            }
        }else {
            mProfileName.setText(getResources().getString(R.string.dummy_name));
        }

        /*
         * setting profile image, if gender of the user is Male, male_avatar image will set
         * other wise female_avatar image will set by default.
         * */
        if (gender.equalsIgnoreCase("Male")) {
            /*
             * setting profile image
             * */
            Glide.with(this)
                    .load(R.drawable.male_avatar)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile_white)
                            .circleCrop())
                    .into(mProfilePicture);
        }else{
            Glide.with(this)
                    .load(R.drawable.female_avatar)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_my_profile_white)
                            .circleCrop())
                    .into(mProfilePicture);
        }
    }

    /*
     * click listeners
     * */
    private void clickListeners() {
        /*
         * side menu icon click, to open navigation drawer
         * */
        mNavigationIcon.setOnClickListener(this);

        mMenuLayout.setOnItemClickListener(this);
    }

    /*
     * Adding side menu items in list
     * */
    public void addItems(ArrayList<NavigationDrawerItemBeans> dataList) {
        dataList.add(new NavigationDrawerItemBeans(getString(R.string.menu), R.drawable.ic_home_white));
        dataList.add(new NavigationDrawerItemBeans(getString(R.string.my_order), R.drawable.ic_booking));
        dataList.add(new NavigationDrawerItemBeans(getString(R.string.my_profile), R.drawable.ic_my_profile_white));
        dataList.add(new NavigationDrawerItemBeans(getString(R.string.payment), R.drawable.ic_payment_wallet));
        dataList.add(new NavigationDrawerItemBeans(getString(R.string.setting), R.drawable.ic_settings_blue));
        dataList.add(new NavigationDrawerItemBeans(getString(R.string.about_us), R.drawable.ic_contact_us));
        dataList.add(new NavigationDrawerItemBeans(getString(R.string.logout), R.drawable.ic_logout_image));
    }

    /*
     * Checking intent method
     * */
    protected void checkIntent() {
        if (getIntent() != null && getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG) != null && !getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG).isEmpty()) {
            String title = getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE);
            String fragmentTag = getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG);
//          Bundle completeOrderBundleData = getIntent().getBundleExtra(GlobalConstants.MENU_BUNDLE_DATA_FOR_MY_ORDERS);
//            if (completeOrderBundleData != null) {
//                mOrderListForMyOrders = (ArrayList<OrderHistoryBeanClass>) completeOrderBundleData.getSerializable(GlobalConstants.COMPLETE_ORDER_DATA_FOR_MY_ORDERS);
//            }
            // calling fragment
            if (mValidationsClass.checkStringNull(title)) {
                setToolbarTitle(title);
                /* Call Fragment as per requirement */
                    callFragments(fragmentTag, title, 0, false);
            }
        }else {
            setToolbarTitle(GlobalConstants.FRAGMENT.MENU);
            /* Call Fragment as per requirement */
            callFragments(GlobalConstants.FRAGMENT.MENU, GlobalConstants.FRAGMENT.MENU, 0, false);
        }
    }

    /*
     * Call fragment method
     * */
    @SuppressLint("NewApi")
    public void callFragments(String fragTag, String fragName, int removeFragCount, boolean removeFragCheck) {

        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (fragTag) {
            case GlobalConstants.FRAGMENT.MENU:
                mCartIv.setVisibility(View.VISIBLE);
                mCartIv.setOnClickListener(this);
                mFragment = new MenuFragment();
                break;
            case GlobalConstants.FRAGMENT.SETTINGS:
                mCartIv.setVisibility(View.INVISIBLE);
                mFragment = new SettingsFragment();
                break;
            case GlobalConstants.FRAGMENT.MY_PROFILE:
                mCartIv.setVisibility(View.INVISIBLE);
                mFragment = new MyProfileFragment();
                break;
            case GlobalConstants.FRAGMENT.PAYMENT:
                mCartIv.setVisibility(View.INVISIBLE);
                mFragment = new PaymentFragment();
                break;
            case GlobalConstants.FRAGMENT.ABOUT_US:
                mCartIv.setVisibility(View.INVISIBLE);
                mFragment = new AboutUsFragment();
                break;
            case GlobalConstants.FRAGMENT.MY_ORDERS:
                mCartIv.setVisibility(View.INVISIBLE);
                mFragment = new MyOrderFragment();
                break;
        }
        if (mFragment != null) {
            // removeFragCheck : if You want to remove fragment before open new fragment
           int count = mFragmentManager.getBackStackEntryCount();
            if (removeFragCheck) {
                mFragmentManager.popBackStackImmediate(count - removeFragCount, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.home_activity_frame, mFragment, fragTag);
            fragmentTransaction.commit();
        }
    }

    // this method set toolbar title
    public void setToolbarTitle(String title) {
        if (mValidationsClass.checkStringNull(title)) {
            mToolbarTitle.setText(title);
        }
    }

    // onCLick methods
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                if (mDrawer != null) {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.cart_iv:
                ArrayList<OrderHistoryBeanClass> mCompleteOrderListForCart = new ArrayList<>();
                Bundle completeOrderBundleData = getIntent().getBundleExtra(GlobalConstants.MENU_BUNDLE_DATA_FOR_CART);
                if (completeOrderBundleData != null) {
                    mCompleteOrderListForCart = (ArrayList<OrderHistoryBeanClass>) completeOrderBundleData.getSerializable(GlobalConstants.COMPLETE_ORDER_DATA_FOR_CART);
                }

                Intent intent = new Intent(this, CommonActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MY_CART_FRAGMENT);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MY_CART_FRAGMENT);
                Bundle listBundleToTransfer = new Bundle();
                listBundleToTransfer.putSerializable(GlobalConstants.COMPLETE_ORDER_DATA_FOR_CART,(Serializable) mCompleteOrderListForCart);
                intent.putExtra(GlobalConstants.MENU_BUNDLE_DATA_FOR_CART,listBundleToTransfer);
                startActivity(intent);
                break;
        }
    }

    // on side menu item click methods
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String title = "";
        switch (position) {
            case 0:
                title = GlobalConstants.FRAGMENT.MENU;
                break;
            case 1:
                title = GlobalConstants.FRAGMENT.MY_ORDERS;
                break;
            case 2:
                title = GlobalConstants.FRAGMENT.MY_PROFILE;
                break;
            case 3:
                title = GlobalConstants.FRAGMENT.PAYMENT;
                break;
            case 4:
                title = GlobalConstants.FRAGMENT.SETTINGS;
                break;
            case 5:
                title = GlobalConstants.FRAGMENT.ABOUT_US;
                break;
            case 6:
                //user will logout when he clicks logout and we set the loggedIn shared preference to False
                new SharedPreferenceClass(this).setLoggedIn(false);
                new SharedPreferenceClass(this).clearSharedPreferences();
                new PaymentCardDatabaseHelper(this).deleteTable();
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.LOGIN);
                intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.LOGIN);
                startActivity(intent);
                finishAffinity();
                break;
        }

        if (mValidationsClass.checkStringNull(title)) {
            closeNavigationDrawer();
            final String finalTitle = title;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mValidationsClass.checkStringNull(finalTitle))
                        callFragments(finalTitle, finalTitle, 0, false);
                }
            }, 240);
        }
    }


    /* To Close Navigation Drawer */
    public void closeNavigationDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}