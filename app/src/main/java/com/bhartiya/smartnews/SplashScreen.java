package com.bhartiya.smartnews;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bhartiya.smartnews.Fragments.NewsDetail;
import com.bhartiya.smartnews.MVP.CategoryListResponse;
import com.bhartiya.smartnews.MVP.NewsListResponse;
import com.bhartiya.smartnews.Retrofit.Api;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashScreen extends Activity {

    public static List<CategoryListResponse> categoryListResponseData;
    public static List<NewsListResponse> newsListResponsesData;
    public static List<NewsListResponse> imagesList1;
    String id = "";
    @BindView(R.id.internetNotAvailable)
    LinearLayout internetNotAvailable;
    @BindView(R.id.splashImage)
    ImageView splashImage;
    SharedPreferences sharedPreference,sharedPreferencesCache;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferencesCache = getSharedPreferences("cacheExist", 0);

        // check data from FCM
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            Log.d("notification Data", id);
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
        }
        sharedPreference=getSharedPreferences("localData",0);
        editor=sharedPreference.edit();
        // Check the internet and get response from API's
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            getCategoryList();
        } else {

            if (sharedPreference.getString("categoryList", "").equalsIgnoreCase("")||
                    sharedPreferencesCache.getBoolean("exist",false)==false)
            {
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }else {
                Gson gson = new Gson();
                String json = sharedPreference.getString("categoryList", "");
                String json1 = sharedPreference.getString("newslist", "");
                Log.d("savedCategoryData",sharedPreference.getString("categoryList", "Not Available"));
                List categoryData,placeData;
                CategoryListResponse[] categoryItems = gson.fromJson(json,CategoryListResponse[].class);
                categoryData = Arrays.asList(categoryItems);
                categoryListResponseData = new ArrayList(categoryData);
                Log.d("categoryListRseDataD",categoryListResponseData.size()+"");

                NewsListResponse[] placeItems = gson.fromJson(json1,NewsListResponse[].class);
                placeData = Arrays.asList(placeItems);
                newsListResponsesData = new ArrayList(placeData);
                Log.d("newsListRseDataD",newsListResponsesData.size()+"");
                moveNext();
            }
        }
    }

    @OnClick(R.id.tryAgain)
    public void onClick() {
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            internetNotAvailable.setVisibility(View.GONE);
            splashImage.setVisibility(View.VISIBLE);
            getCategoryList();
        } else {
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    public void getCategoryList() {
        // getting category list news data
        Api.getClient().getCategoryList(new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {
                categoryListResponseData = categoryListResponses;
                Gson gson = new Gson();
                String json = gson.toJson(categoryListResponseData);
                editor.putString("categoryList", json);
                editor.commit();
                getNewsList();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }

    public void getNewsList() {
        // getting news list data
        Api.getClient().getNewsList(new Callback<List<NewsListResponse>>() {
            @Override
            public void success(List<NewsListResponse> newsListResponses, Response response) {
                newsListResponsesData = newsListResponses;
                Gson gson = new Gson();
                String json = gson.toJson(newsListResponsesData);
                editor.putString("newslist", json);
                editor.commit();
                moveNext();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }

    private void moveNext() {
// redirect to next page after getting data from server
        try {
            imagesList1 = new ArrayList<>();
            if (id.length() > 0) {
                for (int j = 0; j < newsListResponsesData.size(); j++) {
                    if (newsListResponsesData.get(j).getId().trim().equalsIgnoreCase(id)) {
                        imagesList1.add(newsListResponsesData.get(j));
                    }
                }

                NewsDetail.newsListResponsesData = imagesList1;
                Intent intent = new Intent(SplashScreen.this, NewsDetail.class);
                intent.putExtra("pos", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
