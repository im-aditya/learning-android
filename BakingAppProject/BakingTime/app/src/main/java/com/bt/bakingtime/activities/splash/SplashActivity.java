package com.bt.bakingtime.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bt.bakingtime.activities.main.MainActivity;

/**
 * Created by Aditya on 8/14/2017.
 */

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
