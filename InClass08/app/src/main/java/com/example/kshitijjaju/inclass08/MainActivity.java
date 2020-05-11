package com.example.kshitijjaju.inclass08;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.OnFragmentInteractionListener , AddExpenseFragment.OnFragmentInteractionListener, ShowExpenseFragment.OnFragmentInteractionListener, EditExpenseFragment.OnFragmentInteractionListener {

    Context context;
    LinearLayout linlyt_container;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenseDataArrayList");
   // Map<String, ExpenseData> firebaseHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        linlyt_container = findViewById(R.id.linlyt_container);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.linlyt_container,new ExpenseAppFragment(),"ExpenseAppFragment")
                .commit();
    }

    @Override
    public void addButtonClick(boolean clicked) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.linlyt_container,new AddExpenseFragment(),"AddExpenseFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void itemLongClick(ExpenseData expenseData, String firebaseKey) {
        myRef.child(firebaseKey).removeValue();
        //myRef.child(firebaseKey).setValue(null);
    }

    @Override
    public void itemClick(final ExpenseData expenseData) {

        Fragment showExpenseFragment = new ShowExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ExpenseData", expenseData);
        showExpenseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.linlyt_container, showExpenseFragment,"ShowExpenseFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAddExpClick(String name, String amount,String category) {
        ExpenseData expenseData = new ExpenseData();
        expenseData.name = name;
        expenseData.amount= amount;
        expenseData.category = category;
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String date = formatter.format(today);
        expenseData.date = date;
        String keyName = myRef.push().getKey();
        expenseData.setFirbaseKey(keyName);
        myRef.child(keyName).setValue(expenseData);
        //myRef.push().setValue(expenseData);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    expenseDataArrayList.add( dataSnapshot1.getValue(ExpenseData.class));
               }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.", error.toException());
            }
        });
        Fragment fragment = (ExpenseAppFragment) getSupportFragmentManager().findFragmentByTag("ExpenseAppFragment");
        if(null!=expenseDataArrayList && expenseDataArrayList.size()>0) {
            ((ExpenseAppFragment) fragment).expenseListResult(expenseDataArrayList);
        }
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            Log.d("back_pressed1:::","stack empty");
        }

    }

    @Override
    public void onCancelClick() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            Log.d("back_pressed2:::","stack empty");
        }
    }

    @Override
    public void onCloseButtonClick() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            Log.d("back_pressed3:::","stack empty");
        }
    }

    @Override
    public void onEditButtonClick(ExpenseData expenseData) {

        Fragment editExpenseFragment = new EditExpenseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ExpenseData", expenseData);
        editExpenseFragment.setArguments(bundle);
        Log.d("expenseData22",expenseData.toString());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.linlyt_container, editExpenseFragment,"EditExpenseFragment")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onEditCancelClick() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            Log.d("back_pressed4:::","stack empty");
        }
    }

    @Override
    public void onEditSaveClick(ExpenseData expenseData) {
        myRef.child(expenseData.getFirbaseKey()).setValue(expenseData);
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            if(getSupportFragmentManager().getBackStackEntryCount()>0){
                getSupportFragmentManager().popBackStack();
            }else{
                Log.d("back_pressed5:::","stack empty");
            }
        }else{
            Log.d("back_pressed6:::","stack empty");
        }
    }
}

