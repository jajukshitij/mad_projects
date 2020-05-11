package com.example.kshitijjaju.inclass2a;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/*Assignment 2
Main Activity.java
Kshitij Jaju, Sai Smruthi*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = MainActivity.this;

        setTitle(context.getString(R.string.app_name));

        Button buttonCalculate;

        final TextView tvResult = (TextView) findViewById(R.id.textViewResult);
        final TextView tvResultValue = (TextView) findViewById(R.id.textViewResultValue);
        final EditText etWeight = (EditText) findViewById(R.id.editTextWeight);
        final EditText etFeet = (EditText) findViewById(R.id.editTextFeet);
        final EditText etInches = (EditText) findViewById(R.id.editTextInches);
        buttonCalculate = (Button) findViewById(R.id.buttonCalculate);

        if(etWeight.getText().toString().isEmpty()){
            etWeight.setError(context.getString(R.string.set_error_message));
        }
        if(etFeet.getText().toString().isEmpty()){
            etFeet.setError(context.getString(R.string.set_error_message));
        }
        if(etInches.getText().toString().isEmpty()){
            etInches.setError(context.getString(R.string.set_error_message));
        }


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Double weight, feet, inches, feetToInches, totalInches, bmi;
                        weight = Double.valueOf(etWeight.getText().toString());
                        feet = Double.valueOf(etFeet.getText().toString());
                        inches = Double.valueOf(etInches.getText().toString());

                        feetToInches = (feet *12);

                        totalInches = (feetToInches + inches);

                        bmi = ((weight/(totalInches*totalInches))*703);
                        bmi = Math.round(bmi *100.0)/100.0;

                        if(weight <= 0 || totalInches <= 0){

                            Toast.makeText(MainActivity.this, context.getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();

                        } else {

                            tvResultValue.setText(context.getString(R.string.your_bmi) +" "+ bmi.toString());
                            if (bmi <= 18.5) {
                                //underweight
                                tvResult.setText(context.getString(R.string.you_are) + " "+ context.getString(R.string.under_weight));
                            } else if (18.5 < bmi && bmi <= 24.9) {
                                //normal
                                tvResult.setText(context.getString(R.string.you_are) + " "+ context.getString(R.string.normal));
                            } else if (25 <= bmi && bmi <= 29.9) {
                                //overweight
                                tvResult.setText(context.getString(R.string.you_are) + " "+ context.getString(R.string.over_weight));
                            } else if (bmi >= 30) {
                                //obese
                                tvResult.setText(context.getString(R.string.you_are) + " "+ context.getString(R.string.obese));
                            }
                            Toast.makeText(MainActivity.this, context.getString(R.string.bmi_calculated), Toast.LENGTH_SHORT).show();
                        }

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, context.getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
