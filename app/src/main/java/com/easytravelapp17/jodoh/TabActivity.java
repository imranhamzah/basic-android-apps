package com.easytravelapp17.jodoh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.StrictMode;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TabActivity extends Fragment {

    ListView onlineUserListView;
    ListAdapter onlineUserListAdapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<String> arrayList = null;

    //Start for list view on main page
    private ProgressDialog m_ProgressDialog = null;
    private Runnable viewUsers;
    private ArrayList<UserList> m_users = null;
    private UserListAdapter m_user_list_adapter;

    DatabaseHelper userDb;
    //End for list view on main page

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_online,container,false);

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        m_users = new ArrayList<>();

        try {
            URL onlineUsers = new URL(getString(R.string.OnlineURL));
            urlConnection = (HttpURLConnection) onlineUsers.openConnection();

            StringBuffer buffer = new StringBuffer();
            InputStream stream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String jsonString = buffer.toString();
            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;

            while (count < jsonArray.length()) {
                UserList userList = new UserList();
                JSONObject JO = jsonArray.getJSONObject(count);
                userList.setUser_name(JO.getString("user_name"));
                userList.setFull_name(JO.getString("fullname"));
                m_users.add(userList);
                count++;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            try {
                if (reader != null) {
                    reader.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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