package com.example.group02_hw05;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class AddExpActivity extends AppCompatActivity {

    EditText et_name, et_cost;
    TextView tv_date;
    Button btn_image, btn_add_exp;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenseDataArrayList");
    DatePickerDialog.OnDateSetListener mDateSetListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Uri selectedImage,photoURI;
    String downloadUrl;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exp);

        et_name =  findViewById(R.id.et_name);
        et_cost = findViewById(R.id.et_cost);
        tv_date =  findViewById(R.id.tv_date);
        btn_image = findViewById(R.id.btn_image);
        btn_add_exp = findViewById(R.id.btn_add_exp);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddExpActivity.this,android.R.style.Theme_Holo_Light_Dialog, mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = month +"/"+ dayOfMonth +"/"+year;
                tv_date.setText(date);
            }
        };

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        photoURI = FileProvider.getUriForFile(AddExpActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePicture, 1);

                    }
                }*/

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 0);

            }
        });

        btn_add_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseData expenseData =  new ExpenseData();
                expenseData.name = et_name.getText().toString();
                expenseData.cost = et_cost.getText().toString();
                expenseData.date = tv_date.getText().toString();
                expenseData.receipt = downloadUrl;

                onAddExpense(expenseData);
                finish();
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
            case 1:
                if(resultCode == RESULT_OK){
                    selectedImage = data.getData();
                    uploadImage(photoURI);
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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

            btn_image.setEnabled(false);
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
                            btn_image.setEnabled(true);
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                downloadUrl = downloadUri.toString();
                                //Picasso.get().load(downloadUri).into(iv_areceipt);
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
                            Toast.makeText(AddExpActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void onAddExpense(ExpenseData expenseData){
        String keyName = myRef.push().getKey();
        expenseData.setFirebaseKey(keyName);
        myRef.child(keyName).setValue(expenseData);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    expenseDataArrayList.add( dataSnapshot1.getValue(ExpenseData.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.", error.toException());
            }
        });
    }
}
