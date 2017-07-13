package com.easytravelapp17.jodoh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.easytravelapp17.jodoh.R.id.imageView;

public class SubscriptionRecycleAdapter extends RecyclerView.Adapter<SubscriptionRecycleAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;

    List<Subscription> data = Collections.emptyList();

    public SubscriptionRecycleAdapter(Context context, ArrayList<Subscription> data) {
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_subscription,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Subscription current = data.get(position);
        holder.title.setText(current.getPlan_title());
        holder.duration.setText(current.getDuration());

        URL urlImage = null;
        try {
            urlImage = new URL(current.getPlan_image());
            Bitmap bmp = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
            holder.icon.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,duration;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.subscriptionTitleTextView);
            duration = (TextView) itemView.findViewById(R.id.dateExpireTextView);
            icon = (ImageView) itemView.findViewById(R.id.subscriptionPackageIcon);
        }
    }
}
