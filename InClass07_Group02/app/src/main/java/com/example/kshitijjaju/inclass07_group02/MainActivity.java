package com.example.kshitijjaju.inclass07_group02;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.OnFragmentInteractionListener , AddExpenseFragment.OnFragmentInteractionListener, ShowExpenseFragment.OnFragmentInteractionListener {

    Context context;
    LinearLayout linlyt_container;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();

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
    public void itemLongClick(ExpenseData expenseData) {
        expenseDataArrayList.remove(expenseData);
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
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
        String date = formatter.format(today);
        expenseData.date = date;
        expenseDataArrayList.add(expenseData);
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
            Log.d("back_pressed3:::","stack empty");
        }
    }

    @Override
    public void onCloseButtonClick() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            Log.d("back_pressed2:::","stack empty");
        }
    }
}
