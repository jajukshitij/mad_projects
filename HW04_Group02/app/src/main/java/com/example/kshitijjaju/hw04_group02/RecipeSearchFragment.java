package com.example.kshitijjaju.hw04_group02;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class RecipeSearchFragment extends Fragment {


    Button btn_search;
    EditText et_dish;
    RecyclerView rv_results;
    LinearLayout linlyt_progress;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> ingredientsList;
    ArrayList<String> items = new ArrayList<>();
    boolean flag=false;

    private OnFragmentInteractionListener mListener;

    public RecipeSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);
        btn_search = view.findViewById(R.id.btn_search);
        et_dish = view.findViewById(R.id.et_dish);
        rv_results =  view.findViewById(R.id.rv_results);
        linlyt_progress =  view.findViewById(R.id.linlyt_progress);
        rv_results.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rv_results.setLayoutManager(layoutManager);
        items.add("EditText");
        mAdapter = new IngredientsAdapter(items);
        rv_results.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_dish.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Enter the values", Toast.LENGTH_SHORT).show();
                } else {
                    ingredientsList = new ArrayList<>();
                    for (int i = 0; i < rv_results.getChildCount(); i++) {
                        IngredientsAdapter.MyViewHolder holder = (IngredientsAdapter.MyViewHolder) rv_results.getChildViewHolder(rv_results.getChildAt(i));
                        ingredientsList.add(holder.et_item_name.getText().toString());
                    }
                    mListener.SendValuesToResultFragment(et_dish.getText().toString(), ingredientsList);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        items=new ArrayList<>();
        mAdapter.notifyDataSetChanged();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void SendValuesToResultFragment(String dishName, ArrayList<String> Ingredients);
    }
}
