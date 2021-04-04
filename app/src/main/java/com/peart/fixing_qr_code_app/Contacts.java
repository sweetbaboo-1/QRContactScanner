package com.peart.fixing_qr_code_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Contains the logic for the Contacts activity which lets the user see a list of their contacts with their associated phone number
 **/

//Developed by Denzil Museruka
public class Contacts extends AppCompatActivity {

    // initialize variable
    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // assign variable
        recyclerView = findViewById(R.id.recycler_view);

        // check permissions
        checkPermission();
    }

    private void checkPermission() {
        // check condition
        if(ContextCompat.checkSelfPermission(Contacts.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // when permission is not granted, request permission
            ActivityCompat.requestPermissions(Contacts.this,new String[]{Manifest.permission.READ_CONTACTS},100);
        }else {
            // when permission is granted
            getContactList();
        }
    }

    private void getContactList() {
        // initialize uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        // sort by ascending
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        // Initialize cursor
        Cursor cursor = getContentResolver().query(uri,null,null,null, sort);

        // check condition
        if(cursor.getCount() > 0) {
            //when count is greater than 0
            while(cursor.moveToNext()){
                // get contact id
                String id = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                ));
                // get contact name
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                ));
                // initialize phone uri
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                // initialize selection
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                // initialize phone cursor
                Cursor phoneCursor = getContentResolver().query(uriPhone,null,selection,new String[]{id},null);

                // check condition
                if(phoneCursor.moveToNext()){
                    // when cursor move to next
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    // initialize contact model
                    ContactModel model = new ContactModel();
                    model.setName(name);
                    model.setNumber(number);

                    arrayList.add(model);
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
        // set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // initialize adapter
        adapter = new MainAdapter(this,arrayList);
        // set adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // when permissions are granted
            getContactList();
        }else {
            // display a toast to say permissions denied
            Toast.makeText(Contacts.this,"Permission Denied.",Toast.LENGTH_SHORT).show();
            // call check permissions
            checkPermission();
        }
    }
}
