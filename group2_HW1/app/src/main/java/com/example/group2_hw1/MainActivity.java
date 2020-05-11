package com.example.group2_hw1;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Double bac=0.00;
    TextView tvalcval;
    ProgressBar prg;
    TextView tvbacval;
    TextView statusval;
    EditText etwt;
    RadioButton rb1oz;
    RadioButton rb5oz;
    RadioButton rb12oz;

    Switch gender;
    Button reset,save,adddrink;
    public Double wt,r;
    public String gen;
    int ounce;
    Double totalalc=0.00;
    Double alcpercent;
    int saveflag=0;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = MainActivity.this;
        setContentView(R.layout.activity_main);
        setTitle("BAC Calculator");
        //set logo
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBaralcpercent);
        seekBar.setProgress(1);
        //seekBar.incrementProgressBy(5);
         seekBar.setMin(0);
        seekBar.setMax(5);
        int p=seekBar.getProgress();
        final String a=String.valueOf(p*5);
        //Log.d("demo",a);

        tvalcval=(TextView)findViewById(R.id.textViewalcpercentval);
        tvalcval.setText(a);
        prg=findViewById(R.id.progressBarlevel);
        prg.setProgress(0);
        prg.setMax(25);
        tvbacval=findViewById(R.id.textView8levelval);
        tvbacval.setText("0.00");
        statusval=findViewById(R.id.textViewstatusval);
        statusval.setBackgroundColor(Color.parseColor("#006400"));
        statusval.setTextColor(Color.WHITE);
        statusval.setText("You're safe");
        rb1oz=findViewById(R.id.radioButton1oz);
        rb5oz=findViewById(R.id.radioButton5oz);
        rb12oz=findViewById(R.id.radioButton12oz);
        gender=findViewById(R.id.switch1);
        etwt=findViewById(R.id.editTextwtval);
        reset=findViewById(R.id.buttonreset);
        save=findViewById(R.id.buttonsave);
        adddrink=findViewById(R.id.buttonadddrink);

        rb1oz.setOnClickListener(this);
        rb5oz.setOnClickListener(this);
        rb12oz.setOnClickListener(this);

        gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    gender.setText(context.getString(R.string.male_st));
                    r=0.68;
                }

                else {
                    gender.setText(context.getString(R.string.female_st));
                    r = 0.55;
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval=progress*5;
                String prgval=String.valueOf(pval);
                tvalcval.setText(prgval);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((etwt.getText().toString()).isEmpty()) {
                    etwt.setError(context.getString(R.string.enter_weight));
                } else {
                    wt = Double.valueOf(etwt.getText().toString());
                    if (wt < 1) {
                        etwt.setError(context.getString(R.string.enter_weight));
                    }else {

                        gen = gender.getText().toString();
                        if (rb1oz.isChecked())
                            ounce = 1;
                        else if (rb5oz.isChecked())
                            ounce = 5;
                        else
                            ounce = 12;
                        alcpercent = Double.parseDouble(tvalcval.getText().toString());

                        if (saveflag == 1) {
                            bac = 0.00;
                            //Log.d("demo","Inside saveflag 1"+ bac);
                            //totalalc = totalalc + (ounce * (alcpercent / 100));
                            bac = (totalalc * 6.24) / (wt * r);
                            bac = Math.round(bac * 100.0) / 100.0;
                            tvbacval.setText(String.valueOf(bac));
                            prg.setProgress((int) (bac*100));
                        }
                        saveflag = 1;
                        if(bac<=0.08){
                            statusval.setText(R.string.youre_safe);
                            statusval.setBackgroundColor(Color.parseColor("#006400"));
                        }else if(bac<0.20){
                            statusval.setText(context.getString(R.string.be_careful));
                            statusval.setBackgroundColor(Color.parseColor("#f4d142"));
                        } else{
                            statusval.setText(context.getString(R.string.over_limit));
                            statusval.setBackgroundColor(Color.parseColor("#d11e1b"));
                        }

                        if(bac>=0.25){
                            Toast.makeText(MainActivity.this,context.getString(R.string.no_more_drinks), Toast.LENGTH_SHORT).show();
                            save.setEnabled(false);
                            adddrink.setEnabled(false);
                        }

                    }
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etwt.setText("");
                if(gender.isChecked())
                    gender.toggle();
                rb1oz.setChecked(true);
                seekBar.setProgress(1);//check
                tvalcval.setText(a);
                save.setEnabled(true);
                adddrink.setEnabled(true);
                tvbacval.setText("0.00");
                prg.setProgress(0); //change to default value
                statusval.setBackgroundColor(Color.parseColor("#006400"));
                statusval.setTextColor(Color.WHITE);
                statusval.setText(context.getString(R.string.youre_safe));
                bac=0.00;
                totalalc=0.00;
                saveflag=0;
            }
        });
     adddrink.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             if((etwt.getText().toString()).isEmpty()) {
                 etwt.setError(context.getString(R.string.enter_weight));
             } else {
                 wt = Double.valueOf(etwt.getText().toString());
                 if (wt < 1) {
                     etwt.setError(context.getString(R.string.enter_weight));
                 } else {

                     gen = gender.getText().toString();
                     if (rb1oz.isChecked())
                         ounce = 1;
                     else if (rb5oz.isChecked())
                         ounce = 5;
                     else
                         ounce = 12;
                     alcpercent = Double.parseDouble(tvalcval.getText().toString());
                     if (saveflag == 1) {
                         bac = 0.00;
                         totalalc = totalalc + (ounce * (alcpercent / 100));
                         bac = (totalalc * 6.24) / (wt * r);
                         bac = Math.round(bac * 100.0) / 100.0;
                         tvbacval.setText(String.valueOf(bac));
                         prg.setProgress((int) (bac*100));
                     } else {
                         etwt.setError(context.getString(R.string.save_weight));
                     }

                     if(bac<=0.08){
                         statusval.setText(R.string.youre_safe);
                         statusval.setBackgroundColor(Color.parseColor("#006400"));
                     }else if(bac<0.20){
                         statusval.setText(context.getString(R.string.be_careful));
                         statusval.setBackgroundColor(Color.parseColor("#f4d142"));
                     } else{
                         statusval.setText(context.getString(R.string.over_limit));
                         statusval.setBackgroundColor(Color.parseColor("#d11e1b"));
                     }

                     if(bac>=0.25){
                         Toast.makeText(MainActivity.this,context.getString(R.string.no_more_drinks), Toast.LENGTH_SHORT).show();
                         save.setEnabled(false);
                         adddrink.setEnabled(false);
                     }
                 }
             }
         }
     });



    }

    @Override
    public void onClick(View v) {

    }
}
