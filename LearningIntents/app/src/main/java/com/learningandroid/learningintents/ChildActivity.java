package com.learningandroid.learningintents;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class ChildActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btnStartCall;
    private Button btnViewLocation;
    private Button btnOpenWebPage;
    private Button btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        btnStartCall = this.findViewById(R.id.btn_start_call);
        btnStartCall.setOnClickListener(this);

        btnViewLocation = this.findViewById(R.id.btn_view_location);
        btnViewLocation.setOnClickListener(this);

        btnOpenWebPage = this.findViewById(R.id.btn_open_web_page);
        btnOpenWebPage.setOnClickListener(this);

        btnSendEmail = this.findViewById(R.id.btn_send_email);
        btnSendEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = null;

        switch (view.getId())
        {
            case R.id.btn_start_call :
                Uri number = Uri.parse("tel:9167365421");
                intent = new Intent(Intent.ACTION_DIAL, number);
                break;
            case R.id.btn_view_location :
                Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
                intent = new Intent(Intent.ACTION_VIEW, location);
                break;
            case R.id.btn_open_web_page :
                Uri webpage = Uri.parse("http://www.android.com");
                intent = new Intent(Intent.ACTION_VIEW, webpage);
                break;
            case R.id.btn_send_email :
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jon@example.com"}); // recipients
                intent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Email message text");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                break;
        }

        if(intent != null)
        {
            //checking if some app exists to handle this intent
            PackageManager packageManager = getPackageManager();
            List activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            if(activities.size() > 0)
            {
                this.startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "No activity found for this action.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
