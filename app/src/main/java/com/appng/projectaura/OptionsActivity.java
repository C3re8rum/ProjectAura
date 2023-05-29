package com.appng.projectaura;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;

public class OptionsActivity extends Activity {

    private RadioButton btnEasy, btnNormal, btnHard;
    private SeekBar soundBar;

    private SharedPreferences preferences;
    private String sharedPrefFile = "com.appng.projectaura";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);


        this.btnEasy = (RadioButton) findViewById(R.id.rbtnEasy);
        this.btnNormal = (RadioButton) findViewById(R.id.rbtnNormal);
        this.btnHard = (RadioButton) findViewById(R.id.rbtnHard);
        this.soundBar = (SeekBar) findViewById(R.id.barSoundLevel);

        readData();
    }

    private void readData() {
        String currentlyChecked = preferences.getString("Difficulty", "EASY");

        switch (currentlyChecked) {
            case "EASY":
                if (!btnEasy.isChecked()) {
                    btnEasy.toggle();
                }
                break;
            case "NORMAL":
                if (!btnNormal.isChecked()) {
                    btnNormal.toggle();
                }
                break;
            case "HARD":
                if (!btnHard.isChecked()) {
                    btnHard.toggle();
                }
                break;
        }

        int volume = preferences.getInt("SOUNDLEVEL", 25);
        soundBar.setProgress(volume);
    }

    public void updateDifficulty() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        String difficulty = "";

        if (btnEasy.isChecked()) {
            difficulty = "EASY";
        } else if (btnNormal.isChecked()) {
            difficulty = "NORMAL";
        } else if (btnHard.isChecked()) {
            difficulty = "HARD";
        }

        preferencesEditor.putString("Difficulty", difficulty);
        preferencesEditor.apply();


    }

    public void updateSoundLevel() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        SeekBar seekBar = findViewById(R.id.barSoundLevel);
        int soundLevel = seekBar.getProgress();
        preferencesEditor.putInt("SOUNDLEVEL", soundLevel);
        preferencesEditor.apply();
    }

    public void goToMenu(View view) {
        updateDifficulty();
        updateSoundLevel();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}