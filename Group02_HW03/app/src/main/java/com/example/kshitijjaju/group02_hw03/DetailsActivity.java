package com.example.kshitijjaju.group02_hw03;

/*Homework 03
 * DetailsActivity
 * Kshitij Jaju
 * Smruthi Rajagopal*/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    TextView tv_dTrackName, tv_dGenre, tv_dArtistName, tv_dAlbumName, tv_dTrackPrice, tv_dAlbumPrice;
    ImageView iv_artwork;
    Button button_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        iv_artwork = findViewById(R.id.iv_artwork);
        tv_dTrackName = findViewById(R.id.tv_dTrackName);
        tv_dGenre = findViewById(R.id.tv_dGenre);
        tv_dArtistName = findViewById(R.id.tv_dArtistName);
        tv_dAlbumName = findViewById(R.id.tv_dAlbumName);
        tv_dTrackPrice = findViewById(R.id.tv_dTrackPrice);
        tv_dAlbumPrice = findViewById(R.id.tv_dAlbumPrice);
        button_finish = findViewById(R.id.button_finish);

        if(null!=getIntent() && null!= getIntent().getSerializableExtra("musicData")) {

            MusicData musicData = (MusicData) getIntent().getSerializableExtra("musicData");

            Picasso.get().load(musicData.artworkUrl).into(iv_artwork);
            tv_dTrackName.setText("Track: "+musicData.trackName);
            tv_dGenre.setText("Genre: "+musicData.genre);
            tv_dArtistName.setText("Artist: "+musicData.artistName);
            tv_dAlbumName.setText("Album: "+musicData.albumName);
            tv_dTrackPrice.setText("Track Price: "+musicData.trackPrice+" $");
            tv_dAlbumPrice.setText("Album Price: "+musicData.albumPrice+" $");
        }

        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
