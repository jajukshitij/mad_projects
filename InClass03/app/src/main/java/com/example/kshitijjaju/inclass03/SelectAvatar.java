package com.example.kshitijjaju.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectAvatar extends AppCompatActivity implements View.OnClickListener {

   ImageView ivFemale1,ivFemale2,ivFemale3,ivMale1,ivMale2,ivMale3;
   String ImageID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        setTitle("Select Avatar");

        ivFemale1 =(ImageView) findViewById(R.id.ivFemale1);
        ivFemale2 =(ImageView) findViewById(R.id.ivFemale2);
        ivFemale3 =(ImageView) findViewById(R.id.ivFemale3);
        ivMale1 =(ImageView) findViewById(R.id.ivMale1);
        ivMale2 =(ImageView) findViewById(R.id.ivMale2);
        ivMale3 =(ImageView) findViewById(R.id.ivMale3);
        ivFemale1.setOnClickListener(this);
        ivFemale2.setOnClickListener(this);
        ivFemale3.setOnClickListener(this);
        ivMale1.setOnClickListener(this);
        ivMale2.setOnClickListener(this);
        ivMale3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent();
        Bundle b=new Bundle();
        switch (v.getId()){
            case R.id.ivFemale1:
                b.putInt("Image",R.drawable.avatar_f_1);
                i.putExtras(b);
                setResult(RESULT_OK,i);
                break;
            case R.id.ivFemale2:
                b.putInt("Image",R.drawable.avatar_f_2);
                i.putExtras(b);
                setResult(RESULT_OK,i);
                break;
            case R.id.ivFemale3:
                b.putInt("Image",R.drawable.avatar_f_3);
                i.putExtras(b);
                setResult(RESULT_OK,i);
                break;
            case R.id.ivMale1:
                b.putInt("Image",R.drawable.avatar_m_1);
                i.putExtras(b);
                setResult(RESULT_OK,i);
                break;
            case R.id.ivMale2:
                b.putInt("Image",R.drawable.avatar_m_2);
                i.putExtras(b);
                setResult(RESULT_OK,i);
                break;
            case R.id.ivMale3:
                b.putInt("Image",R.drawable.avatar_m_3);
                i.putExtras(b);
                setResult(RESULT_OK,i);
                break;
        }
        finish();
    }




}
