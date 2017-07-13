package com.easytravelapp17.jodoh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DbHelperTableWhishList dbHelperTableWhishList;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        dbHelperTableWhishList = new DbHelperTableWhishList(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = dbHelperTableWhishList.getUnsyncedWhishList();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveToWhishList(
                                cursor.getInt(cursor.getColumnIndex(DbHelperTableWhishList.COLUMN_WHISHLIST_ID)),
                                cursor.getString(cursor.getColumnIndex(DbHelperTableWhishList.COLUMN_SRC_USR_ID)),
                                cursor.getString(cursor.getColumnIndex(DbHelperTableWhishList.COLUMN_TGT_USR_ID))
                        );
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    /*
    * method taking two arguments
    * name that is to be saved and id of the name from SQLite
    * if the name is successfully sent
    * we will update the status as synced in SQLite
    * */
    private void saveToWhishList(final int wishlist_id, final String src_usr_id, final String tgt_usr_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConstants.URL_TO_SAVE_WHISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                dbHelperTableWhishList.updateWhishListStatus(wishlist_id, MyConstants.NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(MyConstants.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("src_usr_id", src_usr_id);
                params.put("tgt_usr_id", tgt_usr_id);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
