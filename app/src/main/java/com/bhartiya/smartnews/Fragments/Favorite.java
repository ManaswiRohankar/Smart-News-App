package com.bhartiya.smartnews.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.bhartiya.smartnews.Adapters.NewsListAdapter;
import com.bhartiya.smartnews.MVP.NewsListResponse;
import com.bhartiya.smartnews.MainActivity;
import com.bhartiya.smartnews.R;
import com.bhartiya.smartnews.SplashScreen;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Favorite extends Fragment {

    View view;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.empty)
    TextView empty;
    public static ArrayList<NewsListResponse> imagesList;
    public static ArrayList<NewsListResponse> imagesList1;
    public static NewsListAdapter newsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        MainActivity.searchView.setVisibility(View.GONE);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {
        // get favorite list data and set it in RecyclerView
        Log.d("arrayListSize", MainActivity.imageIds.size() + "");
        imagesList = new ArrayList<>();
        imagesList1 = new ArrayList<>();
        imagesList.addAll(SplashScreen.newsListResponsesData);
        for (int j = 0; j < imagesList.size(); j++) {

            for (int k = 0; k < MainActivity.imageIds.size(); k++) {
                Log.d("compare", imagesList.get(j).getId().trim() + " " + MainActivity.imageIds.get(k).trim());
                if (imagesList.get(j).getId().trim().equalsIgnoreCase(MainActivity.imageIds.get(k).trim())) {
                    imagesList1.add(imagesList.get(j));

                }
            }

        }
        Log.d("imagesSize", imagesList1.size() + "");
        if (imagesList1.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(linearLayoutManager);
        newsListAdapter = new NewsListAdapter(getActivity(), imagesList1);
        recyclerview.setAdapter(newsListAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.searchView.setVisibility(View.VISIBLE);

    }
}
