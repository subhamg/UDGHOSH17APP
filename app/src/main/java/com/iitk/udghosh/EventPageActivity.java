package com.iitk.udghosh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.iitk.udghosh.fragments.EventPageFragment;

import java.util.ArrayList;
import java.util.List;

public class EventPageActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private String eventName;
    private String ln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        eventName = getIntent().getStringExtra("data");
        ln = getIntent().getStringExtra("linkN");

        viewPager = (ViewPager) findViewById(R.id.event_viewpager);
        setupViewPager(viewPager, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager, Bundle savedInstanceState) {
        EventPagerAdapter eventAdapter = new EventPagerAdapter(getSupportFragmentManager());

        eventAdapter.addFrag(EventPageFragment.newInstance(eventName, ln));

        viewPager.setAdapter(eventAdapter);
    }

    private class EventPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();

        private EventPagerAdapter(FragmentManager manager) {
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

        private void addFrag(Fragment fragment) {

            if(mFragmentList.isEmpty()){
                mFragmentList.add(fragment);
//              mFragmentTitleList.add(title);
            }

        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }

    }
}
