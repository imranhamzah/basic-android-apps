package com.easytravelapp17.jodoh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("User List");

        ListView userListView = (ListView) findViewById(R.id.userListView);
        ArrayList<String> userList = new ArrayList<String>();

        userList.add("Name 1");
        userList.add("Name 2");
        userList.add("Name 3");
        userList.add("Name 4");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(arrayAdapter);
    }
}
