package com.necohorne.mygrocerylist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.necohorne.mygrocerylist.Model.Grocery;
import com.necohorne.mygrocerylist.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by necoh on 2017/12/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;

    //Constructor for DatabaseHandler
    public DatabaseHandler(Context context) {
        super( context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // First the SQL CREATE statement needs to be prepared in a string variable.
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_GROCERY_ITEM +
                " TEXT," + Constants.KEY_QTY_NUMBER + " TEXT," + Constants.KEY_DATE_NAME +
                " LONG);";

        //after the SQL CREATE variable has been created as a string variable it can be inserted in
        // the execSQL() method to actually create the db object.
        db.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /**
     * CRUD OPERATIONS; Create, Read, Update, Delete
     */

    //Add Grocery
    public void addGrocery(Grocery grocery){
        //first a writable database needs to be created.
        SQLiteDatabase db = this.getWritableDatabase();

        //then set up a ContentValues item calling it values and add the key/value items from the Grocery Class.
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //insert the row in the table.
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");
    }

    //Get a Grocery Item
    public Grocery getGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER,
                Constants.KEY_DATE_NAME},Constants.KEY_ID + "=?", new String[] {
                String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

            Grocery grocery = new Grocery();
            grocery.setId(Integer.parseInt(cursor.getString( cursor.getColumnIndex(Constants.KEY_ID))));
            grocery.setName(cursor.getString( cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
            grocery.setQuantity(cursor.getString( cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

            //for the date object a different approach is needed as in the add method the time is added as ms system time
            // and convert into something readable.
            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
            String formatedDate = dateFormat.format(new Date( cursor.getLong(cursor.getColumnIndex( Constants.KEY_DATE_NAME )))
            .getTime());

            grocery.setDateItemAdded(formatedDate);


        return grocery;
    }

    //Get all Groceries
    public List<Grocery> getAllGroceries(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<Grocery> groceryList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {
                Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER,
                Constants.KEY_DATE_NAME},null, null,null,null,Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()){
            do {
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString( cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString( cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString( cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date( cursor.getLong(cursor.getColumnIndex( Constants.KEY_DATE_NAME )))
                        .getTime());
                grocery.setDateItemAdded(formatedDate);
                // add the item to the ArrayList!!!
                groceryList.add(grocery);
            }while (cursor.moveToNext());
        }
        return groceryList;
    }

    //Update Grocery table
    public int updateGrocery(Grocery grocery) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //update Row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] {
                String.valueOf(grocery.getId())});
    }

    //Delete Grocery
    public void deleteGrocery(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( Constants.TABLE_NAME,Constants.KEY_ID + "=?", new String[]
                {String.valueOf(id)});
        db.close();
    }

    //Count of Grocery Items in the db.
    public int getGroceryCount(){
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }
}
