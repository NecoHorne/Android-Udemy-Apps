package com.necohorne.calcounter.data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.necohorne.calcounter.Activities.FoodItemDetailsActivity;
import com.necohorne.calcounter.Model.Food;
import com.necohorne.calcounter.R;
import com.necohorne.calcounter.Util.ViewHolder;

import java.util.ArrayList;

/**
 * Created by necoh on 2017/12/21.
 */

public class CustomListViewAdapter extends ArrayAdapter<Food>{

    private int layOutResource;
    private Activity activity;
    private ArrayList<Food> foodList = new ArrayList<>();

    public CustomListViewAdapter(Activity act, int resource, ArrayList<Food> data) {
        super( act, resource, data );
        layOutResource =resource;
        activity = act;
        foodList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Nullable
    @Override
    public Food getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public int getPosition(@Nullable Food item) {
        return super.getPosition( item );
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId( position );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null || (row.getTag() == null)) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            row = inflater.inflate(layOutResource, null);

            holder = new ViewHolder();
            holder.foodname = (TextView) row.findViewById(R.id.name);
            holder.foodDate = (TextView) row.findViewById(R.id.dateText);
            holder.foodCalories = (TextView) row.findViewById(R.id.calories);

            row.setTag(holder);

        }else {
            holder = (ViewHolder) row.getTag();
        }

        holder.food = getItem(position);
        holder.foodname.setText(holder.food.getFoodName());
        holder.foodDate.setText(holder.food.getRecordDate());
        holder.foodCalories.setText(String.valueOf(holder.food.getCalories()));

        final ViewHolder finalHolder = holder;
        row.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, FoodItemDetailsActivity.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("userObj", finalHolder.food);
                i.putExtras(mBundle);

                activity.startActivity(i);
            }
        } );

        return row;
    }

    public Activity getActivity() {
        return activity;
    }
}
