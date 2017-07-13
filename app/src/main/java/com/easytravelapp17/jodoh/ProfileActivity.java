package com.easytravelapp17.jodoh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements PicModeSelectDialogFragment.IPicModeSelectListener {

    DatabaseHelper db;
    DbHelperTableWhishList dbHelperTableWhishList;
    TextView fullnameTextView,ageTextView,genderTextView,stateTextView;
    ImageView profileImageView;

    Button whishList;

    MaterialSearchView searchView;

    private static final int CAMERA_REQUEST_CODE = 1;


    private ShareActionProvider mShareActionProvider;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private static final String SERVER_ADDRESS = "http://13.59.107.215:8031/";


    public static final String TAG = "ImageViewActivity";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;
    private String imgUri;

    private Button mBtnUpdatePic;
    private ImageView mImageView;
    private CardView mCardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        dbHelperTableWhishList = new DbHelperTableWhishList(this);

        getUserDetails();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        //Display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchView = (MaterialSearchView) findViewById(R.id.search_view_everywhere_profile);

        //Initialize component definition
        fullnameTextView = (TextView) findViewById(R.id.fullnameProfileTextView);
        ageTextView = (TextView) findViewById(R.id.ageTextView);
        genderTextView = (TextView) findViewById(R.id.genderTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        profileImageView = (ImageView) findViewById(R.id.profileImage);
        whishList = (Button) findViewById(R.id.whishlistButton);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });

        whishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageAddToWhishList();
            }
        });

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        if(username != null)
        {
            HelperHttpPost http = new HelperHttpPost();
            String inputURL = String.valueOf(R.string.OnlineURL);
//            UserList userList = http.connect(inputURL);
//            fullnameTextView.setText(userList.getFull_name());
//            ageTextView.setText(userList.getUser_name());
        }

        //Start share

        Button shareButton;
        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String appLink = "https://play.google.com/store/apps/details?id=com.easytravelapp17.loghat";
                i.putExtra(Intent.EXTRA_SUBJECT,"My new app");
                i.putExtra(Intent.EXTRA_TEXT,"Try my new app: "+ appLink);
                startActivity(Intent.createChooser(i,"Share via"));
            }
        });

        //End share
    }



    private void getUserDetails() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;




        try {
            URL onlineUsers = new URL(getString(R.string.user_url));
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

            TextView fullnameProfileTextView = (TextView) findViewById(R.id.fullnameProfileTextView);
            TextView stateTextView = (TextView) findViewById(R.id.stateTextView);
            TextView genderTextView = (TextView) findViewById(R.id.genderTextView);
            TextView ageTextView = (TextView) findViewById(R.id.ageTextView);
            TextView targetUserTextView = (TextView) findViewById(R.id.targetUserTextView);
            TextView maritalStatusTextView = (TextView) findViewById(R.id.maritalStatusTextView);
            TextView worksTextView = (TextView) findViewById(R.id.worksTextView);
            TextView schoolTextView = (TextView) findViewById(R.id.schoolTextView);
            TextView locationTextView = (TextView) findViewById(R.id.locationTextView);
            TextView totalUniqueViewsTextView = (TextView) findViewById(R.id.totalUniqueViewsTextView);
            TextView ratingButton = (TextView) findViewById(R.id.ratingButton);
            ImageView img = (ImageView) findViewById(R.id.profileImage);
            ImageView featuredImage1 = (ImageView) findViewById(R.id.featuredImage1);
            ImageView featuredImage2 = (ImageView) findViewById(R.id.featuredImage2);


            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                fullnameProfileTextView.setText(JO.getString("fullname"));
                stateTextView.setText(JO.getString("hometown"));
                genderTextView.setText(JO.getString("gender"));
                ageTextView.setText(JO.getString("age"));
                targetUserTextView.setText(JO.getString("prefer_words"));
                maritalStatusTextView.setText(JO.getString("marital_status"));
                worksTextView.setText(JO.getString("profession"));
                schoolTextView.setText(JO.getString("education"));
                locationTextView.setText(JO.getString("location"));
                totalUniqueViewsTextView.setText(JO.getString("viewers"));
                ratingButton.setText(JO.getString("rating"));


                URL urlImage,urlFeautredImage1,urlFeautredImage2 = null;
                try {
                    urlImage = new URL(JO.getString("profile_image"));
                    urlFeautredImage1 = new URL(JO.getString("featured_image1"));
                    urlFeautredImage2 = new URL(JO.getString("featured_image2"));
                    Bitmap bmp = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
                    Bitmap bmp1 = BitmapFactory.decodeStream(urlFeautredImage1.openConnection().getInputStream());
                    Bitmap bmp2 = BitmapFactory.decodeStream(urlFeautredImage2.openConnection().getInputStream());
                    img.setImageBitmap(bmp);
                    featuredImage1.setImageBitmap(bmp1);
                    featuredImage2.setImageBitmap(bmp2);
                    Log.i("bmp",bmp.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

//        ProfileActivity.this.runOnUiThread(returnSubscription);
    }

    public void updateImage(View view)
    {
        showAddProfilePicDialog();
    }


    private void showAddProfilePicDialog() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(this);
        dialogFragment.show(getFragmentManager(), "picModeSelector");
        checkPermissions();
    }


    @SuppressLint("InlinedApi")
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            profileImageView.setImageBitmap(photo);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            //Save the images to the device
//            MediaStore.Images.Media.insertImage(getContentResolver(),photo,"Title","Description");



            new uploadImage(encoded,"ce_try").execute();



            //Save image to the server
        }


//        if (requestCode == REQUEST_CODE_UPDATE_PIC) {
//            if (resultCode == RESULT_OK) {
//                String imagePath = data.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
//                showCroppedImage(imagePath);
//            } else if (resultCode == RESULT_CANCELED) {
//                //TODO : Handle case
//            } else {
//                String errorMsg = data.getStringExtra(ImageCropActivity.ERROR_MSG);
//                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
//            }
//        }
    }

    private void showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            mImageView.setImageBitmap(myBitmap);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile,menu);
        MenuItem item = menu.findItem(R.id.action_search_profile);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit_profile)
        {
            Intent i = new Intent(this,ProfileEditActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    private void manageAddToWhishList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyConstants.URL_TO_SAVE_WHISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response",response.toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveWhishListToLocalStorage(10,15, MyConstants.NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveWhishListToLocalStorage(10,15, MyConstants.NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        saveWhishListToLocalStorage(10,15, MyConstants.NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("src_usr_id", "1");
                params.put("tgt_usr_id", "2 ");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    //saving the name to local storage
    private void saveWhishListToLocalStorage(int src_usr_id, int tgt_usr_id, int sync_status) {
        Log.i("data_catched", String.valueOf(src_usr_id+"-"+tgt_usr_id+"-"+sync_status));
        dbHelperTableWhishList.manageAddToWhishList(src_usr_id,tgt_usr_id, sync_status);
    }

    @Override
    public void onPicModeSelected(String mode) {

    }

    private class uploadImage extends AsyncTask<Void,Void,Void>
    {
        String encodedImage;
        String name;
        public uploadImage(String encodedImage, String name) {
            this.encodedImage = encodedImage;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));

            HttpParams httpParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"upload_image.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Image uploaded",Toast.LENGTH_SHORT).show();
        }
    }

    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,1000*30);
        HttpConnectionParams.setSoTimeout(httpRequestParams,1000*30);
        return httpRequestParams;
    }
}
