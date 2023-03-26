package com.bhartiya.smartnews.Retrofit;

import retrofit.RestAdapter;

/**
 * Created by Manaswi Rohankar
 */
public class Api {

    public static ApiInterface getClient() {

        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://www.destinyhub.info/") //Set the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
