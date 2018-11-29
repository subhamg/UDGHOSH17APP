package com.iitk.udghosh.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.iitk.udghosh.MainActivity;
import com.iitk.udghosh.models.Event;

import com.iitk.udghosh.R;
import com.iitk.udghosh.adapters.EventsAdapter;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/*
 *  Created by hiteshkr on 03/10/17
 */


public class OneFragment extends Fragment{
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    GridView eventsListAdapter;
    ArrayList<Event> eventsList = new ArrayList<>();

    private DatabaseReference mDatabase;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_one, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
//                    try {
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                    }
//                    catch (Exception e) {
//                        System.out.print(e.getMessage());
//                    }

                } else {
                    // User is signed out, ok
                    Log.d(TAG, "onAuthStateChanged:signed_in");

                }
                // ...
            }
        };
        auth.addAuthStateListener(authListener);

        eventsListAdapter =  rootView.findViewById(R.id.eventsGridView);

        if(eventsList.isEmpty()) {
            eventsList.add(new Event("Athletics", R.drawable.athletic, "Football", 0));
            eventsList.add(new Event("Football", R.drawable.football1, "Football", 0));
            eventsList.add(new Event("Badminton", R.drawable.badminton1, "Badminton", 0));
            eventsList.add(new Event("Basketball", R.drawable.basketball1, "Basketball", 0));
            eventsList.add(new Event("Carrom", R.drawable.carom, "Carom", 0));
            eventsList.add(new Event("Chess", R.drawable.chess, "Chess", 0));
            eventsList.add(new Event("Cricket", R.drawable.cricket1, "Cricket", 0));
            eventsList.add(new Event("Handball", R.drawable.handball, "Handball", 0));
            eventsList.add(new Event("Hockey", R.drawable.hockey1, "Hockey", 0));
            eventsList.add(new Event("Kabaddi", R.drawable.kabaddi, "Kabaddi", 0));
            eventsList.add(new Event("Kho-kho", R.drawable.khokho, "KhoKho", 0));
            eventsList.add(new Event("Lawn Tennis", R.drawable.lawntennis1, "Lawn_Tennis", 0));
            eventsList.add(new Event("Weightlifting", R.drawable.weightlifting, "Lifting", 0));
            eventsList.add(new Event("Powerlifting", R.drawable.powerlifting1, "Lifting", 0));
            eventsList.add(new Event("Skating", R.drawable.squash, "Skating", 0));
            eventsList.add(new Event("Sports Quiz", R.drawable.sportsquiz, "Sports_Quiz", 0));
            eventsList.add(new Event("Squash", R.drawable.squash, "Squash", 0));
            eventsList.add(new Event("Swimming", R.drawable.squash, "Swimming", 0));
            eventsList.add(new Event("Table Tennis", R.drawable.tabletennis, "Table_Tennis", 0));
            eventsList.add(new Event("Taekwando", R.drawable.squash, "Taekwando", 0));
            eventsList.add(new Event("Volleyball", R.drawable.volleyball1, "Volleyball", 0));
            eventsList.add(new Event("Mr Udghosh", R.drawable.mrudghosh, "Volleyball", 0));
        }

        EventsAdapter myAdapter=new EventsAdapter(getContext(), R.layout.grid_view_items, eventsList);
        eventsListAdapter.setAdapter(myAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

}
