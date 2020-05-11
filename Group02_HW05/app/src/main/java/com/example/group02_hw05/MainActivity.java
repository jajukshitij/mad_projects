package com.example.group02_hw05;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    Context context;
    FloatingActionButton fab;
    RecyclerView rv_results;
    TextView tv_total,tv_no_list;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenseDataArrayList");
    boolean cost,date;
    ExpenseAdapter expenseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        fab = findViewById(R.id.fab);
        rv_results = findViewById(R.id.rv_results);
        tv_total = findViewById(R.id.tv_total);
        tv_no_list = findViewById(R.id.tv_no_list);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expenseDataArrayList.clear();
                int totalExpense = 0;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    expenseDataArrayList.add( dataSnapshot1.getValue(ExpenseData.class));
                    totalExpense = totalExpense + Integer.parseInt(dataSnapshot1.getValue(ExpenseData.class).cost);
                }
                if(arrayListSizeCheck(expenseDataArrayList)) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);
                    rv_results.setLayoutManager(layoutManager);
                    Collections.sort(expenseDataArrayList,new ExpenseListSort());
                    expenseAdapter = new ExpenseAdapter(context, expenseDataArrayList);
                    rv_results.setAdapter(expenseAdapter);
                }
                tv_total.setText("$ "+totalExpense);
                Log.d("firebase_frag_expense:", "expenseDataArrayList is: " + totalExpense);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.", error.toException());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListener.onFabClick();
                Intent intent = new Intent(MainActivity.this,AddExpActivity.class);
                startActivity(intent);
            }
        });
    }



    class ExpenseListSort implements Comparator<ExpenseData> {
        @Override
        public int compare(ExpenseData o1, ExpenseData o2) {
            if(cost) {
                cost = false;
                return o1.cost.compareTo(o2.cost);
            }else {
                date = false;
                return o2.date.compareTo(o1.date);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.mi_cost:
                cost = true;
                Collections.sort(expenseDataArrayList,new ExpenseListSort());
                expenseAdapter.notifyDataSetChanged();
                break;
            case R.id.mi_date:
                date = true;
                Collections.sort(expenseDataArrayList,new ExpenseListSort());
                expenseAdapter.notifyDataSetChanged();
                break;
            case R.id.mi_reset:
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                for(int i=0;i<expenseDataArrayList.size();i++){
                    StorageReference receiptsRef = storageRef.child("receipts/" + expenseDataArrayList.get(i).firebaseKey);
                    receiptsRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }
                myRef.removeValue();
                break;
        }


        return true;
    }


    public boolean arrayListSizeCheck(ArrayList<ExpenseData> expenseDataArrayList){

        if(null!=expenseDataArrayList && expenseDataArrayList.size()>0) {

            tv_no_list.setVisibility(View.INVISIBLE);
            rv_results.setVisibility(View.VISIBLE);
            return true;
        }else {
            tv_no_list.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
            return false;
        }
    }
}
