package com.easytravelapp17.jodoh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DatabaseHelper db;
    TextView signUpTextView;
    EditText loginIdEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        setTitle("Login");

        sharedPreferences = this.getSharedPreferences("com.easytravelapp17.jodoh", Context.MODE_PRIVATE);

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        loginIdEditText = (EditText) findViewById(R.id.loginIdEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    public void validateUser(View view)
    {
        String login_id = loginIdEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(login_id.isEmpty())
        {
            showMessage("Login ID","This field is required");
            loginIdEditText.setFocusable(true);
            return;
        }

        if(password.isEmpty())
        {
            showMessage("Password","This field is required");
            return;
        }

        Cursor res = db.validateUser(login_id,password);
        if(res.getCount() == 0) {
            // show message
            showMessage("Oopps","Invalid username or password, please try again!");
            return;
        }
        else
        {
            sharedPreferences.edit().putString("username",login_id).apply();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }


    }

    public void openRegister(View view)
    {
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
