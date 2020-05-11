package com.example.kshitijjaju.inclass06;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetNewsAsync extends AsyncTask<String,Void,ArrayList<NewsData>> {

    IData idata;

    public GetNewsAsync(IData idata) {
        this.idata = idata;
    }

    @Override
    protected ArrayList<NewsData> doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection connection = null;
        ArrayList<NewsData> newsDataArrayList = new ArrayList<>();

        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String jsonString = IOUtils.toString(connection.getInputStream(), "UTF-8");
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray articleArray = jsonObject.getJSONArray("articles");
                for (int i = 0; i < articleArray.length(); i++) {

                    NewsData newsData = new NewsData();
                    JSONObject articleObject = articleArray.getJSONObject(i);
                    newsData.title = articleObject.getString("title");
                    newsData.date = articleObject.getString("publishedAt");
                    newsData.imageUrl = articleObject.getString("urlToImage");
                    newsData.decscription = articleObject.getString("description");
                    newsDataArrayList.add(newsData);
                }
                return newsDataArrayList;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        idata.setProgress();
    }

    @Override
    protected void onPostExecute(ArrayList<NewsData> newsData) {
        idata.setNewsData(newsData);

    }

    public static interface  IData{
        public void setNewsData(ArrayList<NewsData> newsDataArrayList);
        public void setProgress();

    }
}
