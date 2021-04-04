package com.peart.fixing_qr_code_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGSaver;
/**
 * Scanner contains the logic to scan and save a CONTACT QR Code to the phone
**/

//developed by Matthew Peart
public class Scanner extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannerView;
    ContactsContract.Contacts myContact;
    String resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {//when the scanner decodes a QR

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { //save result as a contact

                        resultData = result.getText().toString();

                        //if the result doesn't contain the proper information display a toast to the user letting them know
                        if (!(resultData.contains("N:") && resultData.contains("ADR:") && resultData.contains("TEL:") && resultData.contains("EMAIL:"))){
                            Toast.makeText(Scanner.this, "Not a valid contact", Toast.LENGTH_LONG).show();
                        } else {
                            //parse out the VCARD here
                            //To properly add the contact to the phone we have to parse through a valid VCARD and find the user's information and save it to an intent.
                            //then save the intent as a contact to the system

                            //Indexes representing where the correct information is contained in the VCARD
                            int nameBegin, nameEnd, adrBegin, adrEnd, telBegin, telEnd, emailBegin, emailEnd;

                            nameBegin = resultData.indexOf("N:",10); //Starts at index 10 to skip the "BEGIN:VCARD" tag
                            nameEnd = resultData.indexOf(";",nameBegin);
                            String name = resultData.substring(nameBegin + 2, nameEnd); // +2 to skip the "N:" and parse out only the name

                            adrBegin = resultData.indexOf("ADR:");
                            adrEnd = resultData.indexOf("TEL:");
                            String adr = resultData.substring(adrBegin + 4, adrEnd); // +4 to skip the "ADR:" and parse out only the address

                            telBegin = resultData.indexOf("TEL:");
                            telEnd = resultData.indexOf("EMAIL:");
                            String tel = resultData.substring(telBegin + 4, telEnd); // +4 to skip the "TEL:" and parse out only the phone number

                            emailBegin = resultData.indexOf("EMAIL:");
                            emailEnd = resultData.indexOf("END:");
                            String email = resultData.substring(emailBegin + 6, emailEnd); // +4 to skip the "EMAIL:" and parse out only the email

                            //insert all the strings into the intent
                            Intent intent = new Intent(Intent.ACTION_INSERT);
                            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                            intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, adr);
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, tel);
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);

                            if (intent.resolveActivity(getPackageManager()) != null){
                                startActivity(intent);
                            } else {
                                Toast.makeText(Scanner.this, "Something went wrong", Toast.LENGTH_LONG).show(); //Hopefully we never see this
                            }

                        }
                    }
                });
            }
        });

        //keeps the scanner going if it doesn't find anything
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });

    }

    //resumes with a tap
    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}