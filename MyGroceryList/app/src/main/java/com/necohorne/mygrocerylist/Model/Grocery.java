package com.necohorne.mygrocerylist.Model;

/**
 * Created by necoh on 2017/12/18.
 */

public class Grocery {

    private String name;
    private String quantity;
    private String dateItemAdded;
    private int id;

    //Constructors for the Grocery Class

    public Grocery() {
    }

    public Grocery(String name, String quantity, String dateItemAdded, int id) {
        this.name = name;
        this.quantity = quantity;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    //Getters and setter methods for the Grocery Class.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
