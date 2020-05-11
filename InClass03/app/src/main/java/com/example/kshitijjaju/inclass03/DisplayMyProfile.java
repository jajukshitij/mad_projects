package com.example.kshitijjaju.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayMyProfile extends AppCompatActivity {

    TextView tvName,tvStudentId,tvDepartment;
    Button edit;
    ImageView ivAvatar;
    String fName,lName;
    int studentId,img_id;
    String deptName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);
        setTitle("Display My Profile");

        tvName = (TextView) findViewById(R.id.tvName);
        tvStudentId = (TextView) findViewById(R.id.tvStudentId);
        tvDepartment = (TextView) findViewById(R.id.tvDepartment);
        edit = (Button) findViewById(R.id.buttonEdit);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Student student = (Student) getIntent().getExtras().getSerializable(MyProfile.studentkey);
            tvName.setText(student.name);
            tvStudentId.setText(String.valueOf(student.student_id));
            tvDepartment.setText(student.dept);
            ivAvatar.setImageResource(student.image_id);

            Log.d("demo","error");
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }







        }


