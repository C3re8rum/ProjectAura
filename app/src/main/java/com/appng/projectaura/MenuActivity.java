package com.appng.projectaura;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import sound.BackgroundMusicService;
import view.GameView;

public class MenuActivity extends Activity {

    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.appng.projectaura";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        }

    public void startGame(View view){
        String difficulty = preferences.getString("Difficulty", "EASY");
        GameView gameView = new GameView(this, difficulty);
        this.setContentView(gameView);
        this.playBackgroundMusic(gameView);
    }

    public void goToOptions(View view){
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void playBackgroundMusic (View view) {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        Log.d("createService", "Entered PlayBackgroundMusic");
        startService(intent);
        Log.d("createService", "Started BackgroundMusic");
    }
}