package com.rhythm.mypizzaapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rhythm.mypizzaapp.R;
import com.rhythm.mypizzaapp.getterAndSetterClasses.NavigationDrawerItemBeans;

import java.util.List;

/**
 * Custom navigation drawer adapter for navigation view (side menu)
 * */

public class CustomNavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItemBeans> {

  private   Context mContext;
  private   List<NavigationDrawerItemBeans> mDrawerItemList;
  private   int mLayoutResID;
//  private int lastValue;

    public CustomNavigationDrawerAdapter(Context context, int layoutResourceID, List<NavigationDrawerItemBeans> listItems) {
        super(context, layoutResourceID, listItems);
        this.mContext = context;
        this.mDrawerItemList = listItems;
        this.mLayoutResID = layoutResourceID;
    }


    /*
     * getting view for custom navigation
    */
    @SuppressLint("UseCompatLoadingForDrawables")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        DrawerItemHolder drawerHolder;
       View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();
            view = inflater.inflate(mLayoutResID, parent, false);
            drawerHolder.mItemName = view.findViewById(R.id.drawer_itemName);
            drawerHolder.mIcon =  view.findViewById(R.id.drawer_icon);
            drawerHolder.mView=view.findViewById(R.id.view_for_list);
            view.setTag(drawerHolder);
        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        NavigationDrawerItemBeans dItem = this.mDrawerItemList.get(position);
            drawerHolder.mIcon.setVisibility(View.VISIBLE);
            drawerHolder.mIcon.setImageDrawable(view.getResources().getDrawable(
                    dItem.getImage()));

        drawerHolder.mItemName.setText(dItem.getItemName());

        if(position==8){
           drawerHolder.mView.setVisibility(View.GONE);
        }

        return view;
    }

    /* Drawer item holder class */
    protected static class DrawerItemHolder {
        private TextView mItemName;
        private ImageView mIcon;
        private View mView;
    }
}