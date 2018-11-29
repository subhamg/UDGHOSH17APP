package com.iitk.udghosh.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iitk.udghosh.HomeActivity;
import com.iitk.udghosh.MainActivity;
import com.iitk.udghosh.R;
import com.iitk.udghosh.adapters.InfoCardsAdapter;
import com.iitk.udghosh.models.Event;
import com.iitk.udghosh.models.InfoCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by hiteshkr on 03/10/17.
 */

public class TwoFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mDemoSlider;
    private RecyclerView recyclerView;
    private InfoCardsAdapter adapter;
    private List<InfoCard> infoCardList;
    private Handler handler;

    private TextView titlenew, stitlenew;

    private DatabaseReference mDatabase, otherRef, backdrop, caraouselRef;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_two, container, false);
        mDemoSlider = (SliderLayout)rootView.findViewById(R.id.slider);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        caraouselRef = mDatabase.child("caraousel");
        final TextSliderView textSliderView = new TextSliderView(getContext());

        final HashMap<String,String> url_maps = new HashMap<String, String>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Set values to XML here.
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = (String) ds.child("name").getValue();
                    String link = (String) ds.child("link").getValue();
                    url_maps.put(name, link);

                }

                setimg(url_maps, textSliderView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LogFragment", "loadLog:onCancelled", databaseError.toException());
            }
        };
        caraouselRef.addValueEventListener(valueEventListener);





        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");


//        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Hannibal",R.drawable.hannibal);
//        file_maps.put("Big Bang Theory",R.drawable.bigbang);
//        file_maps.put("House of Cards",R.drawable.house);
//        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);




        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2500);
        mDemoSlider.addOnPageChangeListener(this);
//        ListView l = (ListView)rootView.findViewById(R.id.transformers);
//        l.setAdapter(new TransformerAdapter(this));
//        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
//                Toast.makeText(getActivity(), ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });


        FirebaseAuth auth;
        FirebaseAuth.AuthStateListener authListener;
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
//                    startActivity(new Intent(getContext(), MainActivity.class));

                } else {
                    // User is signed in, ok
                    Log.d(TAG, "onAuthStateChanged:signed_in");

                }
                // ...
            }
        };
        auth.addAuthStateListener(authListener);

        titlenew = (TextView) rootView.findViewById(R.id.backrop_title);
        stitlenew = (TextView) rootView.findViewById(R.id.backdrop_subtitle);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_card);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        initCollapsingToolbar(rootView);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        infoCardList = new ArrayList<>();
        adapter = new InfoCardsAdapter(getContext(), infoCardList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        try {

            mDatabase = FirebaseDatabase.getInstance().getReference();
            backdrop = mDatabase.child("backdrop");
            ValueEventListener valueEventListenerBackdrop = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ds) {

                    String backdropImage = (String) ds.child("url").getValue();
                    String backdropTitle = (String) ds.child("title").getValue();
                    String backdropst = (String) ds.child("subtitle").getValue();

                    titlenew.setText(backdropTitle);
                    stitlenew.setText(backdropst);
                    Glide.with(getActivity()).load(Uri.parse(backdropImage)).into((ImageView) rootView.findViewById(R.id.backdrop));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("LogFragment", "loadLog:onCancelled", databaseError.toException());
                }
            };
            backdrop.addValueEventListener(valueEventListenerBackdrop);



        } catch (Exception e) {
            e.printStackTrace();
        }




        return rootView;
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar(final View view) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Trending");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void setimg(HashMap<String,String> url_maps, TextSliderView textSliderView) {
        for(String name : url_maps.keySet()) {

            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);

        }
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.logo,
                };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        otherRef = mDatabase.child("others");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Set values to XML here.
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = (String) ds.child("name").getValue();
                    String thumbnail = (String) ds.child("thumbnail").getValue();
                    infoCardList.add(new InfoCard(name, thumbnail));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LogFragment", "loadLog:onCancelled", databaseError.toException());
            }
        };
        otherRef.addValueEventListener(valueEventListener);


    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getApplicationContext(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        final View rootView = inflater.inflate(R.layout.fragment_two, container, false);
//        switch (item.getItemId()){
//            case R.id.custom_indicator:
//                mDemoSlider.setCustomIndicator((PagerIndicator) rootView.findViewById(R.id.custom_indicator));
//                break;
////            case R.id.action_custom_child_animation:
////                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
////                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

}
