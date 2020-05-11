package com.example.kshitijjaju.inclass03;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MyProfile extends AppCompatActivity {

    ImageView ivAvatarImage;
    EditText etFirstName,etLastName, etStudentId;
    Button buttonSave;
    RadioButton rbdept1,rbdept2,rbdept3,rbdept4;
    int REQ_CODE= 100,imageid,studentid,flag =0;
    String firstname,lastname,deptname;

    static String studentkey = "student";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = MyProfile.this;
        setTitle("My Profile");

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etStudentId = (EditText) findViewById(R.id.etStudentId);
        ivAvatarImage = (ImageView) findViewById(R.id.ivAvatarImage);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        rbdept1 = (RadioButton) findViewById(R.id.rbCS);
        rbdept2 = (RadioButton) findViewById(R.id.rbSIS);
        rbdept3 = (RadioButton) findViewById(R.id.rbBIO);
        rbdept4 = (RadioButton) findViewById(R.id.rbOther);

        ivAvatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfile.this, SelectAvatar.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(flag==0) {
                        firstname = etFirstName.getText().toString();
                        lastname = etLastName.getText().toString();
                        studentid = Integer.parseInt(etStudentId.getText().toString());
                        if (firstname.length() == 0 || lastname.length() == 0 || etStudentId.getText().toString().length() < 9 || imageid==0) {
                            flag = 1;
                            throw new Exception();

                        }
                        if (rbdept1.isChecked())
                            deptname = rbdept1.getText().toString();
                        else if (rbdept2.isChecked())
                            deptname = rbdept2.getText().toString();
                        else if (rbdept3.isChecked())
                            deptname = rbdept3.getText().toString();
                        else if (rbdept4.isChecked())
                            deptname = rbdept4.getText().toString();
                        else {
                            flag = 1;
                            throw new Exception();
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(MyProfile.this, "invalid Input", Toast.LENGTH_SHORT).show();
                }

                if(flag!=1) {

                    Intent i = new Intent(MyProfile.this, DisplayMyProfile.class);
                    String name = firstname.concat(lastname);
                    Student stud = new Student(name, studentid, deptname, imageid);
                    i.putExtra(studentkey, stud);
                    startActivity(i);
                }
                flag = 0;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK && data.getExtras() != null) {
                Bundle b = data.getExtras();
                imageid = b.getInt("Image");
                ivAvatarImage.setImageResource(imageid);
            }
        }
    }


}

