// THIS IS DEPRECATED
package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Bundle extras = getIntent().getExtras();
        Long orderId = extras.getLong("orderId");
        Long acctId = extras.getLong("acctId");
        String restaurantName = extras.getString("restaurantName");

        TextView orderNumLabel= findViewById(R.id.txtOrderNumber);
        TextView orderPlacedLabel= findViewById(R.id.txtOrderPlaced);
        orderNumLabel.setText("Your order number is #00" + orderId);
//        orderPlacedLabel.setText("Your order is placed for " + restaurantName);
        Log.i("resto name", restaurantName);

        Button backbtn = findViewById(R.id.btnHome);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderConfirmation.this, CustomerHomeActivity.class);
                i.putExtra("acctId", acctId);
                startActivity(i);
            }
        });



    }
}