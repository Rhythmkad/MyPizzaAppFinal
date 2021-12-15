package com.rhythm.mypizzaapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rhythm.mypizzaapp.CommonActivity;
import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.OrderHistoryBeanClass;
import com.rhythm.mypizzaapp.getterAndSetterClasses.PizzaMenuBeanClass;
import com.rhythm.mypizzaapp.utils.AlertDialogClass;
import com.rhythm.mypizzaapp.utils.AlertDialogInterface;
import com.rhythm.mypizzaapp.utils.StringFormatterClass;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This method show the details of the pizza..
 * like pizza size, pizza crust, extra cheese, and extra toppings
 * */

public class PlaceYourOrderFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AlertDialogInterface, AdapterView.OnItemClickListener {

    private Context mContext;
    private ArrayList<PizzaMenuBeanClass> mPizzaDetailDataList;
    private Dialog mDialog;
    private ImageView mPizzaImage, mPizzaTypeTagIv;
    private TextView mPizzaNameTv, mPizzaDetailTv, mNumberOfPizzaTv, mPizzaPriceTv, mSubTotalTv;
    private RadioGroup mRadioGroupPizzaSize, mRadioGroupPizzaCrust;
    private CheckBox mCheeseCheckBox;
    private String pizzaSize = "", pizzaCrust = "", mCustomizeToppings = "";
    private int mTotalAmount, previousLengthOfToppingString = 0, mCountToppings = 0;
    private int pizzaImage, pizzaLabel;
    private ArrayList<OrderHistoryBeanClass> mPizzaToOrderDetails;
    private RadioButton sizeRadioBtn;
    private RadioButton crustRadioBtn;
    private ListView mToppingsListView;
    private Button placeOrderBtn, mIncreasePerson,mDecreasePerson;
    private ArrayList<String> toppingsArrayList;

    public PlaceYourOrderFragment() {
        // Required empty public constructor
    }

    public PlaceYourOrderFragment(ArrayList<PizzaMenuBeanClass> pizzaDetailDataList) {
        // Required empty public constructor
        mPizzaDetailDataList = pizzaDetailDataList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_your_order, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // onDestroy we will dismiss the dialog and set it to null.
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CommonActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.PLACE_ORDER_FRAGMENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);

        setViewsAndListeners();

