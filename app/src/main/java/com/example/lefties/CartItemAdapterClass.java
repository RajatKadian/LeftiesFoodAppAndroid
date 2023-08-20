package com.example.lefties;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;


public class CartItemAdapterClass extends RecyclerView.Adapter {
    LayoutInflater layoutInflater;
    Context context;
    String contextClass;
    ArrayList cartItems;
    DBHelper dbh;
    long acctId;
    String restaurantName;
    long restaurantId;


    public CartItemAdapterClass(@NonNull Context context, ArrayList cartItems , long acctId, String restaurantName) {
        this.context = context;
        this.cartItems = cartItems;
        this.restaurantName = restaurantName;
        layoutInflater = LayoutInflater.from(context);
        dbh = new DBHelper(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView restName, foodName, foodPrice, itemQty;
        Button btnMinus, btnPlus;


        // THIS MAPS ATTRIBUTES PER ITEM
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.txtFoodName);
            restName = itemView.findViewById(R.id.txtRestName);
            foodPrice = itemView.findViewById(R.id.txtIFoodPrice);
            itemQty = itemView.findViewById(R.id.txtQty);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);

            btnMinus.setOnClickListener(this);
            btnPlus.setOnClickListener(this);

            return;
        }

        @Override
        public void onClick(View v) {
            int quantity = Integer.parseInt(itemQty.getText().toString());
            switch (v.getId()) {
                case R.id.btnMinus:
                    if (quantity > 1) {
                        quantity--;


                    }
                    break;
                case R.id.btnPlus:
                    quantity++;
                    break;
            }
            itemQty.setText(String.valueOf(quantity));

        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view =  layoutInflater.inflate(R.layout.recycler_cart_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view); // viewHolder holds the layoutInflater
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).restName.setText(restaurantName);
        long foodId = (long) cartItems.get(position);
        Cursor c = dbh.viewDataFoodById(foodId);
        if(c.getCount() > 0) {
            if (c.moveToFirst()) {
                ((ViewHolder) holder).foodName.setText(
                        c.getString(2)
                );
                ((ViewHolder) holder).foodPrice.setText(
                        "$ " + c.getString(3)
                );
            }
        }



//
//        HashMap<String, String> foodItem = foods.get(position);
//        String foodIdString = foodItem.get("food_id");
//        long restaurantId = Long.parseLong(foodItem.get("account_id"));
//        int foodId = Integer.parseInt(foodIdString);
//
//        ((ViewHolder)holder).foodName.setText(name);


    }



    public void createOrder(){
        // find all orders related to user Id
    }


    @Override
    public int getItemCount() {
        return cartItems.size() ;
    }
}
