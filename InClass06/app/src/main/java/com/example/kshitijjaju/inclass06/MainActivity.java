package com.example.kshitijjaju.inclass06;

/*  In Class Assignment 06
    Name: Kshitij Jaju
*   Student ID:801075892*/

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetNewsAsync.IData {

    Button go;
    ImageButton previous, next;
    ImageView imageView;
    TextView tvCategories,tvLoading,tvDate,tvDescription,tvTitle,tvIndex;
    ProgressBar progressBar;
    LinearLayout linllyt_content,linllyt_progress,linlyt_filler;
    String[] categoryArray= new String[7];
    ArrayList<NewsData> newsDataRetrieved = new ArrayList<NewsData>();
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go = findViewById(R.id.btn_go);
        previous = (ImageButton) findViewById(R.id.ibtn_prev);
        next = findViewById(R.id.ibtn_next);
        tvCategories = findViewById(R.id.tv_categories);
        imageView = findViewById(R.id.iv_image);
        progressBar = findViewById(R.id.progress_circular);
        tvLoading = findViewById(R.id.tv_loading);
        tvDate = findViewById(R.id.tv_date);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        tvIndex = findViewById(R.id.tv_index);
        linllyt_content = findViewById(R.id.linllyt_content);
        linllyt_progress = findViewById(R.id.linllyt_progress);
        linlyt_filler = findViewById(R.id.linlyt_filler);

        tvLoading.setVisibility(View.GONE);
        previous.setEnabled(false);
        next.setEnabled(false);
        tvIndex.setVisibility(View.INVISIBLE);
        linllyt_content.setVisibility(View.GONE);
        linllyt_progress.setVisibility(View.GONE);
        linlyt_filler.setVisibility(View.VISIBLE);

        categoryArray[0]="business";
        categoryArray[1]="entertainment";
        categoryArray[2]="general";
        categoryArray[3]="health";
        categoryArray[4]="science";
        categoryArray[5]="sports";
        categoryArray[6]="technology";


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.choose_category))
                        .setItems(categoryArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                tvLoading.setText(getString(R.string.loading));

                                String category= categoryArray[which];
                                tvCategories.setText(category);

                                String url = null;
                                try {
                                    url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=c7fe52e401a94ee6ad5a674665e00f33&category="+URLEncoder.encode(category, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                                if(isConnected()) {
                                    newsDataRetrieved.clear();
                                    index=0;
                                    new GetNewsAsync(MainActivity.this).execute(url);
                                }else{
                                    Toast.makeText(MainActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                builder.create().show();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index --;

                if(null!=newsDataRetrieved) {

                    if(index<0){
                        index = newsDataRetrieved.size()-1;
                    }

                    tvTitle.setText(newsDataRetrieved.get(index).title);
                    tvDate.setText(newsDataRetrieved.get(index).date);
                    Picasso.get().load(newsDataRetrieved.get(index).imageUrl).into(imageView);
                    tvDescription.setText(newsDataRetrieved.get(index).decscription);
                    tvIndex.setText(index+1 +" out of "+ newsDataRetrieved.size());
                    tvIndex.setVisibility(View.VISIBLE);
                    linllyt_content.setVisibility(View.VISIBLE);
                    linllyt_progress.setVisibility(View.GONE);
                    linlyt_filler.setVisibility(View.GONE);

                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;

                if(null!=newsDataRetrieved) {

                    if(index >= newsDataRetrieved.size()){
                        index = 0;
                    }

                    tvTitle.setText(newsDataRetrieved.get(index).title);
                    tvDate.setText(newsDataRetrieved.get(index).date);
                    Picasso.get().load(newsDataRetrieved.get(index).imageUrl).into(imageView);
                    tvDescription.setText(newsDataRetrieved.get(index).decscription);
                    tvIndex.setText(index+1 +" out of "+ newsDataRetrieved.size());
                    tvIndex.setVisibility(View.VISIBLE);
                    linllyt_content.setVisibility(View.VISIBLE);
                    linllyt_progress.setVisibility(View.GONE);
                    linlyt_filler.setVisibility(View.GONE);

                }

            }
        });
    }


    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void setNewsData(ArrayList<NewsData> newsDataArrayList) {
        newsDataRetrieved= newsDataArrayList;
        if(null!=newsDataRetrieved) {
            if(newsDataRetrieved.size()>1){
                previous.setEnabled(true);
                next.setEnabled(true);
            }else {
                previous.setEnabled(false);
                next.setEnabled(false);
            }
            tvTitle.setText(newsDataRetrieved.get(0).title);
            tvDate.setText(newsDataRetrieved.get(0).date);
            Picasso.get().load(newsDataRetrieved.get(0).imageUrl).into(imageView);
            tvDescription.setText(newsDataRetrieved.get(0).decscription);
            tvIndex.setText("1 out of "+ newsDataRetrieved.size());
            tvIndex.setVisibility(View.VISIBLE);
            linllyt_content.setVisibility(View.VISIBLE);
            linllyt_progress.setVisibility(View.GONE);
            linlyt_filler.setVisibility(View.GONE);

        }else{
            previous.setEnabled(false);
            next.setEnabled(false);
            tvIndex.setVisibility(View.INVISIBLE);
            linllyt_content.setVisibility(View.GONE);
            linllyt_progress.setVisibility(View.GONE);
            linlyt_filler.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this,getString(R.string.no_news),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void setProgress() {
        previous.setEnabled(false);
        next.setEnabled(false);
        tvIndex.setVisibility(View.GONE);
        linllyt_content.setVisibility(View.GONE);
        linllyt_progress.setVisibility(View.VISIBLE);
        linlyt_filler.setVisibility(View.GONE);
    }
}
