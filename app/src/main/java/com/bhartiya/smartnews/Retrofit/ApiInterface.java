package com.bhartiya.smartnews.Retrofit;


import java.util.List;

import com.bhartiya.smartnews.MVP.CategoryListResponse;
import com.bhartiya.smartnews.MVP.NewsListResponse;
import com.bhartiya.smartnews.MVP.RegistrationResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {

    // API's endpoints
    @GET("/app_dashboard/jsonUrl/latest-news.php")
    public void getNewsList(
            Callback<List<NewsListResponse>> callback);

    @GET("/app_dashboard/jsonUrl/all-category-news.php")
    public void getCategoryList(Callback<List<CategoryListResponse>> callback);

    @FormUrlEncoded
    @POST("/app_dashboard/jsonUrl/pushadd.php")
    public void sendAccessToken(@Field("accesstoken") String accesstoken, Callback<RegistrationResponse> callback);


}
