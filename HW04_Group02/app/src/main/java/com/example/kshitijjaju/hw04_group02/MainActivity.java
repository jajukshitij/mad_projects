package com.example.kshitijjaju.hw04_group02;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeSearchFragment.OnFragmentInteractionListener,RecipeResultFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Recipe Puppy");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.linlyt_container,new RecipeSearchFragment(),"RecipeSearchFragment")
                .commit();
    }


    @Override
    public void SendValuesToResultFragment(String DishName, ArrayList<String> Ingredients) {

        RecipeResultFragment fragment = new RecipeResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Dish", DishName);
        bundle.putStringArrayList("Ingredients", Ingredients);
        fragment.setArguments(bundle);
        setTitle("Recipes");
        getSupportFragmentManager().beginTransaction().replace(R.id.linlyt_container,fragment, "Display")
                .addToBackStack("Recipe Result Fragment").commit();
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void onFragmentInteraction() {
        setTitle("Recipe puppy");
        onBackPressed();
    }
}
