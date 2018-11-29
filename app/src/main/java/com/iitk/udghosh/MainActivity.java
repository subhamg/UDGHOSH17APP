package com.iitk.udghosh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;


/**
 * Created by hiteshkr on 01/10/17.
 */

public class MainActivity extends AppCompatActivity  {

    private LoginButton loginButton;
    private Intent intent;
    Handler handler, handler2;
    private TextView signUp;
    private EditText emailW, passwordW;
    private Button loginBtnW;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private CallbackManager callbackManager;
    private static int RC_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    private boolean gs = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = (TextView) findViewById(R.id.sign_up);
        emailW = (EditText) findViewById(R.id.email);
        passwordW = (EditText) findViewById(R.id.password);
        loginBtnW = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_in:");

                } else if(isLoggedIn()) {
                    // User is signed in, ok
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                // ...
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });


        loginBtnW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailW.getText().toString();
                final String password = passwordW.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        passwordW.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));

//                                    new Thread(new Runnable() {
//                                        public void run() {
//                                            // a potentially  time consuming task
//                                            final Intent intent2 =
//                                                    new Intent(MainActivity.this, HomeActivity.class);
//                                            handler2.post(new Runnable() {
//                                                public void run() {
//                                                    startActivity(intent2);
//                                                    finish();
//                                                }
//                                            });
//                                        }
//                                    }).start();

                                }
                            }
                        });
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gs = true;
                signIn();
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        //

    }



    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(gs) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    // Google Sign In failed.
                }
            }

            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
        } else {
            Toast.makeText(getApplicationContext(), "Unable to login please check your internet connection",Toast.LENGTH_LONG).show();
        }
    }
    //

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithFacebook(AccessToken token) {
        Log.d(TAG, "signInWithFacebook:" + token);

//        showProgressDialog();
        progressBar.setVisibility(View.VISIBLE);


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            String uid=task.getResult().getUser().getUid();
                            String name=task.getResult().getUser().getDisplayName();
                            String email=task.getResult().getUser().getEmail();
                            String image=task.getResult().getUser().getPhotoUrl().toString();

                            //Create a new User and Save it in Firebase database
//                            User user = new User(uid,name,null,email,null);

//                            mRef.child(uid).setValue(user);
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));

//                            new Thread(new Runnable() {
//                                public void run() {
//                                    // a potentially  time consuming task
//                                    final Intent intent0 =
//                                            new Intent(getApplicationContext(), HomeActivity.class);
//                                    handler.post(new Runnable() {
//                                        public void run() {
//                                            startActivity(intent0);
//                                            finish();
//                                        }
//                                    });
//                                }
//                            }).start();

//                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                            intent.putExtra("user_id",uid);
//                            intent.putExtra("profile_picture",image);
//                            startActivity(intent);
//                            finish();

                        }

//                        hideProgressDialog();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }



//    @Override
//    public Loader<Object> onCreateLoader(int i, Bundle bundle){
//        return null; // TODO
//    }
//
//    @Override
//    public void onLoadFinished(Loader loader, Object o) {
//        Toast.makeText(this, "onLoadFinished", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onLoaderReset(Loader loader)    {
//        Toast.makeText(this, "onLoaderReset", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isLoggedIn()){
//            Intent home = new Intent(this, HomeActivity.class);
//            startActivity(home);
//        }
//    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        FirebaseUser user = auth.getCurrentUser();
        return (accessToken != null)||(user != null);
    }

}
