package com.appng.projectaura;

import android.app.Activity;
import android.os.Bundle;

import view.GameView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        GameView gameView = new GameView(this);
        this.setContentView(gameView);
    }
}