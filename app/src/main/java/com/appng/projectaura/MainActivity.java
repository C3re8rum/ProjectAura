package com.appng.projectaura;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import java.io.File;

import sound.BackgroundMusicService;
import view.GameView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        GameView gameView = new GameView(this);

        Log.i("SCREEN", "Width: " + getScreenWidth() + " Height: " + getScreenHeight());

        this.setContentView(gameView);
        this.playBackgroundmusic(gameView);
    }

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void playBackgroundmusic (View view) {
        Intent intent = new Intent(MainActivity.this, BackgroundMusicService.class);
        Log.d("createService", "Entered PlayBackgroundMusic");
        startService(intent);
        Log.d("createService", "Started BackgroundMusic");
    }
}