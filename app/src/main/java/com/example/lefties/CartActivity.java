package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    CartItemAdapterClass adapter;
//    String[] food = {"Paasta With Seafood", "Bruschetta"};
//    int[] images = {R.drawable.placeholder, R.drawable.bgimg};
    DBHelper dbh;
    ArrayList cartItems;
    long acctId;
    RecyclerView cartList;
    String restaurantName;
    long restaurantId;
    Double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Bundle extras = getIntent().getExtras();
        acctId = extras.getLong("acctId");
        restaurantName = extras.getString("restaurantName");
        restaurantId = extras.getLong("restaurantId");
        total = 0.0;

        dbh = new DBHelper(this);
        cartItems = new ArrayList<>();

        Button btnBack = findViewById(R.id.btnBack);
        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        Chip chipDelivery = findViewById(R.id.chipDelivery);
        Chip chipPickup = findViewById(R.id.chipPickup);
        TextView address = findViewById(R.id.txtAddress);
        TextView totalAmount = findViewById(R.id.txtAmount);
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        Button btnOrderMoreFood = findViewById(R.id.btnOrderMore);

        cartList = findViewById(R.id.cartRecycler);

        int columnCount = 1;
//        cartList.setAdapter();
        cartList.setLayoutManager(
                new GridLayoutManager(this, columnCount)
        );

        adapter = new CartItemAdapterClass(this, cartItems , acctId, restaurantName);
        cartList.setAdapter(adapter);
        getCartContent();
        totalAmount.setText("$" + total);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long orderId = placeOrder();
                if(orderId>0){
                    Intent i = new Intent(CartActivity.this, OrderConfirmation.class);
                    i.putExtra("orderId", orderId);
                    i.putExtra("restaurantName", restaurantName);
                    startActivity(i);
                }else{
                    Toast.makeText(CartActivity.this, "Error with your order", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOrderMoreFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CustomerHomeActivity.class));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });

    }

    public void getCartContent() {
        cartItems.clear();
        Cursor c = dbh.viewCustomerCart(acctId);

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                long cartItem = Long.parseLong(c.getString(0));

                Cursor foodCursor = dbh.viewDataFoodById(cartItem);
                if (foodCursor.moveToFirst()){
                    Double itemPrice = foodCursor.getDouble(3);
                    total += itemPrice;
                }
                cartItems.add(cartItem);
            }
        adapter.notifyDataSetChanged();
        }
    }

    public long placeOrder(){
        long orderId = 0;
        // create order
        orderId = dbh.createOrder(
                acctId,
                restaurantId,
                total
        );
        dbh.updateCartWithOrder(orderId, acctId);

        // add orderId to all food item, checkout is true
        // compute total
        // edit order

        return orderId;
    };


//    public void applyListView(){
////        ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
////
////        for(int i=0; i<food.length; i++)
////        {
////            HashMap<String, String> hashMap =new HashMap<>();
////            hashMap.put("txt", food[i]);
////            hashMap.put("images", Integer.toString(images[i]));
////            aList.add(hashMap);
////        }
//
//        String[] from ={"images", "txt"};
//        int[] to ={R.id.imageFood, R.id.FoodText};
//
//        SimpleAdapter adapter = new SimpleAdapter(CartActivity.this,
//                aList,R.layout.recycler_cart_item,from,to);
//
//        ListView listView = findViewById(R.id.listViewSummary);
//
//        listView.setAdapter(adapter);
//    }

}