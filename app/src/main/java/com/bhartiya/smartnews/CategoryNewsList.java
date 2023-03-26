package com.bhartiya.smartnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import com.bhartiya.smartnews.Adapters.NewsListAdapter;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryNewsList extends Activity {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    int pos;
    @BindViews({R.id.menu, R.id.back})
    List<ImageView> imageViews;
    @BindView(R.id.title)
    TextView title;
    SearchView searchView;
    NewsListAdapter newsListAdapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        imageViews.get(0).setVisibility(View.INVISIBLE);
        imageViews.get(1).setVisibility(View.VISIBLE);
        searchView = (SearchView) findViewById(R.id.searchView);
        // custom searchView
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(id);
        searchEditText.setTextColor(getResources().getColor(R.color.color_white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.light_white));
        mAdView = (AdView) findViewById(R.id.adView);
        // display Banner Ads
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A9C93B8F0F67284AB70DF28784CA0F1C")
                .build();
        mAdView.loadAd(adRequest);
        imageViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText(SplashScreen.categoryListResponseData.get(pos).getCategoryName());
        setData();
        // implement onQueryTextListener on searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                newsListAdapter.filter(text);
                return false;
            }
        });
    }

    @OnClick(R.id.share)
    public void onClick() {
        shareApp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    public void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this Smart News App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    private void setData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoryNewsList.this);
        recyclerview.setLayoutManager(linearLayoutManager);
        newsListAdapter = new NewsListAdapter(CategoryNewsList.this, SplashScreen.categoryListResponseData.get(pos).getNewsList());
        recyclerview.setAdapter(newsListAdapter);
    }
}
