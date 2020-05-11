package com.example.inclass12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder>{

    static Context context;
    ArrayList<CityData> cityDataArrayList;
    private static OnInteractionListener mListener;

    public CityAdapter(Context context, ArrayList<CityData> cityDataArrayList) {
        this.cityDataArrayList = cityDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        if (context instanceof OnInteractionListener) {
            mListener = (OnInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        CityData cityData = cityDataArrayList.get(i);
        myViewHolder.tv_city.setText(cityData.address);
        myViewHolder.cityData = cityData;

    }

    @Override
    public int getItemCount() {
        if(null!= cityDataArrayList) {
            return cityDataArrayList.size();
        }else{
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_city;
        CityData cityData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CreateTripActivity.class);
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("cityData",cityData);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    mListener.finishActivity();
                }
            });
        }
    }

    public interface OnInteractionListener{
        void finishActivity();

    }
}
