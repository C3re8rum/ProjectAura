package com.appng.projectaura;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import sound.BackgroundMusicService;
import view.GameView;

public class menuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        }

    public void startGame(View view){
        GameView gameView = new GameView(this);
        this.setContentView(gameView);
        this.playBackgroundMusic(gameView);
    }

    public void playBackgroundMusic (View view) {
        Intent intent = new Intent(this, BackgroundMusicService.class);
        Log.d("createService", "Entered PlayBackgroundMusic");
        startService(intent);
        Log.d("createService", "Started BackgroundMusic");
    }
}