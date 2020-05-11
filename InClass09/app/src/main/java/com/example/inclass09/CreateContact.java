package com.example.inclass09;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.AUTHORITY;
import static android.provider.Telephony.Mms.Part.FILENAME;

public class CreateContact extends AppCompatActivity {

    EditText et_name,et_email, et_phn;
    ImageView iv_image;
    Button btn_submit;
    String userid;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Uri selectedImage,photoURI;
    String downloadUrl;
    boolean cameraImage;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_phn = findViewById(R.id.et_phn);
        iv_image = findViewById(R.id.iv_image);
        btn_submit = findViewById(R.id.btn_submit);

        Intent intent = getIntent();
        if (null!= intent){
            userid = intent.getStringExtra("userid");
            myRef = database.getReference(userid);
            Log.d("auth_create:::", "UID" +userid);
        }


        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                        photoURI = FileProvider.getUriForFile(CreateContact.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePicture, 0);

                    }
                }
                cameraImage = true;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactData contactData = new ContactData();
                contactData.name = et_name.getText().toString();
                contactData.email = et_email.getText().toString();
                contactData.phone = et_phn.getText().toString();
                if(cameraImage) {
                    contactData.imageUrl = downloadUrl;
                }else{
                    Uri imagePath = Uri.parse("android.resource://com.example.inclass09/" + R.drawable.baseline_photo_camera_black_36dp);
                    selectedImage = imagePath;
                    uploadImage(selectedImage);
                    contactData.imageUrl = downloadUrl;
                }
                onAddContact(contactData);

                /*Intent intent = new Intent(CreateContact.this,ContactList.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
                finish();*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            //Bundle extras = data.getExtras();
            Log.d("firebase_uri2",":::"+photoURI);
            uploadImage(photoURI);
            //uploadImage(selectedImage);
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
            Log.d("firebase_uri1",":::"+selectedImage);
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            //imageview.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte[] byteArray = baos.toByteArray();

            final String path = "receipts/"+ myRef.push().getKey();
            final StorageReference storageReference = storage.getReference(path);

            //btn_image.setEnabled(false);
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
                           // btn_image.setEnabled(true);
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                downloadUrl = downloadUri.toString();
                                Picasso.get().load(downloadUri).into(iv_image);
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
                            Toast.makeText(CreateContact.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void onAddContact(ContactData contactData){
        String keyName = myRef.push().getKey();
        contactData.setFirebaseKey(keyName);
        myRef.child(keyName).setValue(contactData);

    }

}
