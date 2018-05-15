package com.example.cnich.mymedicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cnich.mymedicare.database.AccessDatabase;

public class DashBoard extends AppCompatActivity implements AppConstants {
    // Will hold the user's record when retrieved from or writing to database
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        TextView unameField;
        Bundle bundle;

        // Get the username from the bundle
        bundle = getIntent().getExtras(); // Get the bundled info

        username = bundle.getString(USERNAME); // Unpack username from bundle

        // Populate username field
        unameField = (TextView) findViewById(R.id.userNameTextView);
        unameField.setText(username);

    }

    // Assess Stats button pressed start Assess Stats activity
    void assessStatisticsButtonPressed(View view) {
        Intent intent;

        // Add username to bundle for passing to UserCreateAccount activity
        intent = new Intent(this, AssessStats.class); // Intent for new account
        intent.putExtra(USERNAME, username); // Add the user name to the bundle

        startActivity(intent); // Start the create account activity
    }

    // Manage Account button pressed - Allow user to view and edit their details
    void manageAccountButtonPressed(View view) {
        Intent intent;

        // Add username to bundle for passing to UserCreateAccount activity
        intent = new Intent(this, ManageAccount.class); // Intent for new account
        intent.putExtra(USERNAME, username); // Add the user name to the bundle

        startActivity(intent); // Start the create account activity

    }
}
