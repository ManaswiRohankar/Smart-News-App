package com.bhartiya.smartnews.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.bhartiya.smartnews.MVP.NewsListResponse;
import com.bhartiya.smartnews.R;


/**
 * Created by Manaswi Rohankar
 */
public class NewsListViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView catName, date, newsTitle;
    CardView cardView;

    public NewsListViewHolder(final Context context, View itemView,List<NewsListResponse> newsListResponse) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.newsImage);
        catName = (TextView) itemView.findViewById(R.id.categoryName);
        date = (TextView) itemView.findViewById(R.id.date);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);

    }
}
