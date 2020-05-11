package com.example.kshitijjaju.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetImageAsync extends AsyncTask<String,Void, Bitmap> {
    IData idata;

    public GetImageAsync(IData idata) {
        this.idata = idata;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        HttpURLConnection con = null;
        Bitmap bitmap = null;
        try{
            URL url=new URL(strings[0]);
            Log.d("Bitmap_url",""+strings[0]);
            con= (HttpURLConnection) url.openConnection();
            con.connect();
            bitmap= BitmapFactory.decodeStream(con.getInputStream());
            Log.d("Bitmap1",""+bitmap);
            //publishProgress();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d("Bitmap2",""+bitmap);
        idata.handledata(bitmap);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        idata.updateprogress();
    }

    public static interface  IData{
        public void handledata(Bitmap bitmap);
        public void updateprogress();
    }
}

