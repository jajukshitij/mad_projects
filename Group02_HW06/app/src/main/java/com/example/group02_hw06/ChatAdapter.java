package com.example.group02_hw06;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{

    Context context;
    ArrayList<ChatData> chatDataArrayList;
    private static OnAdapterInteractionListener mListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef;
    String userId;

    public ChatAdapter(Context context, String userId, ArrayList<ChatData> chatDataArrayList) {
        this.context = context;
        this.chatDataArrayList = chatDataArrayList;
        this.userId = userId;
        myRef = database.getReference("chatDataArrayList");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ChatData chatData = chatDataArrayList.get(i);

        if (null!= chatData.imageUrl && !chatData.imageUrl.equals("")) {
            Picasso.get().load(chatData.imageUrl).into(myViewHolder.imageView);
            myViewHolder.imageView.setVisibility(View.VISIBLE);
        }else{
            myViewHolder.imageView.setVisibility(View.GONE);
        }

        /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                String date = formatter.format(today);*/
        PrettyTime p  = new PrettyTime();
        String prettyTime= p.format(chatData.chatTime);
        myViewHolder.tv_sendtime.setText(prettyTime);

        myViewHolder.tv_message.setText(chatData.chatMessage);
        myViewHolder.tv_sender.setText(chatData.senderName);
        myViewHolder.chatData = chatData;

        if(chatData.userId.equals(userId)){
            myViewHolder.btn_delete.setVisibility(View.VISIBLE);
        } else{
            myViewHolder.btn_delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(null!= chatDataArrayList) {
            return chatDataArrayList.size();
        }else{
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_message, tv_sender, tv_sendtime;
        ImageView imageView;
        ImageButton btn_delete;
        ChatData chatData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_sender = itemView.findViewById(R.id.tv_sender);
            tv_sendtime = itemView.findViewById(R.id.tv_sendtime);
            imageView = itemView.findViewById(R.id.imageView);
            btn_delete = itemView.findViewById(R.id.btn_delete);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myRef.child(chatData.firebaseKey).removeValue();
                    mListener.notifyDataset();
                }
            });
        }
    }

    public interface OnAdapterInteractionListener {
        void notifyDataset();
    }

}
