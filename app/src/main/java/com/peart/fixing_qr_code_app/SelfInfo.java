package com.peart.fixing_qr_code_app;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * The SelfInfo activity allows the user to save their personal contact data.
 * It also fills a bundle with the correct information for a CONTACT QR Code
 **/

//Developed by Matthew Peart with some help by Denzil Museruka
public class SelfInfo extends AppCompatActivity {

    //these are used to save data to the phone
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";
    public static final String ADDRESS = "ADDRESS";
    public static final String EMAIL = "EMAIL";

    //used locally for loading data and for getting info from the activity
    private EditText name, email, phone, address;
    private String selfName, selfAddress, selfNumber, selfEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_info);

        Button saveExit = findViewById(R.id.selfSave);

        name    = findViewById(R.id.selfName);
        phone   = findViewById(R.id.selfNumber);
        email   = findViewById(R.id.selfEmail);
        address = findViewById(R.id.selfAddress);

        saveExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this is the intent that is used to move the bundle from here to main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                selfName    = name.getText().toString();
                selfAddress = address.getText().toString();
                selfNumber  = phone.getText().toString();
                selfEmail   = email.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("name", selfName);
                bundle.putString("postal", selfAddress);
                bundle.putString("phone", selfNumber);
                bundle.putString("email", selfEmail);
                intent.putExtras(bundle);
                saveData();
                startActivity(intent);
            }
        });

        //Loads previously saved data from the preferences
        loadData();

        //Updates the application so the user can see the loaded data
        updateViews();
    }

    //saves the personal contact information
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NAME, name.getText().toString());
        editor.putString(PHONE, phone.getText().toString());
        editor.putString(ADDRESS, address.getText().toString());
        editor.putString(EMAIL, email.getText().toString());

        editor.apply();

        //lets the user know if it was successful
        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();
    }

    //loads the correct data
    public  void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        selfName =  sharedPreferences.getString(NAME, "");
        selfAddress = sharedPreferences.getString(ADDRESS, "");
        selfNumber = sharedPreferences.getString(PHONE, "");
        selfEmail = sharedPreferences.getString(EMAIL, "");
    }

    //displays the data
    public void updateViews() {
        name.setText(selfName);
        phone.setText(selfNumber);
        address.setText(selfAddress);
        email.setText(selfEmail);
    }
}