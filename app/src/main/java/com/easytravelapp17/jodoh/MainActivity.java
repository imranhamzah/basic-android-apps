package com.easytravelapp17.jodoh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MaterialSearchView searchView;

    //Global sharedPreferences
    SharedPreferences sharedPreferences;

    HelperIsNetworkConnected helperIsNetworkConnected;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<String> arrayList = new ArrayList<>();

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        helperIsNetworkConnected = new HelperIsNetworkConnected();
        if(!helperIsNetworkConnected.checkIsConnected(MainActivity.this))
        {
            helperIsNetworkConnected.buildDialog(MainActivity.this).show();
        }else
        {
            setContentView(R.layout.activity_main);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            searchView = (MaterialSearchView)findViewById(R.id.search_view_everywhere);
//
//            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//            layoutManager = new LinearLayoutManager(this);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setHasFixedSize(true);








//            recyclerAdapter = new RecyclerAdapter();
//            recyclerView.setAdapter(recyclerAdapter);


            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            //Store into shared preferences
            sharedPreferences = this.getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);

            final String userName = sharedPreferences.getString("username","");
            setTitle(getString(R.string.welcome)+" "+userName);

            //Check either user authenticated or not, else go to login page
            if(userName.isEmpty())
            {
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
            }
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(android.support.v4.app.FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    TabPopular tabPopular = new TabPopular();
                    return tabPopular;
                case 1:
                    TabOnline tabOnline = new TabOnline();
                    return tabOnline;
                case 2:
                    TabActivity tabActivity = new TabActivity();
                    return tabActivity;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(com.easytravelapp17.jodoh.R.string.popular);
                case 1:
                    return getString(com.easytravelapp17.jodoh.R.string.online);
                case 2:
                    return getString(com.easytravelapp17.jodoh.R.string.activity);
            }
            return null;
        }
    }

    public void openUserProfile(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_logout) {

            sharedPreferences.edit().remove("username").apply();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }*/

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {

            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_subscription) {

            Intent i = new Intent(this, SubscriptionActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_logout) {

            sharedPreferences.edit().remove("username").apply();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
