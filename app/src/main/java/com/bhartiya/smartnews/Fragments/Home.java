package com.bhartiya.smartnews.Fragments;


import android.os.Bundle;

import com.bhartiya.smartnews.MainActivity;
import com.bhartiya.smartnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    View view;

    @BindView(R.id.simpleTabLayout)
    TabLayout simpleTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    ViewPager.OnPageChangeListener mOnPageChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        MainActivity.drawerLayout.closeDrawers(); // close drawer
        createTabs(); // create custom tabs
        return view;
    }

    private void createTabs() {
        setupViewPager(viewPager);
        simpleTabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LatestNews(), "Latest News");
        adapter.addFragment(new Categories(), "Categories");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
