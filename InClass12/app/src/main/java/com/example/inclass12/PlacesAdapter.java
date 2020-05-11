package com.example.inclass12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder>{

    static Context context;
    ArrayList<PlacesData> placesDataArrayList;

    public PlacesAdapter(Context context,  ArrayList<PlacesData> placesDataArrayList) {
        this.context = context;
        this.placesDataArrayList = placesDataArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        PlacesData placesData = placesDataArrayList.get(i);

        myViewHolder.tv_city.setText(placesData.placetitle);
        myViewHolder.placesData = placesData;

    }

    @Override
    public int getItemCount() {
        if(null!= placesDataArrayList) {
            return placesDataArrayList.size();
        }else{
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_city;
        PlacesData placesData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
        }
    }
}
