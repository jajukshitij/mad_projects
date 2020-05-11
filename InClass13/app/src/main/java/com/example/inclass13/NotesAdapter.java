package com.example.inclass13;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder>{

    static Context context;
    ArrayList<NotesData> notesDataArrayList;
    private static OnInteractionListener mListener;

    public NotesAdapter(Context context,ArrayList<NotesData> notesDataArrayList) {
        this.notesDataArrayList = notesDataArrayList;
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
        NotesData notesData = notesDataArrayList.get(i);

    }

    @Override
    public int getItemCount() {
        if(null!= notesDataArrayList) {
            return notesDataArrayList.size();
        }else{
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_note, tv_priority, tv_time;
        CheckBox checkBox;
        NotesData notesData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_priority = itemView.findViewById(R.id.tv_priority);
            tv_time = itemView.findViewById(R.id.tv_time);
            checkBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });

        }
    }

    public interface OnInteractionListener{
        void OnLongPress();
        void notedChecked();
    }
}
