/**
 * Author: Guneet
 */
package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryActivity extends AppCompatActivity {
    OrderItemAdapterClass adapter;
    DBHelper dbh;
    ArrayList<HashMap> orderDetails;
    RecyclerView orderList;
    long acctId;
    Boolean isRestaurant;
    //long restaurantId;
    String restaurantName = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // rajat / guneet: problem is here
        // macci: thanks

        Bundle extras = getIntent().getExtras();
        //acctId = extras.getLong("acctId");
        //restaurantId = extras.getLong("restaurantId");
        //restaurantName = extras.getString("acctName");

        // Macci: Get resto type to use on adapater and layout
        acctId = sharedPreferences.getLong("acctID", 1);
        String acnType = sharedPreferences.getString("accountType", "");
        isRestaurant = acnType.equals("Restaurant");
        restaurantName = sharedPreferences.getString("acctName", " ");

        if(isRestaurant){
            TextView title = findViewById(R.id.titleYourOrders);
            title.setTextColor(Color.parseColor("#ffffff"));

            findViewById(R.id.layoutOrderHistory).setBackgroundColor(Color.parseColor("#000000"));
        }

        dbh = new DBHelper(this);
        orderDetails = new ArrayList<>();

        orderList = findViewById(R.id.orderHistRecycler);

        int columnCount = 1;
        orderList.setLayoutManager(
                new GridLayoutManager(this, columnCount)
        );

        adapter = new OrderItemAdapterClass(this, orderDetails, isRestaurant);
        orderList.setAdapter(adapter);
        getOrderDetails(acctId);
    }
    public void getOrderDetails(long acctId) {
        Log.i("acct id is", acctId+" ");
        Log.i("is res is", isRestaurant +" ");
        orderDetails = new ArrayList<HashMap>();
        Cursor c;
        if(isRestaurant){
            c = dbh.viewDataOrderByRestaurantId(acctId);
        }else{
            c = dbh.viewDataOrderByCustomerId(acctId);
        }

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Log.i("orderId", c.getString(0));
                HashMap<String, String> orderTableColumns = new HashMap<>();
               // long orderDetail = Long.parseLong(c.getString(0));
                orderTableColumns.put("order_id", c.getString(0));
                orderTableColumns.put("order_date", c.getString(1));
                orderTableColumns.put("order_status", c.getString(2));
                orderTableColumns.put("order_total", c.getString(3));
                orderTableColumns.put("customer_name", c.getString(5));
                orderTableColumns.put("customer_address", c.getString(7));
                orderTableColumns.put("restaurant_name", restaurantName);

                orderDetails.add(orderTableColumns);
            }
        }else{
            Toast.makeText(getApplicationContext(), "No orders", Toast.LENGTH_LONG).show();
        }
        adapter = new OrderItemAdapterClass(this, orderDetails, isRestaurant);
        orderList.setAdapter(adapter);
    }
}