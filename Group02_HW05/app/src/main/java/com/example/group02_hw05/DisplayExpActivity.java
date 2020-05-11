package com.example.group02_hw05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayExpActivity extends AppCompatActivity {

    TextView tv_dname,tv_dcost,tv_ddate;
    ImageView iv_receipt;
    Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_exp);
        tv_dname = findViewById(R.id.tv_dname);
        tv_dcost = findViewById(R.id.tv_dcost);
        tv_ddate = findViewById(R.id.tv_ddate);
        iv_receipt = findViewById(R.id.iv_receipt);
        btn_finish = findViewById(R.id.btn_finish);

        if(null!=getIntent() && null!= getIntent().getSerializableExtra("expenseData")) {

            ExpenseData expenseData = (ExpenseData) getIntent().getSerializableExtra("expenseData");

           Picasso.get().load(expenseData.receipt).into(iv_receipt);
            tv_dname.setText(expenseData.name);
            tv_ddate.setText(expenseData.date);
            tv_dcost.setText(expenseData.cost);
        }

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
