package com.example.kshitijjaju.fragmentpractice;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener  {

    LinearLayout container;
    TextView tv_fromF;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        container = findViewById(R.id.container);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,new FirstFragment(),"firstFragment")
                .commit();
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new SecondFragment(),"SecondFragment")
                .addToBackStack(null)
                .commit();*/

        Fragment fragment = (FirstFragment) getSupportFragmentManager().findFragmentByTag("firstFragment");
        ((FirstFragment) fragment).activityToFragment();


        /*ViewPager viewPager = findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new FixedTabsPagerAdapter(context,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);*/


    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(String text) {
        tv_fromF.setText(text);

    }
}
