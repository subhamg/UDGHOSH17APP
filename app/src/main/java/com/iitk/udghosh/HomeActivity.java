package com.iitk.udghosh;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iitk.udghosh.fragments.OneFragment;
import com.iitk.udghosh.fragments.ThreeFragment;
import com.iitk.udghosh.fragments.TwoFragment;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager, savedInstanceState);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
         //User this user to get information of the user !

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
//                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
//                    finish();
                    Log.d("user: ", "signed out");
                }
                else {
                    Log.d("user: ", "signed in");
                }
            }
        };
        auth.addAuthStateListener(authListener);    // Register Listener to the FirebaseAuth

    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabOne.setText("Events");
//        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_udh_event, 0, 0);
        tabLayout.getTabAt(0).setText("Events");
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_udh_event);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabTwo.setText("Trending");
//        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_udh_trend, 0, 0);
        tabLayout.getTabAt(1).setText("Trending");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_udh_trend);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
//        tabThree.setText("More");
//        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_udh_more, 0, 0);
        tabLayout.getTabAt(2).setText("More");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_udh_more);

        tabLayout.setTabTextColors(
                ContextCompat.getColor(HomeActivity.this, R.color.unselect),
                ContextCompat.getColor(HomeActivity.this, R.color.select)
        );


        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.rgb(3,57,108), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.rgb(94,98,102), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.rgb(94,98,102), PorterDuff.Mode.SRC_IN);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.rgb(3,57,108), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //for removing the color of first icon when switched to next tab
                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.rgb(94,98,102), PorterDuff.Mode.SRC_IN);
                //for other tabs
                tab.getIcon().setColorFilter(Color.rgb(94,98,102), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void setupViewPager(ViewPager viewPager, Bundle savedInstanceState) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.addFrag(new OneFragment(), "ONE");
            adapter.addFrag(new TwoFragment(), "TWO");
            adapter.addFrag(new ThreeFragment(), "THREE");



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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.appbar_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.appbar_signout) {
////            auth.signOut();
////
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }



}
