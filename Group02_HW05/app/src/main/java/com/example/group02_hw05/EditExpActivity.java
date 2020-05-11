package com.example.group02_hw05;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class EditExpActivity extends AppCompatActivity {

    EditText et_ename, et_ecost;
    TextView tv_edate;
    Button btn_eimage, btn_save_exp;
    ImageView iv_ereceipt;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenseDataArrayList");
    ExpenseData expenseData;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri selectedImage;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exp);

        et_ename =  findViewById(R.id.et_ename);
        et_ecost = findViewById(R.id.et_ecost);
        tv_edate =  findViewById(R.id.tv_edate);
        btn_eimage = findViewById(R.id.btn_eimage);
        btn_save_exp = findViewById(R.id.btn_save_exp);
        iv_ereceipt = findViewById(R.id.iv_ereceipt);

        if(null!=getIntent() && null!= getIntent().getSerializableExtra("expenseData")) {

            expenseData = (ExpenseData) getIntent().getSerializableExtra("expenseData");

            Picasso.get().load(expenseData.receipt).into(iv_ereceipt);
            et_ename.setText(expenseData.name);
            tv_edate.setText(expenseData.date);
            et_ecost.setText(expenseData.cost);

            tv_edate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(EditExpActivity.this,android.R.style.Theme_Holo_Light_Dialog, mDateSetListener,year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }
            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month+1;
                    String date = month +"/"+ dayOfMonth +"/"+year;
                    tv_edate.setText(date);
                }
            };


            btn_eimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 0);
                }
            });



            btn_save_exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    expenseData.name = et_ename.getText().toString();
                    expenseData.cost = et_ecost.getText().toString();
                    expenseData.date = tv_edate.getText().toString();
                    if(null != downloadUrl) {
                        expenseData.receipt = downloadUrl;
                    }
                    onEditExpense(expenseData);

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    uploadImage();
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    uploadImage();
                }
                break;
        }
    }

    private void uploadImage() {

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

            final String path = "receipts/"+ UUID.randomUUID().toString();
            final StorageReference storageReference = storage.getReference(path);

            btn_eimage.setEnabled(false);
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
                            btn_eimage.setEnabled(true);
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                downloadUrl = downloadUri.toString();
                                Picasso.get().load(downloadUri).into(iv_ereceipt);
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
                            Toast.makeText(EditExpActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void onEditExpense(ExpenseData expenseData){
        myRef.child(expenseData.getFirebaseKey()).setValue(expenseData);

        finish();
    }
}
