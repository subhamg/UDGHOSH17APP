package com.iitk.udghosh;

import android.app.Activity;

import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.*;
import android.widget.VideoView;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            VideoView videoHolder = new VideoView(this);
            DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
            android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoHolder.getLayoutParams();
            params.width =  metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            videoHolder.setLayoutParams(params);
            setContentView(videoHolder);
            Uri video = Uri.parse("https://160298.000webhostapp.com/ud/giphy.mp4");
            videoHolder.setVideoURI(video);

            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    jump();
                }
            });
            videoHolder.start();
        } catch (Exception ex) {
            jump();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}