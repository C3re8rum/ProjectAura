package com.appng.projectaura;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class YouDiedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_died);
    }

    public void returnToMenu(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}