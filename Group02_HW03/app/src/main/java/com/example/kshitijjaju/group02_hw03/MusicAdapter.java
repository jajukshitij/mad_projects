package com.example.kshitijjaju.group02_hw03;

/*Homework 03
 * MusicAdapter
 * Kshitij Jaju
 * Smruthi Rajagopal*/

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private ArrayList<MusicData> musicDataArrayList;
    static Context activity;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_track,tv_price,tv_artist,tv_date;
        MusicData musicData;
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_track = itemView.findViewById(R.id.tv_track);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_artist = itemView.findViewById(R.id.tv_artist);
            tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,DetailsActivity.class);
                    intent.putExtra("musicData",musicData);
                    activity.startActivity(intent);
                }
            });
        }
    }

    public MusicAdapter(Context activity,ArrayList<MusicData> musicDataArrayList) {
        this.musicDataArrayList = musicDataArrayList;
        this.activity = activity;

    }

    @NonNull
    @Override
    public MusicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MyViewHolder myViewHolder, int i) {

        MusicData musicData = musicDataArrayList.get(i);
        myViewHolder.tv_track.setText("Track: "+musicData.trackName);
        myViewHolder.tv_artist.setText("Artist: "+musicData.artistName);
        SimpleDateFormat sd2 = new SimpleDateFormat("MM-dd-yyyy");
        String newDate = sd2.format(musicData.date);
        myViewHolder.tv_date.setText("Date: "+newDate);
        myViewHolder.tv_price.setText("Price: "+musicData.trackPrice+" $");
        myViewHolder.musicData = musicData;
    }

    @Override
    public int getItemCount() {
        if(null!= musicDataArrayList) {
            return musicDataArrayList.size();
        }else{
            return 0;
        }
    }
}
