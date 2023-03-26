package com.bhartiya.smartnews.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bhartiya.smartnews.Adapters.CategoriesAdapter;
import com.bhartiya.smartnews.DetectConnection;
import com.bhartiya.smartnews.MVP.CategoryListResponse;
import com.bhartiya.smartnews.R;
import com.bhartiya.smartnews.Retrofit.Api;
import com.bhartiya.smartnews.SplashScreen;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Categories extends Fragment {

    View view;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    public static CategoriesAdapter categoriesAdapter;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_latest_news, container, false);
        ButterKnife.bind(this, view);
        setData();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getCategoryList();
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return view;
    }

    public void getCategoryList() {
        Api.getClient().getCategoryList(new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {
                SplashScreen.categoryListResponseData.clear();
                SplashScreen.categoryListResponseData.addAll(categoryListResponses);
                categoriesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void setData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerview.setLayoutManager(gridLayoutManager);
        categoriesAdapter = new CategoriesAdapter(getActivity(), SplashScreen.categoryListResponseData);
        recyclerview.setAdapter(categoriesAdapter);
    }

}
