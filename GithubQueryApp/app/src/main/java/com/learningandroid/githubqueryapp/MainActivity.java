package com.learningandroid.githubqueryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    //How to add a menu to the app's Action Bar

    //1. Add a 'menu' resource directory to the res folder
    //2. Add a 'menu' resource file to the menu folder
    //3a. Add an 'item' to the menu resource file
    //3b. Assign id, title, showAsAction and orderInCategory attributes
    //4. Override onCreateOptionsMenu to inflate the menu
    //5. Override onOptionsItemSelected to handle menu options clicks

    private EditText mQueryStringField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueryStringField = this.findViewById(R.id.et_query_field);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int menuItemId = item.getItemId();
        if(menuItemId == R.id.menu_search)
        {
            if(mQueryStringField.getText().length() != 0)
            {
                Toast.makeText(this, "Search: " + mQueryStringField.getText(), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Search string is empty !!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return false;
    }
}
