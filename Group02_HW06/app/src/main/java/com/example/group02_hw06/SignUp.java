package com.example.group02_hw06;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    EditText et_fname, et_lname, et_s_email, et_s_password, et_confpwd;
    Button btn_s_signup, btn_cancel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_s_email = findViewById(R.id.et_s_email);
        et_s_password = findViewById(R.id.et_s_password);
        et_confpwd = findViewById(R.id.et_confpwd);
        btn_s_signup = findViewById(R.id.btn_s_signup);
        btn_cancel = findViewById(R.id.btn_cancel);
        mAuth = FirebaseAuth.getInstance();

        btn_s_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(null!= et_fname.getText() && null!= et_lname.getText() && null!= et_s_email.getText() && null!= et_s_password.getText() && null!= et_confpwd.getText() && et_s_password.getText().toString().equals(et_confpwd.getText().toString())) {
                    createAccount(et_s_email.getText().toString(), et_s_password.getText().toString());
                }else{
                    Toast.makeText(SignUp.this, getResources().getString(R.string.enter_correct),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userProfile();
                            Log.d("auth_signup:::", "createUserWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("auth_signup:::", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, getResources().getString(R.string.auth_fail) +task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                       // hideProgressDialog();
                    }
                });
    }


    private void userProfile(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(null!= user){
            UserProfileChangeRequest profileUpdate =new UserProfileChangeRequest.Builder()
                    .setDisplayName(et_fname.getText().toString()+ " " + et_lname.getText().toString())
                    //.setPhotoUri("")
                    .build();

            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SignUp.this, getResources().getString(R.string.new_user),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this,ChatRoom.class);
                        intent.putExtra("userid",user.getUid());
                        startActivity(intent);
                        Log.d("auth_signup:::", "UID" +user.getUid());
                        finish();
                    }
                }
            });
        }
    }
}
