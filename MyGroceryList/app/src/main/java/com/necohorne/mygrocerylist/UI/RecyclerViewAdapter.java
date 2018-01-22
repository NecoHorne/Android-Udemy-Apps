package com.necohorne.mygrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.necohorne.mygrocerylist.Activities.DetailsActivity;
import com.necohorne.mygrocerylist.Model.Grocery;
import com.necohorne.mygrocerylist.R;
import com.necohorne.mygrocerylist.data.DatabaseHandler;

import java.util.List;

/**
 * Created by necoh on 2017/12/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;


    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.groceryItemQuantity.setText(grocery.getQuantity());
        holder.groceryItemDate.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView groceryItemName;
        public TextView groceryItemQuantity;
        public TextView groceryItemDate;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            groceryItemName = (TextView) view.findViewById(R.id.name);
            groceryItemQuantity = (TextView) view.findViewById(R.id.quantity);
            groceryItemDate = (TextView) view.findViewById(R.id.dateAdded);

            editButton = (Button) view.findViewById( R.id.editButton );
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to DetailsActivity
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);

                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra( "name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());

                    context.startActivity(intent);
                }
            } );
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get( position );
                    editItem(grocery);
                    break;

                case R.id.deleteButton:
                    position = getAdapterPosition();
                    grocery = groceryItems.get(position);
                    deleteItem(grocery.getId());
                    break;
            }
        }

        public void deleteItem(final int id) {

            //create an alertdialogbuilder object.
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate( R.layout.confirmation_dialog,null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView( view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            } );

            yesButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item from the db.
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            } );

        }

        public void editItem (final Grocery grocery) {

            alertDialogBuilder = new AlertDialog.Builder( context );
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.pop_up,null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.groceryItem);
            final EditText quantity = (EditText) view.findViewById(R.id.groceryQty);
            final TextView title = (TextView) view.findViewById( R.id.title);

            title.setText("Edit Grocery Item");

            Button saveButton = (Button) view.findViewById( R.id.saveButton);

            alertDialogBuilder.setView( view );
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler( context );

                    // update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty() && quantity.getText().toString().isEmpty()) {
                        db.updateGrocery( grocery );
                        notifyItemChanged( getAdapterPosition(), grocery);
                    }else {
                        Snackbar.make( view, "Add item and quantity", Snackbar.LENGTH_LONG ).show();
                    }
                    dialog.dismiss();
                }
            } );
        }
    }
}
