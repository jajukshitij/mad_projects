package com.example.group02_hw06;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatRoom extends AppCompatActivity implements ChatAdapter.OnAdapterInteractionListener{

    private FirebaseAuth mAuth;
    TextView tv_username;
    RecyclerView rv_results;
    EditText et_message;
    ImageButton btn_addImage,btn_sendMessage,btn_logout;
    Uri selectedImage;
    String downloadUrl, userName, userId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("chatDataArrayList");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<ChatData> chatDataArrayList = new ArrayList<>();
    Context context;
    ChatAdapter chatAdapter;
    String firstName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        context = ChatRoom.this;
        setTitle("Chat Room");

        tv_username = findViewById(R.id.tv_username);
        rv_results = findViewById(R.id.rv_results);
        et_message = findViewById(R.id.et_message);
        btn_addImage = findViewById(R.id.btn_addImage);
        btn_sendMessage = findViewById(R.id. btn_sendMessage);
        btn_logout = findViewById(R.id.btn_logout);
        mAuth = FirebaseAuth.getInstance();

        if(null!= getIntent()) {
            userId = getIntent().getStringExtra("userid");
        }

        if(null!= mAuth) {
            FirebaseUser user = mAuth.getCurrentUser();
            if(null!=user){
                userName = user.getDisplayName();
                firstName = userName.split("\\s+")[0];
                tv_username.setText(userName);
                Log.d("username::"," "+user.getDisplayName());
                Log.d("firstName::"," "+firstName);
            }
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatDataArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    chatDataArrayList.add( dataSnapshot1.getValue(ChatData.class));
                }
                if(arrayListSizeCheck(chatDataArrayList)) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);
                    rv_results.setLayoutManager(layoutManager);
                    chatAdapter = new ChatAdapter(context, userId, chatDataArrayList);
                    rv_results.setAdapter(chatAdapter);
                }
                Log.d("firebase_list:", "contactDataArrayList is: " + chatDataArrayList.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.", error.toException());
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ChatRoom.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 0);
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date today = Calendar.getInstance().getTime();

                ChatData chatData = new ChatData();
                chatData.chatMessage = et_message.getText().toString();
                chatData.senderName = firstName;
                chatData.imageUrl = downloadUrl;
                chatData.userId = userId;
                chatData.chatTime = today;
                onAddMessage(chatData);
                et_message.setText("");
                btn_addImage.setImageDrawable(getResources().getDrawable(R.drawable.addimage));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    uploadImage(selectedImage);
                }
                break;
        }
    }

    private void uploadImage(Uri selectedImage) {

        Bitmap bitmap = null;
        try {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            //imageview.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte[] byteArray = baos.toByteArray();

            final String path = "receipts/"+ myRef.push().getKey();
            final StorageReference storageReference = storage.getReference(path);
            /*StorageMetadata storageMetadata = new StorageMetadata.Builder().setCustomMetadata("name",et_name.getText().toString())
                .build();*/
            btn_addImage.setEnabled(false);
            final UploadTask uploadTask = storageReference.putBytes(byteArray);

            uploadTask.continueWithTask (new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }// Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            })

                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            progressDialog.dismiss();
                            btn_addImage.setEnabled(true);
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                downloadUrl = downloadUri.toString();
                                Picasso.get().load(downloadUri).into(btn_addImage);
                                Log.d("firebase_storage_com2",downloadUri.toString());
                            } else {
                                // Handle failures
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.d("firebase_storage_URL","Fail");
                            Toast.makeText(ChatRoom.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                            .getTotalByteCount());
                    Log.d("firebase_storage_URLp",""+progress);
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onAddMessage(ChatData chatData){
        String keyName = myRef.push().getKey();
        chatData.setFirebaseKey(keyName);
        myRef.child(keyName).setValue(chatData);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatDataArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    chatDataArrayList.add( dataSnapshot1.getValue(ChatData.class));
                }
                chatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.", error.toException());
            }
        });
    }

    public boolean arrayListSizeCheck(ArrayList<ChatData> chatDataArrayList){

        if(null!=chatDataArrayList && chatDataArrayList.size()>0) {

            //tv_no_list.setVisibility(View.INVISIBLE);
            rv_results.setVisibility(View.VISIBLE);
            return true;
        }else {
            //tv_no_list.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    @Override
    public void notifyDataset() {
        chatAdapter.notifyDataSetChanged();
    }
}
