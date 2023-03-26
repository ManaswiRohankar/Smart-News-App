package com.bhartiya.smartnews.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bhartiya.smartnews.MainActivity;
import com.bhartiya.smartnews.R;

public class ContactUs extends Fragment {

    View view;
    String fileName = "contactus.html";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        MainActivity.share.setVisibility(View.INVISIBLE);
        MainActivity.searchView.setVisibility(View.INVISIBLE);
        // display content of local HTML file
        WebView webView = (WebView) view.findViewById(R.id.simpleWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings=webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.loadUrl("file:///android_asset/" + fileName);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.share.setVisibility(View.VISIBLE);
        MainActivity.searchView.setVisibility(View.VISIBLE);
    }
}
