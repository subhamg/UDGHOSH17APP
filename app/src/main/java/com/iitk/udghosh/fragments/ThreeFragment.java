package com.iitk.udghosh.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iitk.udghosh.HomeActivity;
import com.iitk.udghosh.MainActivity;
import com.iitk.udghosh.Misc.About;
import com.iitk.udghosh.Misc.FAQActivity;
import com.iitk.udghosh.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by hiteshkr on 03/10/17.
 */

public class ThreeFragment extends Fragment{
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleApiClient mGoogleApiClient;

    private String[] mNames = {  "Live Updates", "About Udghosh", "Contacts", "FAQ", "Sign Out", "Feedback", "Rate the App" };

    int[] flags = new int[]{
            R.drawable.ic_reg_live,
            R.drawable.ic_reg_info,
            R.drawable.ic_reg_contact,
            R.drawable.ic_reg_help,
            R.drawable.ic_reg_signout,
            R.drawable.ic_reg_feedback,
            R.drawable.ic_reg_rate,


    };

    int[] mArrow = new int[]{
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow,
            R.drawable.ic_reg_rightarrow
    };
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_in:");
//                    startActivity(new Intent(getActivity(), MainActivity.class));

                } else {
                    // User is signed in, ok
                    Log.d(TAG, "onAuthStateChanged:signed_in");

                }
                // ...
            }
        };

        auth.addAuthStateListener(authListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<7;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt",  mNames[i]);
            hm.put("cur",Integer.toString(mArrow[i]));
            hm.put("flag", Integer.toString(flags[i]) );
            aList.add(hm);
        }
        String[] from = { "flag","txt","cur" };

        int[] to = { R.id.flag,R.id.txt,R.id.cur,R.id.textView};

        final View v = inflater.inflate(R.layout.fragment_three, container,false);

        ListView list = (ListView)v.findViewById(R.id.listView1);
        SimpleAdapter adapter = new SimpleAdapter(getContext(), aList, R.layout.listview_layout, from, to);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Open the browser here

                if(position == 4) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Sign Out")
                            .setMessage("Are you sure you want to sign out?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    if(AccessToken.getCurrentAccessToken() != null && auth.getCurrentUser() != null) {
//
//                                    } else if(AccessToken.getCurrentAccessToken() == null) {
//                                        auth.signOut();
//                                    } else if(auth.getCurrentUser() == null) {
//                                        LoginManager.getInstance().logOut();
//                                    } else {
//                                        auth.signOut();
//                                        LoginManager.getInstance().logOut();
//                                    }
//                                    if(AccessToken.getCurrentAccessToken() != null) {
//                                        LoginManager.getInstance().logOut();
//                                        startActivity(new Intent(getActivity(), MainActivity.class));
//
//                                    }

                                    LoginManager.getInstance().logOut();
                                    FirebaseAuth.getInstance().signOut();
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                            new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(Status status) {
                                                    startActivity(new Intent(getContext(), MainActivity.class));
                                                }
                                            });
                                    startActivity(new Intent(getContext(), MainActivity.class));




                                }
                            }).setNegativeButton("No", null).show();

                } else if(position == 2) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
//                    builder.setToolbarColor(0xFF03396C);
//                    builder.setStartAnimations(getActivity().getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
//                    builder.setExitAnimations(getActivity().getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                    customTabsIntent.launchUrl(getActivity().getBaseContext(), Uri.parse("http://www.udghosh.org/Team/index1.html"));
                } else if(position == 5) {

                    CustomTabsIntent.Builder builder_gf = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent_gf = builder_gf.build();
//                    builder_gf.setToolbarColor(0xFF03396C);
//                    builder_gf.setStartAnimations(getActivity().getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
//                    builder_gf.setExitAnimations(getActivity().getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                    customTabsIntent_gf.launchUrl(getActivity().getBaseContext(), Uri.parse("https://goo.gl/forms/CrVBd2yEIfhM9Hz53"));

                } else if(position == 1) {
                    Intent about = new Intent(getContext(), About.class);
                    startActivity(about);
                } else if(position == 3) {
                    Intent faq = new Intent(getContext(), FAQActivity.class);
                    startActivity(faq);
                } else if(position == 0) {
                    CustomTabsIntent.Builder builder_live = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent_live = builder_live.build();
//                    builder_live.setToolbarColor(0xFF03396C);
//                    builder_live.setStartAnimations(getActivity().getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
//                    builder_live.setExitAnimations(getActivity().getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                    customTabsIntent_live.launchUrl(getActivity().getBaseContext(), Uri.parse("http://www.udghosh.org/Live/index1.html"));

                } else if(position == 7) {

                }

            }

        });

        list.setAdapter(adapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (authListener != null) {
////            auth.removeAuthStateListener(authListener);
//        }
    }
}