        setToppingsDataList();
    }

    // initializing views
    private void initialize(View view) {
        mPizzaNameTv = view.findViewById(R.id.pizza_name_tv);
        mPizzaDetailTv = view.findViewById(R.id.pizza_description_tv);
        mSubTotalTv = view.findViewById(R.id.sub_total_tv);
        mNumberOfPizzaTv = view.findViewById(R.id.persons_value_tv);
        mPizzaPriceTv = view.findViewById(R.id.pizza_price);
        mPizzaImage = view.findViewById(R.id.pizza_iv);
        mPizzaTypeTagIv = view.findViewById(R.id.veg_non_veg_label_tag_iv);
        placeOrderBtn = view.findViewById(R.id.place_orderBtn);
        mIncreasePerson = view.findViewById(R.id.plus_btn);
        mDecreasePerson = view.findViewById(R.id.minus_btn);
        mRadioGroupPizzaSize = view.findViewById(R.id.pizza_base_size_radio_group);
        mRadioGroupPizzaCrust = view.findViewById(R.id.pizza_crust_radio_group);
        sizeRadioBtn = view.findViewById(mRadioGroupPizzaSize.getCheckedRadioButtonId());
        crustRadioBtn = view.findViewById(mRadioGroupPizzaCrust.getCheckedRadioButtonId());
        mCheeseCheckBox = view.findViewById(R.id.extra_cheese_checkBox);
        mToppingsListView = view.findViewById(R.id.toppings_listView);
    }

    // setting view and click listeners
    private void setViewsAndListeners() {

        toppingsArrayList = new ArrayList<>();
        String amount = mPizzaPriceTv.getText().toString().substring((mPizzaPriceTv.getText().toString().indexOf(" ")) + 1);
        mTotalAmount = Integer.parseInt(amount);
        if (mPizzaDetailDataList != null){
            for (PizzaMenuBeanClass i : mPizzaDetailDataList){
                mPizzaNameTv.setText(i.getPizzaName());
                mPizzaDetailTv.setText(i.getPizzaDetails());
                pizzaImage = Integer.parseInt(i.getPizzaIv());
                pizzaLabel = Integer.parseInt(i.getPizzaLabelTagIv());
                mPizzaImage.setImageResource(pizzaImage);
                mPizzaTypeTagIv.setImageResource(pizzaLabel);
            }
        }

        mRadioGroupPizzaSize.setOnCheckedChangeListener(this);
        mRadioGroupPizzaCrust.setOnCheckedChangeListener(this);

        placeOrderBtn.setOnClickListener(this);
        mIncreasePerson.setOnClickListener(this);
        mDecreasePerson.setOnClickListener(this);

        mCheeseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mCheeseCheckBox.isChecked()){
                    mTotalAmount += 1;
                }
                else if (!mCheeseCheckBox.isChecked()){
                    mTotalAmount -= 1 ;
                }
                updatePizzaAmount(mTotalAmount);
            }
        });

    }

    // setting topping list
    private void setToppingsDataList(){

        // setChoiceMode is a method of ListView which sets a value
        // indicating whether multiple items can be selected or not.
        // ListView.CHOICE_MODE_SINGLE(1) for single item selection.
        // ListView.CHOICE_MODE_MULTIPLE(2) for multiple item selection.
        // by default it is set to ListView.CHOICE_MODE_NONE, which means 0
        // when using SparseBooleanArray we need to set choice mode of the ListView, else
        // it will show null error on SparseBooleanArray object
        mToppingsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mToppingsListView.setOnItemClickListener(this);

        // adding extra toppings list
        toppingsArrayList.add("Grilled Mushrooms");
        toppingsArrayList.add("Onion");
        toppingsArrayList.add("Capsicum");
        toppingsArrayList.add("Tomato");
        toppingsArrayList.add("Paneer");
        toppingsArrayList.add("Golden Corn");
        toppingsArrayList.add("Black Olives");
        toppingsArrayList.add("Peri-Peri Chicken");
        toppingsArrayList.add("Chicken Barbecue");
        toppingsArrayList.add("Chicken Pepperoni");

        ArrayAdapter mToppingsAdapter = new ArrayAdapter(mContext, R.layout.toppings_item_layout, toppingsArrayList);
        mToppingsListView.setAdapter(mToppingsAdapter);
    }

    // onClick method
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        AlertDialogClass alertDialogClass = new AlertDialogClass();
        int persons;
        switch (view.getId()) {
            case R.id.place_orderBtn:

                setPizzaToOrderDetails();
                mDialog = alertDialogClass.showAlertDialog(mContext, "Order Confirmation", "Place order to your cart \nPizza name: "+ mPizzaNameTv.getText().toString() + "\nQuantity: " + mNumberOfPizzaTv.getText().toString() + "\nTotal: "+ mPizzaPriceTv.getText().toString(), this, false, "Continue", "Cancel", 0);
                if (mDialog != null) {
                    mDialog.show();
                }

            break;
            case R.id.plus_btn:
                persons = Integer.parseInt(mNumberOfPizzaTv.getText().toString()) + 1;
                mNumberOfPizzaTv.setText(String.valueOf(persons));
                updatePizzaAmount(mTotalAmount);
            break;
            case R.id.minus_btn:
                persons = Integer.parseInt(mNumberOfPizzaTv.getText().toString());
                if (persons > 1){
                    persons -= 1;
                    mNumberOfPizzaTv.setText(String.valueOf(persons));
                    updatePizzaAmount(mTotalAmount);
                }
            break;
        }
    }


    // here we are setting the all the details of the selected items
    // and adding it to PizzaToOrderDetailsList
    private void setPizzaToOrderDetails() {
        String pizzaName, pizzaDescription, pizzaCustomizeItems, pizzaQuantity, extraCheese,
                totalAmount, pizzaSize, PizzaCrust;
        mPizzaToOrderDetails = new ArrayList<>();
        pizzaName = mPizzaNameTv.getText().toString();
        pizzaDescription = mPizzaDetailTv.getText().toString();
        totalAmount = mPizzaPriceTv.getText().toString();
        pizzaQuantity = mNumberOfPizzaTv.getText().toString();
        if (mCheeseCheckBox.isChecked()){
            extraCheese = "Extra cheese";
        }else{
            extraCheese = "";
        }

        pizzaSize = sizeRadioBtn.getText().toString().substring(0,sizeRadioBtn.getText().toString().indexOf(" "));
        PizzaCrust = crustRadioBtn.getText().toString().substring(0,crustRadioBtn.getText().toString().indexOf("("));
        StringBuilder stringBuilder = new StringBuilder();

        if (extraCheese.isEmpty()){
            pizzaCustomizeItems = String.valueOf(stringBuilder.append(pizzaSize).append(" | ").append(PizzaCrust));
        }else {
            pizzaCustomizeItems = String.valueOf(stringBuilder.append(pizzaSize).append(" | ").append(PizzaCrust).append(" | ").append(extraCheese));
        }

        pizzaCustomizeItems += " | " + mCustomizeToppings;
        mPizzaToOrderDetails.add(new OrderHistoryBeanClass(pizzaName, pizzaDescription, String.valueOf(pizzaImage), String.valueOf(pizzaLabel), totalAmount, pizzaCustomizeItems, pizzaQuantity,new StringFormatterClass().getFormattedDate()));
    }


    // radio button on checked change method
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        switch (radioGroup.getId()){
            case R.id.pizza_base_size_radio_group:
                sizeRadioBtn = radioGroup.findViewById(id);
                if (pizzaCrust.isEmpty()){
                    pizzaCrust = "Hand Tossed ($ 2)";
                }
                pizzaSize = sizeRadioBtn.getText().toString();
                break;
            case R.id.pizza_crust_radio_group:
                crustRadioBtn = radioGroup.findViewById(id);
                if (pizzaSize.isEmpty()){
                    pizzaSize = "Regular ($ 10)";
                }
                pizzaCrust = crustRadioBtn.getText().toString();
                break;
        }
        setPizzaSizeValue(pizzaSize, pizzaCrust);
    }

    // setting pizza size price value
    private void setPizzaSizeValue(String size, String crust){
        switch (size){
            case "Regular ($ 10)":
                mTotalAmount = 10;
               break;
            case "Medium ($ 15)":
                mTotalAmount = 15;
                break;
            case "Large ($ 20)":
                mTotalAmount = 20;
                break;
        }
                setPizzaCrustValue(crust);
    }

    // setting pizza crust price value
    private void setPizzaCrustValue(String crust){
        switch (crust){
            case "Hand Tossed ($ 2)":
                mTotalAmount += 2;
                break;
            case "Cheese Burst ($ 8)":
                mTotalAmount += 8;
                break;
            case "Fresh Pan Pizza ($ 5)":
                mTotalAmount += 5;
                break;
        }
        if (mCheeseCheckBox.isChecked()){
            mTotalAmount += 1 ;
        }
        updatePizzaAmount(mTotalAmount);
    }

    // this method will update the pizza price Text view
    @SuppressLint("SetTextI18n")
    private void updatePizzaAmount(int totalAmount) {
        // here we are adding total toppings counts, because
        // this method is called everytime when user change any of the size or crust of the pizza.
        totalAmount += mCountToppings;
        totalAmount = totalAmount * Integer.parseInt(mNumberOfPizzaTv.getText().toString());
        mPizzaPriceTv.setText("$ " + totalAmount);
        mSubTotalTv.setText("Subtotal $ " + totalAmount);
    }

    // Alert Dialog methods
    @Override
    public void onDialogConfirmAction(int position) {
        if (mDialog != null) {
            // here when order is placed dialog will show
            // and when user click on confirm button, we will go to the menu screen in MainActivity.
            // with pizza's detail list that includes pizzaName, pizzaDescription, toppings, price etc.
            Intent intent = new Intent((CommonActivity) mContext, MainActivity.class);
            intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.TITLE, GlobalConstants.FRAGMENT.MENU);
            intent.putExtra(GlobalConstants.FRAGMENT_INTENT_DATA.FRAGMENT_TAG, GlobalConstants.FRAGMENT.MENU);

            // here storing the list of the pizza details choose by the user
            // in the shared preference..
            // this list will show in MyCart fragment
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConstants.SHARED_PREFERENCE.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            Gson gson = new Gson();
            String jsonText = sharedPreferences.getString(GlobalConstants.SHARED_PREFERENCE.COMPLETE_ORDER_DETAIL, null);
            Type type = new TypeToken<ArrayList<OrderHistoryBeanClass>>() {}.getType();
            // here we are 1st getting the saved shared preference list
            // if it is not empty we will add new data
            // and update the list and saved it again.
            if (!mPizzaToOrderDetails.isEmpty()){
                if (jsonText != null) {
                    mPizzaToOrderDetails.addAll(gson.fromJson(jsonText, type));
                }//EDIT: gso to gson
            }

            String putJsonText = gson.toJson(mPizzaToOrderDetails);
            prefsEditor.putString(GlobalConstants.SHARED_PREFERENCE.COMPLETE_ORDER_DETAIL, putJsonText);
            prefsEditor.apply();
            startActivity(intent);
            ((CommonActivity) mContext).finish();
            Toast.makeText(mContext, "Order added to cart.", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }
    }

    @Override
    public void onDialogCancelAction(int position) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        getSelectedToppingsItems();
    }

    // this method will get selected toppings item
    private void getSelectedToppingsItems() {

        // getting checked item in the list and storing it in SparseBooleanArray
        // as getCheckedItemPositions() return type is SparseBooleanArray
        //SparseBooleanArrays map integers(in our case item positions) to booleans.
        SparseBooleanArray selectedPositions = mToppingsListView.getCheckedItemPositions();
        StringBuilder stringBuilder = new StringBuilder();

//        int count = toppingsArrayList.size();// gets total number of items added in the listview

        // here we are iterating all the items of the list
        // and checking if selectedPositions array contains
        // the items or not, if true it will add the item.
            for (int i = 0; i < selectedPositions.size(); i++) {
                if (selectedPositions.valueAt(i)) {
                    String topping = mToppingsListView.getItemAtPosition(selectedPositions.keyAt(i)).toString();
                    stringBuilder = stringBuilder.append(topping).append(" | ");
                }
            }

        // through string builder we are appending all the toppings
        // into a single string value, to transfer it further.
        mCustomizeToppings = stringBuilder.toString();

        // here if topping string is empty, means topping selected for the first time, toppings count value will increase.
        if (mCustomizeToppings != null && !mCustomizeToppings.isEmpty()) {
            if (previousLengthOfToppingString == 0) {
                mCountToppings += 1;
            }
            // here if current length of toppings is less than the previous length
            // that means topping is removed from the string, and we need to update the count value.
            else if (mCustomizeToppings.length() < previousLengthOfToppingString) {
                mCountToppings -= 1;
            }
            // here if current length of toppings is greater than previous length
            // that means new topping is added to the string and we need to update the count value.
            else if (mCustomizeToppings.length() > previousLengthOfToppingString) {
                mCountToppings += 1;
            }
        }else{
            // here if all the toppings are removed by the user, means he do not want to add any toppings
            // count set to zero.
            mCountToppings = 0;
        }
        // update pizza Amount value.
        updatePizzaAmount(mTotalAmount);
        previousLengthOfToppingString = mCustomizeToppings.length();
    }

}