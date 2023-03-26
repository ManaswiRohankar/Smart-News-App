package com.bhartiya.smartnews.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhartiya.smartnews.CategoryNewsList;
import com.bhartiya.smartnews.R;
import com.bhartiya.smartnews.SplashScreen;


/**
 * Created by Manaswi Rohankar
 */
public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView catName;

    public CategoriesViewHolder(final Context context, View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.categoryImage);
        catName = (TextView) itemView.findViewById(R.id.categoryName);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d("data", SplashScreen.categoryListResponseData.get(getAdapterPosition()).getNewsList().size() + "");
                    Intent intent = new Intent(context, CategoryNewsList.class);
                    intent.putExtra("pos", getAdapterPosition());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "There is no news in this category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
