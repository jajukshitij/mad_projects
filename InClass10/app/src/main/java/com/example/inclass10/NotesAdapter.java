package com.example.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotesAdapter  extends  RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    static Context context;
    private static OnAdapterInteractionListener mListener;
    ArrayList<NotesData> notesDataArrayList;
    static String token;

    public NotesAdapter(Context context, String token, ArrayList<NotesData> notesDataArrayList) {
        this.context = context;
        this.notesDataArrayList = notesDataArrayList;
        this.token = token;
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
        NotesData notesData = notesDataArrayList.get(i);

        myViewHolder.tv_note.setText(notesData.text);
        myViewHolder.notesData = notesData;
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

        TextView tv_note;
        ImageButton btn_delete;
        NotesData notesData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_note = itemView.findViewById(R.id.tv_note);
            btn_delete = itemView.findViewById(R.id.btn_delete);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DisplayNote.class);
                    intent.putExtra("msgId", notesData.id);
                    //intent.putExtra("token", token);
                    context.startActivity(intent);
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OkHttpClient client = new OkHttpClient();

                    /*RequestBody formBody = new FormBody.Builder()
                            .add("msgId",notesData.id)
                            .build();*/

                    Request request = null;
                    try {
                        request = new Request.Builder()
                                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId="+ URLEncoder.encode(notesData.id, "UTF-8"))
                                .header("x-access-token",token)
                                .addHeader("Content-Type","application/x-www-form-urlencoded")
                                //.post(formBody)
                                .build();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("RESPONSE_Async:::","onFailure");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("RESPONSE_Async:::",response.body().string());
                            mListener.notifyDataset();
                        }
                    });
                }
            });
        }
    }

    public interface OnAdapterInteractionListener {
        void notifyDataset();
    }
}
