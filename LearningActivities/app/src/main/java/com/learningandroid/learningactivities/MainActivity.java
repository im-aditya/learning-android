package com.learningandroid.learningactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private List<String> mActivityStates;
    private TextView mInfoField;

    //onCreate
    //onStart
        //onRestoreInstanceState
    //onResume
    //onPause
        //onSaveInstanceState
    //onStop
        //onRestart
    //onDestroy

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoField = this.findViewById(R.id.tv_info_field);

        Button btnClear = this.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);

        mActivityStates = new ArrayList<>();
        mActivityStates.add("onCreate");

        updateInfoField();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mActivityStates.add("onStart");
        updateInfoField();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        mActivityStates.add("onResume");
        updateInfoField();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mActivityStates.add("onPause");
        updateInfoField();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        mActivityStates.add("onStop");
        updateInfoField();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        mActivityStates.add("onRestart");
        updateInfoField();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mActivityStates.add("onDestroy");
        updateInfoField();
    }

    private void updateInfoField()
    {
        String infoText = "";

        for (int i = 0; i < mActivityStates.size(); i++)
        {
            infoText += mActivityStates.get(i) + "\n";
        }

        mInfoField.setText(infoText);
    }

    @Override
    public void onClick(View view)
    {
        mActivityStates.clear();
        updateInfoField();
    }
}
