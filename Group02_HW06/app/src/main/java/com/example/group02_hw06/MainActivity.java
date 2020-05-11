package com.example.group02_hw06;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login,btn_signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=et_email.getText() && null!= et_password.getText()) {
                    signIn(et_email.getText().toString(), et_password.getText().toString());
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_fields), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(null!= currentUser){
            Intent intent = new Intent(MainActivity.this,ChatRoom.class);
            intent.putExtra("userid",currentUser.getUid());
            startActivity(intent);
            finish();
        }
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("auth_main:::", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("auth_main:::", "UID" +user.getUid());

                            Intent intent = new Intent(MainActivity.this,ChatRoom.class);
                            intent.putExtra("userid",user.getUid());
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("auth_main:::", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed:" +task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // hideProgressDialog();
                    }
                });
    }

}
