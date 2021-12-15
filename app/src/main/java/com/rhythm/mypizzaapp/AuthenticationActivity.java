package com.rhythm.mypizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.fragments.ForgotPasswordFragment;
import com.rhythm.mypizzaapp.fragments.LoginFragment;
import com.rhythm.mypizzaapp.fragments.SignupFragment;
import com.rhythm.mypizzaapp.utils.ValidationsClass;

/** AuthenticationActivity is where we display, Login, Signup, and Forgot Password
 * fragments, when this activity it calls the login fragment to display on the screen.
 *
 * Fragment is a part of an activity which enable more modular activity design.
 * Fragment is a kind of sub-activity. It represents a behaviour or a portion of user interface in an Activity.
 * We can combine multiple Fragments in Single Activity to build a multi panel UI and reuse a Fragment in multiple Activities.
 * */

public class AuthenticationActivity extends AppCompatActivity {

    protected Fragment mFragment;
    protected FragmentManager mFragmentManager;
    protected ValidationsClass mValidationClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // initializing fragment
        initialize();

        // checking intent to check which fragment is called.
        checkIntent();
    }

    protected void initialize() {
        //FragmentManager is the class responsible for performing actions on your app's fragments,
        // such as adding, removing, or replacing them, and adding them to the back stack.
        mFragmentManager = getSupportFragmentManager();
        mValidationClass = new ValidationsClass();
    }

    // An Intent provides a facility for performing late runtime binding between the code in different applications.
    // Its most significant use is in the launching of activities,
    // where it can be thought of as the glue between activities.
    // here we are checking intent, whether this activity has any intent or not..
    // if not then by default we will call the Login fragment.
    protected void checkIntent() {
        if (getIntent() != null && getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG) != null && !getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG).isEmpty()) {
            String title = getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE);
            String fragmentTag = getIntent().getStringExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG);

            /* Update Toolbar Title */
            if (mValidationClass.checkStringNull(title)) {

                /* Call Fragment as per requirement */
                callFragments(fragmentTag, title, 0, false);
            }

        } else {
            // Initialized home fragment
            callFragments(GlobalConstants.FRAGMENT.LOGIN, GlobalConstants.FRAGMENT.LOGIN, 0, false);
        }
    }

    // calling fragments according to above intents
    // here fragTag is tagName of the fragment
    // fragName is name of the Fragment, fragTag and fragName are same
    public void callFragments(String fragTag, String fragName, int removeFragCount, boolean removeFragCheck) {

        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        switch (fragTag) {
            case GlobalConstants.FRAGMENT.LOGIN:
                mFragment = new LoginFragment();
                break;
            case GlobalConstants.FRAGMENT.SIGN_UP:
                mFragment = new SignupFragment();
                break;
            case GlobalConstants.FRAGMENT.FORGOT_PASSWORD:
                mFragment = new ForgotPasswordFragment();
                break;
        }
        if (mFragment != null) {
            // removeFragCheck : if You want to remove fragment before open new fragment
            if (removeFragCheck)
                mFragmentManager.popBackStackImmediate(mFragmentManager.getBackStackEntryCount() - removeFragCount, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //Each set of fragment changes that you commit is called a transaction,
            // and you can specify what to do inside the transaction using the APIs provided by the FragmentTransaction
            // class. You can group multiple actions into a single transaction—for example,
            // a transaction can add or replace multiple fragments.
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.authentication_frame, mFragment, fragTag);
            //The commit() call signals to the FragmentManager
            // that all operations have been added to the transaction.
            fragmentTransaction.commit();
        }
    }
}