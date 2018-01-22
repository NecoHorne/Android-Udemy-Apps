package com.necohorne.calcounter.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.necohorne.calcounter.Model.Food;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by necoh on 2017/12/21.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Food> foodList = new ArrayList<>();
    private Context ctx;

    public DatabaseHandler(Context context) {
        super( context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.FOOD_NAME +
                " TEXT," + Constants.FOOD_CALORIES_NAME + " TEXT," + Constants.DATE_NAME +
                " LONG);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /**
     * CRUD Operations: Create, Read, Update, Delete.
     */

    public void addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.FOOD_NAME, food.getFoodName());
        values.put(Constants.FOOD_CALORIES_NAME, food.getCalories());
        values.put(Constants.DATE_NAME, food.getRecordDate());

        db.insert( Constants.TABLE_NAME, null, values );
        db.close();

        Log.d("Saved", "Item Saved to DB");
    }

    public Food getFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.FOOD_NAME, Constants.FOOD_CALORIES_NAME,
                Constants.DATE_NAME},Constants.KEY_ID + "=?", new String[] {
                String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Food food = new Food();
        food.setFoodId(Integer.parseInt(cursor.getString( cursor.getColumnIndex(Constants.KEY_ID))));
        food.setFoodName(cursor.getString( cursor.getColumnIndex(Constants.FOOD_NAME)));
        food.setCalories(cursor.getInt( cursor.getColumnIndex(Constants.FOOD_CALORIES_NAME)));

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date( cursor.getLong(cursor.getColumnIndex( Constants.DATE_NAME )))
                .getTime());

        food.setRecordDate(formatedDate);

        return food;

    }

    public ArrayList<Food> getAllFood(){
        foodList.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Food> foodList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.FOOD_NAME, Constants.FOOD_CALORIES_NAME,
                Constants.DATE_NAME},null, null,null,null,Constants.DATE_NAME + " DESC");

        if (cursor.moveToFirst()){
            do {
            Food food = new Food();
            food.setFoodId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
            food.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.FOOD_NAME)));
            food.setCalories(cursor.getInt(cursor.getColumnIndex(Constants.FOOD_CALORIES_NAME)));

            DateFormat dateFormat = DateFormat.getDateInstance();
            String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex( Constants.DATE_NAME))).getTime());
            food.setRecordDate(date);

            foodList.add(food);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodList;
    }

    public int updateFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.FOOD_NAME, food.getFoodName());
        values.put(Constants.FOOD_CALORIES_NAME, food.getCalories());
        values.put(Constants.DATE_NAME, food.getRecordDate());

        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] {
                String.valueOf(food.getFoodId())});
    }

    public void deleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( Constants.TABLE_NAME, Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)});
        db.close();
    }

    public int getTotalItems() {
        int totalItems = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery,null);
        totalItems = cursor.getCount();
        cursor.close();
        return totalItems;
    }

    public int totalCalories() {
        int cals = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT SUM( " + Constants.FOOD_CALORIES_NAME + " ) " + "FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery,null);

        if (cursor.moveToFirst()) {
            cals = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return cals;
    }

}
