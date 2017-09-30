package com.practice.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<String> mItemsTodo;
    private ArrayAdapter mListAdapter;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) this.findViewById(R.id.lv_todo_list);
        mItemsTodo = new ArrayList<>();
        mListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mItemsTodo);
        listView.setAdapter(mListAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                mItemsTodo.remove(position);
                mListAdapter.notifyDataSetChanged();

                if(toast != null)
                {
                    toast.cancel();
                }

                toast = Toast.makeText(MainActivity.this, R.string.deleted_item_toast_msg, Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
    }

    public void onAddTodoItem(View view)
    {
        EditText editText = (EditText) this.findViewById(R.id.et_todo_input_field);
        String todoItemString = editText.getText().toString();
        if(!todoItemString.isEmpty())
        {
            editText.setText("");
            mItemsTodo.add(todoItemString);
            mListAdapter.notifyDataSetChanged();
        }
    }
}
