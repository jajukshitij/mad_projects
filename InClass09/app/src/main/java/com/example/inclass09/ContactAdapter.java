package com.example.inclass09;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    static Context context;
    ArrayList<ContactData> contactDataArrayList;
    String userid;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef;
    private static OnAdapterInteractionListener mListener;


    public ContactAdapter(Context context, String userid, ArrayList<ContactData> contactDataArrayList) {
        this.contactDataArrayList = contactDataArrayList;
        this.context = context;
        this.userid = userid;
        myRef = database.getReference(userid);
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        if (context instanceof OnAdapterInteractionListener) {
            mListener = (OnAdapterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAdapterInteractionListener");
        }

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder myViewHolder, int i) {
        ContactData contactData = contactDataArrayList.get(i);
        myViewHolder.tv_name.setText(contactData.name);
        myViewHolder.tv_phone.setText(contactData.phone);
        myViewHolder.tv_email.setText(contactData.email);
        Picasso.get().load(contactData.imageUrl).into(myViewHolder.iv_contact);
        myViewHolder.contactData =contactData;

    }

    @Override
    public int getItemCount() {
        if(null!= contactDataArrayList) {
            return contactDataArrayList.size();
        }else{
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_phone, tv_email;
        ImageView iv_contact;
        ContactData contactData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_email = itemView.findViewById(R.id.tv_email);
            iv_contact = itemView.findViewById(R.id.iv_contact);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myRef.child(contactData.firebaseKey).removeValue();
                    mListener.notifyDataset();
                    return true;
                }
            });

        }
    }

    public interface OnAdapterInteractionListener {
        void notifyDataset();
    }
}
