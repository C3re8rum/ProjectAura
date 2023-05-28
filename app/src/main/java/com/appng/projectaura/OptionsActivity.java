package com.appng.projectaura;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends Activity {

    private RadioButton btnEasy, btnNormal, btnHard;

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

        updateRadioButtons();
    }

    private void updateRadioButtons() {
        String currentlyChecked = preferences.getString("Difficulty", "EASY");

        switch (currentlyChecked){
            case "EASY":
                if (!btnEasy.isChecked()){
                    btnEasy.toggle();
                }
                break;
            case "NORMAL":
                if (!btnNormal.isChecked()){
                    btnNormal.toggle();
                }
                break;
            case "HARD":
                if (!btnHard.isChecked()){
                    btnHard.toggle();
                }
                break;
        }
    }

    public void updateDifficulty(){
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        String difficulty = "";

        if (btnEasy.isChecked()){
            difficulty = "EASY";
        } else if(btnNormal.isChecked()){
            difficulty = "NORMAL";
        } else if (btnHard.isChecked()){
            difficulty = "HARD";
        }


        preferencesEditor.putString("Difficulty", difficulty);
        preferencesEditor.apply();


    }

    public void goToMenu(View view){
        updateDifficulty();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}