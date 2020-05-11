package com.example.inclass09;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ContactList extends AppCompatActivity implements ContactAdapter.OnAdapterInteractionListener {
    Button btn_new_contact;
    ImageButton btn_logout;
    RecyclerView rv_results;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    String userid;
    ArrayList<ContactData> contactDataArrayList = new ArrayList<>();
    Context context;
    ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        context = ContactList.this;

        btn_new_contact = findViewById(R.id.btn_new_contact);
        btn_logout = findViewById(R.id.btn_logout);
        rv_results = findViewById(R.id.rv_results);

        if(null!= getIntent()) {
            userid = getIntent().getStringExtra("userid");
            myRef = database.getReference(userid);
            Log.d("auth_list:::", "UID" +userid);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactDataArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    contactDataArrayList.add( dataSnapshot1.getValue(ContactData.class));
                }
                if(arrayListSizeCheck(contactDataArrayList)) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);
                    rv_results.setLayoutManager(layoutManager);
                    contactAdapter = new ContactAdapter(context, userid, contactDataArrayList);
                    rv_results.setAdapter(contactAdapter);
                }
                Log.d("firebase_list:", "contactDataArrayList is: " + contactDataArrayList.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.", error.toException());
            }
        });

        btn_new_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactList.this, CreateContact.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
                finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ContactList.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean arrayListSizeCheck(ArrayList<ContactData> contactDataArrayList){

        if(null!=contactDataArrayList && contactDataArrayList.size()>0) {

            //tv_no_list.setVisibility(View.INVISIBLE);
            rv_results.setVisibility(View.VISIBLE);
            return true;
        }else {
            //tv_no_list.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    @Override
    public void notifyDataset() {
        contactAdapter.notifyDataSetChanged();
    }
}
