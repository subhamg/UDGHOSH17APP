package com.iitk.udghosh.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iitk.udghosh.R;
import com.iitk.udghosh.models.Event;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EventPageFragment extends Fragment {
//    GridView eventsListAdapter;
//    ArrayList<Event> eventsList;

    // Progress Dialog
    private ProgressDialog pDialog;
    private static String file_url;
    public static final int progress_bar_type = 0;
    private DatabaseReference mDatabase, eventsRef, eventRef;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private TextView event_data;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "eventName";
    private static final String ARG_PARAM2 = "linkName";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventPageFragment() {
        // Required empty public constructor
    }

    public static EventPageFragment newInstance(String data, String linkN) {
        EventPageFragment fragment = new EventPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, data);
        args.putString(ARG_PARAM2, linkN);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_event_page, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final com.getbase.floatingactionbutton.FloatingActionButton res = (com.getbase.floatingactionbutton.FloatingActionButton) rootView.findViewById(R.id.res);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder_live = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent_live = builder_live.build();
                customTabsIntent_live.launchUrl(getActivity().getBaseContext(), Uri.parse("https://160298.000webhostapp.com/ud/" + mParam2 + "_res.pdf"));
            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton sch = (com.getbase.floatingactionbutton.FloatingActionButton) rootView.findViewById(R.id.sch);
        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder_live = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent_live = builder_live.build();
                customTabsIntent_live.launchUrl(getActivity().getBaseContext(), Uri.parse("https://160298.000webhostapp.com/ud/" + mParam2 + "_sch.pdf"));

            }
        });

        final com.getbase.floatingactionbutton.FloatingActionButton rul = (com.getbase.floatingactionbutton.FloatingActionButton) rootView.findViewById(R.id.rul);
        rul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder_live = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent_live = builder_live.build();
                customTabsIntent_live.launchUrl(getActivity().getBaseContext(), Uri.parse("https://160298.000webhostapp.com/ud/" + mParam2 + "_rul.pdf"));
            }
        });

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.event_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // DownloadLink
//        FloatingActionButton event_download = (FloatingActionButton) rootView.findViewById(R.id.event_download);
//        event_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



        loadEntries(rootView);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


//        EventsAdapter myAdapter=new EventsAdapter(getContext(), R.layout.grid_view_items, eventsList);
//        eventsListAdapter.setAdapter(myAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    public void downloadFile(final String url, final String fileName){
        String DownloadUrl = url;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request.setDescription("sample pdf file for testing");   //appears the same in Notification bar while downloading
        request.setTitle("Sample.pdf");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalFilesDir(getApplicationContext(),null, fileName);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui","com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void loadEntries(final View view) {
//        eventsList = new ArrayList<>();

        eventsRef = mDatabase.child("events");
        eventRef = mDatabase.child("events").child(mParam1);   // Event Name is the string to be passed as intent extra

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);

                // Set values to XML here.
                event_data = (TextView) view.findViewById(R.id.event_data);

                event_data.setText(event.Data);
                CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.event_toolbar_layout);
                toolbarLayout.setTitle(mParam1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("LogFragment", "loadLog:onCancelled", databaseError.toException());
            }
        };
        eventRef.addValueEventListener(valueEventListener);
    }
}
