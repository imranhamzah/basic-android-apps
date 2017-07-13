package com.easytravelapp17.jodoh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText fullName, mobileNumber, loginId, password, confirmPassword;
    CheckBox termAndConditionCheckBox;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Register");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        fullName = (EditText) findViewById(R.id.fullnameEditText);
        mobileNumber = (EditText) findViewById(R.id.mobileNumberEditText);
        loginId = (EditText) findViewById(R.id.loginIdRegisterEditText);
        password = (EditText) findViewById(R.id.newPasswordEditText);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);
        termAndConditionCheckBox = (CheckBox) findViewById(R.id.termAndConditionCheckBox);


        db = new DatabaseHelper(this);

        sharedPreferences = this.getSharedPreferences("com.easytravelapp17.jodoh", Context.MODE_PRIVATE);
    }

    public void submitRegistrationForm(View view)
    {
        String full_name = fullName.getText().toString();
        String mobile_number = mobileNumber.getText().toString();
        String login_id = loginId.getText().toString();
        String password_txt = password.getText().toString();
        String confirm_password = confirmPassword.getText().toString();
        Boolean term_and_condition = termAndConditionCheckBox.isChecked();

        boolean isInserted = db.insertData(login_id,full_name,password_txt);
        if(isInserted == true)
        {
            sharedPreferences.edit().putString("username",login_id).apply();
            sharedPreferences.edit().putString("fullname",full_name).apply();

            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendDataToServer()
    {

    }
}
