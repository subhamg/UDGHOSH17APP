package com.iitk.udghosh.fragments;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by hiteshkr on 09/10/17.
 */

public class FB extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        firebase.setAndroidContext(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

}
