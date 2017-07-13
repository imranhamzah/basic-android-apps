package com.easytravelapp17.jodoh;

import android.app.ProgressDialog;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class SubscriptionActivity extends AppCompatActivity {

    private SubscriptionRecycleAdapter adapter;
    private Runnable viewSubscription;
    private ArrayList<Subscription> m_subscription = null;
    RecyclerView recyclerView;

    JSONObject jsonObject;
    JSONArray jsonArray;
    private ProgressDialog m_ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        setTitle("Choose your subscription");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        //Trying to get orders if available
        viewSubscription = new Runnable() {
            @Override
            public void run() {
                getSubscriptionList();
            }
        };

        Thread thread =  new Thread(null, viewSubscription, "MagentoBackground");
        thread.start();

        m_ProgressDialog = ProgressDialog.show(SubscriptionActivity.this,
        "Please wait...", "Retrieving data ...", true);


    }

    private void getSubscriptionList() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        m_subscription = new ArrayList<>();

        try {
            URL onlineUsers = new URL(getString(R.string.subscription_url));
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
                Subscription subscription = new Subscription();
                JSONObject JO = jsonArray.getJSONObject(count);
                subscription.setPlan_title(JO.getString("plan_title"));
                subscription.setDuration(JO.getString("duration"));
                subscription.setPlan_image(JO.getString("plan_image"));
                m_subscription.add(subscription);
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

        SubscriptionActivity.this.runOnUiThread(returnSubscription);
    }

    private Runnable returnSubscription = new Runnable() {

        @Override
        public void run() {


            if(m_subscription != null && m_subscription.size() > 0){

                adapter = new SubscriptionRecycleAdapter(getApplicationContext(),m_subscription);
                recyclerView = (RecyclerView) findViewById(R.id.recyclerviewsubscription);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                adapter.notifyDataSetChanged();

            }
            m_ProgressDialog.dismiss();
        }
    };



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
