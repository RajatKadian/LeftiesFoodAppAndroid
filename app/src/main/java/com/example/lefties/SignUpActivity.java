package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    //Database Initialize
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Bundle extraData = getIntent().getExtras();

        //Spinner Functionality
        Spinner spinner = (Spinner) findViewById(R.id.accontType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.accountType,
                android.R.layout.simple_spinner_dropdown_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Call New Database
        dbh = new DBHelper(this);
        ImageView logoSignUp = findViewById(R.id.logoSignUp);
        TextView textBrandName = findViewById(R.id.textBrandName);
        EditText acnName = findViewById(R.id.accountName);
        Spinner acnType = findViewById(R.id.accontType);
        EditText acnEmail = findViewById(R.id.accountEmail);
        EditText acnPass = findViewById(R.id.accountPassword);
        EditText acnPhoneNumber = findViewById(R.id.txtorderStatus);
        EditText acnAddress = findViewById(R.id.accountAddress);
        Spinner acnCity = findViewById(R.id.accountCity);
        Spinner restaurantType = findViewById(R.id.restaurantType);
        Button btnSignUpConfirm = findViewById(R.id.btnSignUp);
        TextView formName = findViewById(R.id.formName);
        restaurantType.setVisibility(View.GONE);

        if (extraData.getString("formType", "").equals("signUp")) {

            //Raiyan-Form Name sign up or edit account
            formName.setText(extraData.getString("formName", ""));
            Toast.makeText(getApplicationContext(), "Sign Up", Toast.LENGTH_LONG).show();

            acnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //acnType.getSelectedItem().toString().equals("Restaurant")
                    if(position == 2){
                        restaurantType.setVisibility(View.VISIBLE);
                    }
                    if(position == 1){
                        restaurantType.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            btnSignUpConfirm.setOnClickListener(new View.OnClickListener() {
                long acctId;

                //Raiyan-Removed redundant code
                @Override
                public void onClick(View v) {
                    acctId = dbh.addAccount(
                            acnName.getText().toString(),
                            acnType.getSelectedItem().toString(),
                            acnEmail.getText().toString(),
                            acnPass.getText().toString(),
                            acnPhoneNumber.getText().toString(),
                            acnAddress.getText().toString(),
                            acnCity.getSelectedItem().toString()
                    );

                    if (acctId > 0) {
                        Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_LONG).show();
                        Intent intent;
                        //Raiyan-Fixed if logic check bug
                        if (acnType.getSelectedItem().toString().equals("Restaurant")) {
//                            boolean restBool;
                            dbh.addRestaurant(restaurantType.getSelectedItem().toString(), acctId);
                            intent = new Intent(SignUpActivity.this, RestaurantHomeActivity.class);

                        } else {
                            intent = new Intent(SignUpActivity.this, CustomerHomeActivity.class);
                        }
                        intent.putExtra("acctId", acctId);
                        intent.putExtra("acctName", acnName.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add account", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (extraData.getString("formType", "").equals("editAccount")){
//            Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_LONG).show();

//            if(extraData.getString("accountType").equals("Customer")){
                formName.setText(extraData.getString("formName", ""));
//                acnName.setText(extraData.getString("formName", ""));
                acnType.setVisibility(View.GONE);
                acnEmail.setVisibility(View.GONE);
                acnPass.setVisibility(View.GONE);
                acnCity.setVisibility(View.GONE);
                logoSignUp.setVisibility(View.GONE);
                textBrandName.setVisibility(View.GONE);
                btnSignUpConfirm.setText("Update");

                Cursor c = dbh.getAccountInfo(extraData.getLong("accountId"));

                if(c.getCount() > 0){
                    if(c.moveToFirst()){
                        acnName.setText(c.getString(1));
                        acnPhoneNumber.setText(c.getString(5));
                        acnAddress.setText(c.getString(6));

                        btnSignUpConfirm.setOnClickListener(new View.OnClickListener() {
                            long acctId;
                            long accountID = extraData.getLong("accountId");
                            @Override
                            public void onClick(View v) {
                                acctId = dbh.updateAccountInfo(
                                        accountID,
                                        acnName.getText().toString(),
                                        acnPhoneNumber.getText().toString(),
                                        acnAddress.getText().toString()
                                );

                                if (acctId > 0) {
                                    Toast.makeText(getApplicationContext(), "Account updated successfully", Toast.LENGTH_LONG).show();
                                    Intent intent;
                                    //Raiyan-Fixed if logic check bug
                                    if (extraData.getString("accountType").equals("Restaurant")) {
                                        intent = new Intent(SignUpActivity.this, RestaurantHomeActivity.class);
                                    } else {
                                        intent = new Intent(SignUpActivity.this, CustomerHomeActivity.class);
                                    }
                                    intent.putExtra("acctId", acctId);
                                    intent.putExtra("acctName", acnName.getText().toString());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to update account", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
//            }

        }
    }


}