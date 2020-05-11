package com.example.group02_hw05;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {
    static Context context;
    ArrayList<ExpenseData> expenseDataArrayList;

    public ExpenseAdapter(Context context, ArrayList<ExpenseData> expenseDataArrayList) {
        this.context = context;
        this.expenseDataArrayList = expenseDataArrayList;
    }

    @NonNull
    @Override
    public ExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.MyViewHolder myViewHolder, int i) {
        ExpenseData expenseData = expenseDataArrayList.get(i);
        myViewHolder.tv_rname.setText(expenseData.name);
        myViewHolder.tv_rcost.setText(expenseData.cost);
        myViewHolder.tv_rdate.setText(expenseData.date);
        myViewHolder.expenseData = expenseData;

    }

    @Override
    public int getItemCount() {
        if(null!= expenseDataArrayList) {
            return expenseDataArrayList.size();
        }else{
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_rname, tv_rcost, tv_rdate;
        ImageView iv_edit;
        ExpenseData expenseData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_rname = itemView.findViewById(R.id.tv_rname);
            tv_rcost = itemView.findViewById(R.id.tv_rcost);
            tv_rdate = itemView.findViewById(R.id.tv_rdate);
            iv_edit = itemView.findViewById(R.id.iv_edit);

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,EditExpActivity.class);
                    intent.putExtra("expenseData",expenseData);
                    context.startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DisplayExpActivity.class);
                    intent.putExtra("expenseData",expenseData);
                    context.startActivity(intent);
                }
            });
        }
    }
}
