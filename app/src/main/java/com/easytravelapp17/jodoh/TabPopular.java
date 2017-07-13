package com.easytravelapp17.jodoh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TabPopular extends Fragment {

    //Start for list view on main page
    private ProgressDialog m_ProgressDialog = null;
    private Runnable viewUsers;
    private ArrayList<UserList> m_users = null;
    private UserListAdapter m_user_list_adapter;

    DatabaseHelper userDb;
    //End for list view on main page

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_popular,container,false);

        //Start for user list view
        ListView userListView = (ListView) rootView.findViewById(android.R.id.list);


        //Custom layout for list view
        m_users = new ArrayList<>();


        this.m_user_list_adapter = new UserListAdapter(getActivity().getApplicationContext(), R.layout.user_list_item, m_users);
        userListView.setAdapter(this.m_user_list_adapter);


        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserList userList = (UserList)parent.getItemAtPosition(position);

                String username = userList.getUser_name();
                Integer userId = userList.getUserId();

                Intent i = new Intent(getActivity().getApplicationContext(),ProfileActivity.class);
                i.putExtra("username",username);
                i.putExtra("userId",userId);
                startActivity(i);

            }
        });


        //Trying to get orders if available
        viewUsers = new Runnable(){
            @Override
            public void run() {
                getUserList();
            }
        };

        Thread thread =  new Thread(null, viewUsers, "MagentoBackground");
        thread.start();
//        m_ProgressDialog = ProgressDialog.show(getActivity().getApplicationContext(),
//                "Please wait...", "Retrieving data ...", true);


        return rootView;
    }

    private void getUserList()
    {
        m_users = new ArrayList<>();

        userDb = new DatabaseHelper(getActivity().getApplicationContext());

        Cursor userRes = userDb.getAllData();
        if(userRes.getCount() == 0)
        {
            //Show display message no users registered
        }

        while (userRes.moveToNext())
        {
            UserList userList = new UserList();
            userList.setUser_name(userRes.getString(1));
            userList.setFull_name(userRes.getString(2));
            m_users.add(userList);
        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        getActivity().runOnUiThread(returnUserRes);
    }

    private Runnable returnUserRes = new Runnable() {

        @Override
        public void run() {
            if(m_users != null && m_users.size() > 0){
                m_user_list_adapter.notifyDataSetChanged();
                for(int i=0;i<m_users.size();i++)
                    m_user_list_adapter.add(m_users.get(i));
            }
//            m_ProgressDialog.dismiss();
            m_user_list_adapter.notifyDataSetChanged();
        }
    };


    private class UserListAdapter extends ArrayAdapter<UserList> {

        private ArrayList<UserList> items;
        public UserListAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<UserList> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(v == null)
            {
                LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.user_list_item,null);
            }

            UserList userList = items.get(position);
            if(userList != null)
            {
                TextView usernameTextView = (TextView) v.findViewById(R.id.usernameTextView);
                TextView fullnameTextViewFeatured = (TextView) v.findViewById(R.id.fullnameTextViewFeatured);
                if(usernameTextView != null)
                {
                    usernameTextView.setText(userList.getUser_name());
                }
                if(fullnameTextViewFeatured != null)
                {
                    fullnameTextViewFeatured.setText(userList.getFull_name());
                }
            }

            return v;
        }
    }
}