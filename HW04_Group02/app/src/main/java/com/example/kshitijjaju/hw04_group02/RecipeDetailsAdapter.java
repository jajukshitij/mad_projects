package com.example.kshitijjaju.hw04_group02;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.RecipeViewHolder>{

    ArrayList<RecipeData> recipeDataArrayList;
    static Context context;

    public RecipeDetailsAdapter(ArrayList<RecipeData> recipeDataArrayList, Context context) {
        this.recipeDataArrayList = recipeDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_details, viewGroup, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {

        RecipeData recipeData = recipeDataArrayList.get(i);
        recipeViewHolder.tv_title.setText(recipeData.title);
        recipeViewHolder.tv_ingredients.setText(recipeData.ingredients);
        recipeViewHolder.tv_url.setText(recipeData.href);
        if(recipeData.thumbnail.isEmpty()){
            recipeViewHolder.imageView.setImageResource(R.drawable.ic_launcher);
        }
        else{
        Picasso.get()
                .load(recipeData.thumbnail)
                .into(recipeViewHolder.imageView);
        recipeViewHolder.recipeData = recipeData;
    }}

    @Override
    public int getItemCount() {
        if(null!= recipeDataArrayList) {
            return recipeDataArrayList.size();
        }else{
            return 0;
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title, tv_ingredients, tv_url;
        ImageView imageView;
        RecipeData recipeData;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_ingredients = itemView.findViewById(R.id.tv_ingredients);
            tv_url = itemView.findViewById(R.id.tv_url);
            imageView = itemView.findViewById(R.id.imageView);

            tv_url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(recipeData.href));
                    context.startActivity(intent);
                }
            });
        }
    }
}
