package com.example.kshitijjaju.inclass07_group02;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<ExpenseData> {
    public ExpenseAdapter(@NonNull Context context, int resource, @NonNull List<ExpenseData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExpenseData exp=getItem(position);
        ViewHolder viewHolder;
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvexpname = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvexpamnt = (TextView)convertView.findViewById(R.id.tv_amount);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvexpname.setText(exp.name);
        viewHolder.tvexpamnt.setText(exp.amount);
        return convertView;
    }

    private static class ViewHolder{
        TextView tvexpname,tvexpamnt;
    }
}
