package com.bhartiya.smartnews;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bhartiya.smartnews.Fragments.Categories;
import com.bhartiya.smartnews.Fragments.Home;
import com.bhartiya.smartnews.Fragments.LatestNews;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    public static DrawerLayout drawerLayout;
    public static List<String> menuTitles;
    public static ArrayList<Integer> menuIcons = new ArrayList<>(Arrays.asList(R.drawable.home_icon, R.drawable.star_icon, R.drawable.contact_icon, R.drawable.about_icon));
    public static CustomDrawerAdapter customDrawerAdapter;
    private AdView mAdView;
    public static ImageView menu, share, search;
    public static TextView title;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static SearchView searchView;
    public static ArrayList<String> imageIds = new ArrayList<>();
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //slider image
        sliderLayout = findViewById(R.id.imageSlider);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        menuTitles = Arrays.asList(getResources().getStringArray(R.array.menuArray));
        title = (TextView) findViewById(R.id.title);
        menu = (ImageView) findViewById(R.id.menu);
        share = (ImageView) findViewById(R.id.share);
        search = (ImageView) findViewById(R.id.search);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        searchView = (SearchView) findViewById(R.id.searchView);
        // customized searchView
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(id);
        searchEditText.setTextColor(getResources().getColor(R.color.color_white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.light_white));
        // display Banner Ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A9C93B8F0F67284AB70DF28784CA0F1C")
                .build();
        mAdView.loadAd(adRequest);
        // load home fragment
        loadFragment(new Home(), false);
        setRecyclerData(); // set drawer items
        // implement onQueryTextListener on searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // filter news list
                String text = s;
                LatestNews.newsListAdapter.filter(text);
                Categories.categoriesAdapter.filter(text);
                return false;
            }
        });
        displayFirebaseRegId(); // display firebase id
        getFavoriteData(); // get saved favorite list data
        sliderAnimation();
    }

    private void sliderAnimation() {
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(1);
        setSliderView();
    }

    private void setSliderView() {
        for(int i = 0; i <= 4 ; i++){
            DefaultSliderView sliderView = new DefaultSliderView(this);
            switch (i){
                case 0:
                    sliderView.setImageUrl("https://www.destinyhub.info/app_dashboard/slider1/1.png");
                    sliderView.setDescription("Bhartiya Mahavidhyalay Amravati");
                    break;
                case 1:
                    sliderView.setImageUrl("https://www.destinyhub.info/app_dashboard/slider1/2.png");
                    sliderView.setDescription("Annual Gathering");
                    break;
                case 2:
                    sliderView.setImageUrl("https://www.destinyhub.info/app_dashboard/slider1/3.png");
                    sliderView.setDescription("Students");
                    break;
                case 3:
                    sliderView.setImageUrl("https://www.destinyhub.info/app_dashboard/slider1/4.png");
                    sliderView.setDescription("College Campus");
                    break;
                case 4:
                    sliderView.setImageUrl("https://www.destinyhub.info/app_dashboard/slider1/5.png");
                    sliderView.setDescription("College");
                    break;

            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int myI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(MainActivity.this, "This is Slider "+(myI+1), Toast.LENGTH_SHORT).show();
                }
            });
            sliderLayout.addSliderView(sliderView);
        }
    }

    private void getFavoriteData() {
        sharedPreferences = getSharedPreferences("favoriteData", 0);
        editor = sharedPreferences.edit();
        Log.d("favoriteData", sharedPreferences.getString("data", "0"));
        String data = sharedPreferences.getString("data", "0");
        data = data.replace("[", "");
        data = data.replace("]", "");
        String[] array = data.split(", ");
        imageIds = new ArrayList<>(Arrays.asList(array));
        Log.d("arrayList", imageIds.toString().trim());
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("FCM", "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
        } else
            Log.d("Firebase", "Firebase Reg Id is not received yet!");
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void setRecyclerData() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        customDrawerAdapter = new CustomDrawerAdapter(MainActivity.this, menuTitles, menuIcons);
        recyclerView.setAdapter(customDrawerAdapter);
    }


    @OnClick({R.id.menuHomeImage, R.id.menu, R.id.share, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuHomeImage:
                drawerLayout.closeDrawers();
                CustomDrawerAdapter.selected_item = 0;
                customDrawerAdapter.notifyDataSetChanged();
                title.setText(menuTitles.get(0));
                loadFragment(new Home(), false);
                break;
            case R.id.menu:
                if (!MainActivity.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainActivity.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.share:
                shareApp();
                break;

        }
    }

    public void shareApp() {
        // share app with your friends
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this Smart News App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    @Override
    public void onBackPressed() {
        // double press to exit
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool)
            transaction.addToBackStack(null);
        transaction.commit();
    }

}