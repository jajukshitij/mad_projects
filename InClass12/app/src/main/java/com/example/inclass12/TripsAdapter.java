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

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.MyViewHolder>{

    static Context context;
    ArrayList<CityData> cityDataArrayList;
    private static TripsAdapter.OnInteractionListener mListener;

    public TripsAdapter(Context context, ArrayList<CityData> cityDataArrayList) {
        this.cityDataArrayList = cityDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        if (context instanceof TripsAdapter.OnInteractionListener) {
            mListener = (TripsAdapter.OnInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        CityData cityData = cityDataArrayList.get(i);
        myViewHolder.tv_city.setText(cityData.tripName);
        myViewHolder.tv_date.setText(cityData.date);
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

        TextView tv_city, tv_date;
        CityData cityData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TripDetailsActivity.class);
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
