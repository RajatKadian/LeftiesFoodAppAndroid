package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantHomeActivity extends AppCompatActivity {
    RecyclerView inventoryList;
    DBHelper dbh;
    ArrayList<HashMap<String, String>> inventoryMapper = new ArrayList<>();
    Button btnAddItem;
    Button btnOrderHistory;
    TextView headline;
    String restaurantName;
    ArrayList<HashMap> foods;
    long acctId;
    FoodItemAdapterClass adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        Button editAccnBtn = findViewById(R.id.editAccnBtn);
        Button logOutBtn = findViewById(R.id.logOutBtn);


        Bundle extras = getIntent().getExtras();
        acctId = extras.getLong("acctId");
        restaurantName = extras.getString("acctName");

        headline = findViewById(R.id.txtRestoHomeWelcome);

        dbh = new DBHelper(this);
        foods = new ArrayList<HashMap>();

        inventoryList = findViewById(R.id.customerRestaurantRecycler);
        int columnCount = 2;
        inventoryList.setLayoutManager(
                new GridLayoutManager(this, columnCount));

        adapter = new FoodItemAdapterClass(this, foods, acctId);
        inventoryList.setAdapter(adapter);
        displayFoodItemFromRecycler();


        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddItem();
            }
        });

        btnOrderHistory = findViewById(R.id.btnOrderHistoryRest);
        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantHomeActivity.this, OrderHistoryActivity.class));
            }
        });

        //Raiyan-Edit Restaurant Account Functionality
        editAccnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantHomeActivity.this, SignUpActivity.class);
                intent.putExtra("formType", "editAccount");
                intent.putExtra("accountType", sharedPref.getString("accountType", ""));
                intent.putExtra("formName", "Edit " + sharedPref.getString("acctName", "") +" Account");
                intent.putExtra("accountId", acctId);
                startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RestaurantHomeActivity.this, MainActivity.class);
                editor.clear();
                editor.apply();
                startActivity(intent);
            }
        });
    }

    public void goToAddItem(){
        Intent i = new Intent(getApplicationContext(), RestaurantAddAnItemActivity.class);
        i.putExtra("acctId", acctId);
        i.putExtra("acctName", restaurantName);
        startActivity(i);
    }

    public void displayFoodItemFromRecycler(){

        // CREATE ARRAYLIST of HashMap FROM DB
        Cursor c = dbh.viewDataFoodByRestaurant(acctId);
        foods.clear();

        if(c.getCount() > 0){
            while(c.moveToNext()){ // while there is still line left
                HashMap<String, String> foodTableColumns = new HashMap<>();
                foodTableColumns.put("food_id", c.getString(0));
                foodTableColumns.put("account_id", c.getString(1));
                foodTableColumns.put("food_name", c.getString(2));
                foodTableColumns.put("food_discounted_price", c.getString(3));
                foodTableColumns.put("food_regular_price", c.getString(4));
                foodTableColumns.put("food_qty", c.getString(5));
                foodTableColumns.put("restaurant_name", restaurantName);
                foods.add(foodTableColumns);
            }
        }
        adapter.notifyDataSetChanged();

    }



}
