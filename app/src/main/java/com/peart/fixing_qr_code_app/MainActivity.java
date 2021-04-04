package com.peart.fixing_qr_code_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidmads.library.qrgenearator.QRGContents;


/**
 * CONTACT SCANNER APP
 * Developed by Matthew Peart, Steve Evans, Denzil Museruka
 *
 * This application allows a user to store personal contact information and display is as a CONTACT QR Code
 * It also provides A scanner/decoder that will scan and save a CONTACT QR Code to the user's phone as a
 * contact.
 *
 * TO THE PERSON GRADING THIS ASSIGNMENT: Please take note of the "Developed by _____" comments.
 * The contributions to this assignment were hugely varied
 * and everyone was most comfortable with this method for credits.
**/


//Developed by Matthew Peart
public class MainActivity extends AppCompatActivity {

    //Make our buttons
    Button generateBtn, scanBtn, contactsBtn, selfInfoBtn;
    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize values
        selfInfoBtn = findViewById(R.id.editSelf);
        generateBtn = findViewById(R.id.genButton);
        scanBtn = findViewById(R.id.scanBtn);
        qrImage = findViewById(R.id.qrPlaceHolder);
        contactsBtn = findViewById(R.id.contactsBtn);

        //Takes us to the SelfInfo activity for the user to input their personal contact information
        selfInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SelfInfo.class));
            }
        });


        // Generates the QR code
        generateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Get the data from the selfInfo activity
                Intent intent = getIntent();
                Bundle contact = intent.getExtras();

                //The encoder needs a bundle, the type CONTACT and the bundle must contain the android.Contacts.Contact keys name = name, address = postal
                myEncoder qrgEncoder = new myEncoder(null, contact, QRGContents.Type.CONTACT,500);
                Bitmap qrBits = qrgEncoder.getBitmap();
                qrImage.setImageBitmap(qrBits);
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Scanner.class));
            }
        });

        //Developed by Denzil Museruka
        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Contacts.class));
            }
        });
    }
}

