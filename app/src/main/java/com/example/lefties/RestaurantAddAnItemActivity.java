package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.GLDebugHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class RestaurantAddAnItemActivity extends AppCompatActivity {

    Button addItem;
    long acctId;
    String acctName;
    int foodItemToEditId;
    DBHelper dbh;

    EditText name;
    EditText salePrice;
    EditText regularPrice;
    EditText stock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_add_an_item);
        dbh = new DBHelper(this);
        // Get account id
        Bundle extras = getIntent().getExtras();
        acctId = extras.getInt("acctId");
        acctName = extras.getString("acctName");

        name = findViewById(R.id.editName);
        salePrice = findViewById(R.id.editSalePrice);
        regularPrice = findViewById(R.id.editRegularPrice);
        stock = findViewById(R.id.editStocks);

        addItem = findViewById(R.id.btnAddInventory);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                goToRestaurantHome();
                addItem();
            }
        });
    }


    public void addItem(){
        String fname = name.getText().toString();
        Double fDiscPrice = Double.valueOf(salePrice.getText().toString());
        Double fRegPrice = Double.valueOf(regularPrice.getText().toString());
        int fstock = Integer.valueOf(stock.getText().toString());

        dbh.addFood(
                acctId, fname, fDiscPrice, fRegPrice, fstock
        );
        goToRestaurantHome();

    }

    public void goToRestaurantHome(){
        Intent i = new Intent(getApplicationContext(), RestaurantHomeActivity.class);
        i.putExtra("acctId", acctId);
        i.putExtra("acctName", acctName);
        startActivity(i);
    }
}