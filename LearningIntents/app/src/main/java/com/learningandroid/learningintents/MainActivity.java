package com.learningandroid.learningintents;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    static final int PICK_CONTACT_REQUEST = 1;  // request code

    private Button btnStartActivity;
    private Button btnStartActivityForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO : start a service
        //TODO : start a bounded service
        //TODO : deliver a broadcast
        //TODO : pending intent

        btnStartActivity = this.findViewById(R.id.btn_start_activity);
        btnStartActivityForResult = this.findViewById(R.id.btn_start_activity_for_result);

        btnStartActivity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //starting another activity
                startChildActivity();
            }
        });

        btnStartActivityForResult.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //starting another activity for result
                startChildActivityForResult();
            }
        });
    }

    private void startChildActivity()
    {
        Intent intent = new Intent(this, ChildActivity.class);
        this.startActivity(intent);
    }

    private void startChildActivityForResult()
    {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        this.startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == PICK_CONTACT_REQUEST) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Data received: " + data.getDataString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User canceled the action..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
