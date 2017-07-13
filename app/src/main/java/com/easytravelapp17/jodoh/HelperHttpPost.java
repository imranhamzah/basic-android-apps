package com.easytravelapp17.jodoh;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

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

public class HelperHttpPost extends AppCompatActivity {

    JSONObject jsonObject;
    JSONArray jsonArray;

    UserList userList;

    public ArrayList<UserList> m_users = null;

    //End for list view on main page
    public ArrayList<UserList> connect(String inputURL)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        m_users = new ArrayList<>();

        try {
            URL onlineUsers = new URL(String.valueOf(inputURL));
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
                userList = new UserList();
                JSONObject JO = jsonArray.getJSONObject(count);
                userList.setUser_name(JO.getString("user_name"));
                userList.setFull_name(JO.getString("fullname"));
                count++;
                m_users.add(userList);
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
        return m_users;
    }


}
