package com.easytravelapp17.jodoh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<UserList>{


    private static class ViewHolder {
        private TextView itemView;
    }


    public ListViewAdapter(Context context, int textViewResourceId, ArrayList<UserList> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = LayoutInflater.from(this.getContext())
                .inflate(R.layout.user_list_item, parent, false);



        UserList item = getItem(position);

        Log.i("data_here123",item.toString());


        return convertView;
    }
}

