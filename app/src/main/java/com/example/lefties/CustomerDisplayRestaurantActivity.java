package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerDisplayRestaurantActivity extends AppCompatActivity {

    DBHelper dbh;
    RecyclerView inventoryList;
    ArrayList<HashMap<String, String>> inventoryMapper = new ArrayList<>();
    Button addItem;
    ArrayList<HashMap> foods;
    long acctId;
    long restaurantId;
    String restaurantName;
    FoodItemAdapterClass adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_display_restaurant);

        Bundle extras = getIntent().getExtras();
        acctId = extras.getLong("acctId");
        restaurantId = extras.getLong("restaurantId");
        restaurantName = extras.getString("restaurantName");


        dbh = new DBHelper(this);
        inventoryList = findViewById(R.id.customerRestaurantRecycler);

        int columnCount = 2;
        inventoryList.setLayoutManager(
                new GridLayoutManager(this, columnCount));

        foods = new ArrayList<HashMap>();
        Cursor c = dbh.viewDataFoodByRestaurant(restaurantId);
        updateRecycler(c);

    }

    public void updateRecycler(Cursor c){
        if (c.getCount() > 0) {
            while (c.moveToNext()) { // while there is still line left
                HashMap<String, String> foodTableColumns = new HashMap<>();
                foodTableColumns.put("food_id", c.getString(0));
                foodTableColumns.put("account_id", c.getString(1));
                foodTableColumns.put("food_name", c.getString(2));
                foodTableColumns.put("food_discounted_price", c.getString(3));
                foodTableColumns.put("food_regular_price", c.getString(4));
//                foodTableColumns.put("food_qty", c.getString(5));
                foodTableColumns.put("restaurant_name", restaurantName);
                foods.add(foodTableColumns);
            }
        }
        adapter = new FoodItemAdapterClass(this, foods, acctId);
        inventoryList.setAdapter(adapter);
    }
}