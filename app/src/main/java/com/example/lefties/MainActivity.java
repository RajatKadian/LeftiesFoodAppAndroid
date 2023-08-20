package com.example.lefties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DBHelper dbh;
    long acctId;
    String acctName;
    String accountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbh = new DBHelper(this);
        seedTable();

        EditText userEmail = findViewById(R.id.userEmail);
        EditText userPass = findViewById(R.id.userPass);
        Button btnLogin = findViewById(R.id.loginBtn);
        Button btnSignUp = findViewById(R.id.btnGoSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor c = dbh.viewDataAccount(userEmail.getText().toString(), userPass.getText().toString());
//                StringBuilder str = new StringBuilder();
                if(c.getCount() > 0){
                    if (c.moveToFirst()){
                        accountType = c.getString(2);
                        //Raiyan-Fixed bug, email was being extracted instead of name
                        acctName = c.getString(1);
                        acctId = Long.parseLong(c.getString(0));
                        if(accountType.equals("Customer")){
                            Toast.makeText(MainActivity.this, "Welcome " + c.getString(1), Toast.LENGTH_SHORT).show();
                            goToCustomerHome()
;                        } else {
                            Toast.makeText(MainActivity.this, "Welcome " + c.getString(1), Toast.LENGTH_SHORT).show();
                            goToRestoHome();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter valid a Email or Password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpPage();
            }
        });
    }

    public void goToRestoHome()
    {
        //Raiyan-Keep user logged in with shared preferences
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        Intent i = new Intent(getApplicationContext(),RestaurantHomeActivity.class);
        //Shared Preferences
        editor.putBoolean("isLoggedIn", true);
        editor.putLong("acctID", acctId);
        editor.putString("acctName", acctName);
        editor.putString("accountType", accountType);
        editor.apply();
        i.putExtra("acctId", acctId);
        i.putExtra("accName", acctName);
        startActivity(i);
    }

    public void goToSignUpPage() {
        Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
        i.putExtra("formName", "Sign Up");
        i.putExtra("formType", "signUp");
        startActivity(i);
    }

    public void goToCustomerHome()
    {

        //Raiyan-Keep user logged in with shared preferences
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        Intent i = new Intent(getApplicationContext(), CustomerHomeActivity.class);
        //Shared Preferences
        editor.putBoolean("isLoggedIn", true);
        editor.putLong("acctID", acctId);
        editor.putString("acctName", acctName);
        editor.putString("accountType", accountType);
        editor.apply();
        i.putExtra("acctId", acctId);
        i.putExtra("accName", acctName);
        startActivity(i);
    }

    // Macci - Populate with data. If "Golden Star" restaurant does not exist, populate the tables
    public void seedTable(){
        Cursor c = dbh.viewAccountByName("Golden Star");
        StringBuilder str = new StringBuilder();
        if(c.getCount() == 0){
            DBSeeder dbs = new DBSeeder(dbh);
            dbs.seedTables();
        }
    }
}