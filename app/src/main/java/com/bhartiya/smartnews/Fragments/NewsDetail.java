package com.bhartiya.smartnews.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.bhartiya.smartnews.MVP.NewsListResponse;
import com.bhartiya.smartnews.MainActivity;
import com.bhartiya.smartnews.R;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetail extends Activity {

    @BindViews({R.id.bannerImage, R.id.favorite,R.id.shadowIcon})
    List<ImageView> imageViews;
    @BindViews({R.id.postTitle, R.id.categoryName, R.id.date})
    List<TextView> textViews;
    @BindView(R.id.postDescWebView)
    WebView postDescWebView;
    public static List<NewsListResponse> newsListResponsesData;
    @BindViews({R.id.menu, R.id.back, R.id.share})
    List<ImageView> imageViews1;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.favorite)
    ImageView favorite;
    int pos;
    public static int addTime = 1;
    InterstitialAd mInterstitialAd;
    private AdView mAdView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_page);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("rateUs", 0);
        editor = sharedPreferences.edit();
        // display Ads and rate dialog
        if (addTime % 3 == 0)
            showAd();
        else if (addTime % 6 == 0) {
            addTime = addTime + 1;
            if (sharedPreferences.getString("rate", "No").equalsIgnoreCase("No")) {
                showRateDialog();
            }
        } else
            addTime = addTime + 1;
        imageViews1.get(0).setVisibility(View.INVISIBLE);
        imageViews1.get(2).setVisibility(View.VISIBLE);
        searchView.setVisibility(View.GONE);
        imageViews1.get(1).setVisibility(View.VISIBLE);
        // display banner Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("894D5139075F3A8D7BBAC19A7AB5E49F")
                .build();
        mAdView.loadAd(adRequest);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        imageViews1.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });
        imageViews1.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("News Detail");
        setData(); // set news detail data

    }

    private void setData() {

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
                imageViews.get(2).setVisibility(View.GONE);
            }
        });
        builder.build().
                load(newsListResponsesData.get(pos).getImage())
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.error_image)
                .into(imageViews.get(0));

        textViews.get(0).setText(newsListResponsesData.get(pos).getTitle());
        textViews.get(1).setText(newsListResponsesData.get(pos).getCategory());
        textViews.get(2).setText(newsListResponsesData.get(pos).getDate());
        postDescWebView.loadDataWithBaseURL(null, newsListResponsesData.get(pos).getDescription(), "text/html", "utf-8", null);
        if (MainActivity.imageIds.contains(newsListResponsesData.get(pos).getId().trim())) {
            favorite.setImageResource(R.drawable.favorited_icon);
        } else
            favorite.setImageResource(R.drawable.unfavorite_icon);
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
        // share app with friends
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this Smart News App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    @OnClick({R.id.back, R.id.favorite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favorite:
                if (MainActivity.imageIds.contains(newsListResponsesData.get(pos).getId().trim())) {
                    Log.d("remove", "YES");
                    favorite.setImageResource(R.drawable.unfavorite_icon);
                    MainActivity.imageIds.remove(newsListResponsesData.get(pos).getId().trim());
                    Toast.makeText(NewsDetail.this, "Removed From Your Favorite", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("remove", "NO");
                    MainActivity.imageIds.add(newsListResponsesData.get(pos).getId().trim());
                    favorite.setImageResource(R.drawable.favorited_icon);
                    Toast.makeText(NewsDetail.this, "Added To Your Favorite", Toast.LENGTH_SHORT).show();
                }
                Log.d("updatedList", MainActivity.imageIds.toString());
                MainActivity.editor.putString("data", MainActivity.imageIds.toString().trim());
                MainActivity.editor.commit();
                break;
        }
    }

    private void showRateDialog() {
        // Rate your app on Play Store
        new AlertDialog.Builder(NewsDetail.this)
                .setTitle("Rate Us On Play Store")
                .setMessage("If you like this App. Please rate us on Play Store.")
                .setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        editor.putString("rate", "Yes");
                        editor.commit();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                        }

                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        editor.putString("rate", "No");
                        editor.commit();

                    }
                }).show();
    }

    private void showAd() {
        addTime = addTime + 1;
        mInterstitialAd = new InterstitialAd(NewsDetail.this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("894D5139075F3A8D7BBAC19A7AB5E49F")
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}
