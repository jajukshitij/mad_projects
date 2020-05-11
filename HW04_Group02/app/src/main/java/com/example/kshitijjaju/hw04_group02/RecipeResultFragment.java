package com.example.kshitijjaju.hw04_group02;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RecipeResultFragment extends Fragment {

    String url;
    ArrayList<String> ingredients;
    String dish_name;
    RecyclerView rv_recipeDetails;
    LinearLayout linlyt_progress;
    Button btn_finish;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    ProgressBar prg;
    TextView loadtext;

    private OnFragmentInteractionListener mListener;

    public RecipeResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle b = this.getArguments();
        if(b != null){
            ingredients = b.getStringArrayList("Ingredients");
            dish_name = b.getString("Dish");

            StringBuffer buf = new StringBuffer();
            for(int i = 0; i < ingredients.size(); i++)
            {
                if(i == ingredients.size() - 1) {
                    buf.append(ingredients.get(i));
                }
                else{
                    buf.append(ingredients.get(i));
                    buf.append(",");
                }
            }
            rv_recipeDetails = getView().findViewById(R.id.rv_recipeDetails);
            layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
            rv_recipeDetails.setLayoutManager(layoutManager);
            ArrayList<RecipeData> recipes = new ArrayList<>();
            mAdapter = new RecipeDetailsAdapter(recipes,getContext());
            rv_recipeDetails.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            new GetRecipeAsync().execute("http://www.recipepuppy.com/api/?i="+buf.toString()+"&q="+dish_name);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_result, container, false);
        rv_recipeDetails = view.findViewById(R.id.rv_recipeDetails);
        linlyt_progress = view.findViewById(R.id.linlyt_progress);
        btn_finish = view.findViewById(R.id.btn_finish);
        prg=view.findViewById(R.id.progressBar);
        loadtext=view.findViewById(R.id.tvload);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    private class GetRecipeAsync extends AsyncTask<String,Integer,ArrayList<RecipeData>>{

        @Override
        protected ArrayList<RecipeData> doInBackground(String... strings) {
            ArrayList<RecipeData> results = new ArrayList<>();

            HttpURLConnection connection = null;
            String result = null;
            try {
                String strUrl = strings[0];
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(result);
                    JSONArray jsonresults = root.getJSONArray("results");
                    for (int i = 0; i < jsonresults.length(); i++) {
                        JSONObject jsonObject = jsonresults.getJSONObject(i);
                        RecipeData Obj_Recipe = new RecipeData();
                        Obj_Recipe.setTitle(jsonObject.getString("title"));
                        Obj_Recipe.setThumbnail(jsonObject.getString("thumbnail"));
                        Obj_Recipe.setIngredients(jsonObject.getString("ingredients"));
                        Obj_Recipe.setHref(jsonObject.getString("href"));
                        results.add(Obj_Recipe);
                        publishProgress(i);
                    }
                }

            } catch (Exception e) {


            } finally {

            }
            return results;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            linlyt_progress.setVisibility(View.VISIBLE);
            int progress = prg.getProgress();
            prg.setProgress(progress + values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<RecipeData> recipeDataList) {
            rv_recipeDetails.setVisibility(View.VISIBLE);
            linlyt_progress.setVisibility(View.GONE);
            if(recipeDataList.isEmpty())
            {
                mListener.onFragmentInteraction();
                Toast.makeText(getContext(), "Recipe not found", Toast.LENGTH_SHORT).show();
            }
            else {
                mAdapter = new RecipeDetailsAdapter(recipeDataList,getContext());
                rv_recipeDetails.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}

