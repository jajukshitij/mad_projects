package com.example.kshitijjaju.hw04_group02;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    ArrayList<String> ingredientsList;
     ArrayList<String> recipes;
     int i=0;
   // Context context;

    public IngredientsAdapter(ArrayList<String> values) {
        this.ingredientsList = new ArrayList<String>();
        this.recipes=values;
        this.i=0;
        //this.context = context;
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
        String ing=myViewHolder.et_item_name.getText().toString();
        ingredientsList.add(ing);

    }

    @Override
    public int getItemCount() {
        if(null!= ingredientsList) {
            return this.recipes.size();
        }else{
            return 0;
        }
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        EditText et_item_name;
        FloatingActionButton actionButton;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            et_item_name = itemView.findViewById(R.id.et_item_name);
            actionButton = itemView.findViewById(R.id.floatingactionbutton);
            actionButton.setTag(R.drawable.add);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(R.drawable.add == (Integer) actionButton.getTag()) {
                        actionButton.setImageResource(R.drawable.remove);
                        actionButton.setTag(R.drawable.remove);
                        if (i == 4) {
                            Toast.makeText(v.getContext(), "No more ingredients to add", Toast.LENGTH_SHORT).show();
                        } else {
                            i++;
                            recipes.add("Add EditText");
                            int position = getAdapterPosition();
                            notifyItemInserted(position);
                        }
                    }
                    else {
                        actionButton.setImageResource(R.drawable.add);
                        actionButton.setTag(R.drawable.add);
                        if (i >= 1) {
                            i--;
                            if(itemView.hasFocus()){
                                itemView.clearFocus();
                            }
                            et_item_name.setText("");
                            int position = getAdapterPosition();
                            recipes.remove(position);
                            notifyItemRemoved(position);
                        } else {
                            et_item_name.setText("");
                        }
                    }
                }

            });


        }

    }
}



