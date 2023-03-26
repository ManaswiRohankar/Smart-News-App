package com.bhartiya.smartnews.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.bhartiya.smartnews.MainActivity;
import com.bhartiya.smartnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class About extends Fragment {

    View view;
    @BindView(R.id.emailId)
    TextView emailId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        MainActivity.share.setVisibility(View.INVISIBLE);
        MainActivity.searchView.setVisibility(View.INVISIBLE);
        return view;
    }

    @OnClick({R.id.emailLayout, R.id.rateLayout, R.id.moreLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emailLayout:
                // perform click on Email ID
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                final PackageManager pm = getActivity().getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                String className = null;
                for (final ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                        className = info.activityInfo.name;

                        if(className != null && !className.isEmpty()){
                            break;
                        }
                    }
                }
                emailIntent.setData(Uri.parse("mailto:"+emailId.getText().toString().trim()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Of Smart News App");
                emailIntent.setClassName("com.google.android.gm", className);
                try {
                    startActivity(emailIntent);
                } catch(ActivityNotFoundException ex) {
                    // handle error
                }
                break;
            case R.id.rateLayout:
                // perform click on Rate Item
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                break;
            case R.id.moreLayout:
                // display your other live apps
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=")));
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.share.setVisibility(View.VISIBLE);
        MainActivity.searchView.setVisibility(View.VISIBLE);
    }
}
