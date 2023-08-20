package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerActivity extends AppCompatActivity {

    String[] food = {"Pasta With Seafood", "Bruschetta"};
    int[] images = {R.drawable.placeholder, R.drawable.bgimg};
    long acctId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deprecated_activity_customer);

        ArrayList<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for(int i=0; i<food.length; i++)
        {
            HashMap<String, String> hashMap =new HashMap<>();
            hashMap.put("txt", food[i]);
            hashMap.put("images", Integer.toString(images[i]));
            aList.add(hashMap);
        }

        String[] from ={"images", "txt"};
        int[] to ={R.id.imageFood, R.id.FoodText};
        SimpleAdapter adapter = new SimpleAdapter(CustomerActivity.this,
                aList,R.layout.listview_layout,from,to);
        ListView listView = findViewById(R.id.listView);

        listView.setAdapter(adapter);


//


    }
}