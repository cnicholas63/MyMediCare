package com.example.cnich.mymedicare;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cnich.mymedicare.database.AccessDatabase;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;

public class MainActivity extends AppCompatActivity implements AppConstants {
    AccessDatabase userDatabase; // Connection to the database

    List<String> userNameList = new ArrayList<>(); // ArrayList for holding usernames
    List<User> users;   // ArrayList for holding Users retrieved from the database
    int spinnerChoice = 0; // Default spinner selection, 0 = new user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Debugging purposes only - can be removed when solution verified
        // If database is empty it adds several test users
        dbStatus(); // outputs existing users - adds sample users if necessary
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userNameList.size() > 0) { // If the activity has been returned to, refresh the lists
            userNameList.clear();
            users.clear();
        }

        populateSpinner(); // Populate spinner to include any new uers

    }

    void populateSpinner() { // Get user names from database and populate spinner
        userDatabase = new AccessDatabase(this);

        userDatabase.open(); // Open the database
        users = userDatabase.getAllUsers(); // Get users from database
        userDatabase.close(); // Close the database



        // Populate the user name spinner
        userNameList.add("Select User"); // New User entry as first in spinner

        for(User name : users)
            userNameList.add(name.getUserName()); // Populate with existing users

        Spinner spinner = (Spinner) findViewById(R.id.userNameSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userNameList);

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        // Listener for spinner activity - uses anonymous function to call appropriate activity
        // Based on the users selection - New user or existing user

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinnerChoice = pos; // Get the index of the spinner choice
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                //Another interface callback
            }

        });
    }




    // Enter button pressed, validate password and start activity for New User or Existing User
    void enterButtonPressed(View view) {
        Intent intent;
        EditText passwordField;
        String password;
        User user;

        if(spinnerChoice == 0) { // User has not selected a username
            // Display a dialogue explaining error

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(getString(R.string.alertUserNotSelected)); // set alert message - as set in the validation checks

            alert.setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });

            // Create the AlertDialog
            AlertDialog dialog = alert.show();
            return;
        }

        // Validate password against selected user
        passwordField = (EditText) findViewById(R.id.passwordEditText);
        password = passwordField.getText().toString().trim();

        user = users.get(spinnerChoice - 1); // Get the user details from the list

        if(!password.equals(user.getPassword())) { // Incorrect Password
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
        }
        else { // Password validated
            Toast.makeText(this, "Correct Password", Toast.LENGTH_SHORT).show();
            passwordField.setText("");

            // Start User Account activity to get user details
            intent = new Intent(this, DashBoard.class);    // Intent for Dashboard
            intent.putExtra(USERNAME, user.getUserName()); // Pass username to dashboard

            startActivity(intent); // Start the create account activity
        }
    }

    // Create new user
    void createNewUserPressed (View view) {
        Intent intent;

        intent = new Intent(this, NewUser.class);

        startActivity(intent); // Start the New User activity
    }

    /**
     * Debugging DB status
     * Can be removed when solution verified
     * Inserts sample users
     * Outputs to app the number of users in database
     * Outputs to console the user details
     */
    void dbStatus() {
        // The following code tests the database - delete when finished
        // Outputs appointment records to console - delete when finished
        // Open database connection, this will create a new database if it doesn't exist
        userDatabase = new AccessDatabase(this);
        userDatabase.open();

        // Get all users from database
        List<User> values = userDatabase.getAllUsers();

        if(values.size() == 0) { // Database is empty, add some test users
            System.out.println("************************* Creating Test Users");

            String[][] testUsers = {
                    {"Chris", "password", "Christopher", "52", "Bootle", "L20", "Dr. Walker"},
                    {"Mary", "password", "Mary", "21", "Cornwall", "TR11", "Mrs Peters"}
            };
            HashMap testValues = new HashMap();

            for(String[] u : testUsers) {
                testValues.put(USERNAME, u[0]);
                testValues.put(PASSWORD, u[1]);
                testValues.put(NAME, u[2]);
                testValues.put(AGE, u[3]);
                testValues.put(ADDRESS1, u[4]);
                testValues.put(POSTCODE, u[5]);
                testValues.put(DOCTOR, u[6]);


                User user = new User(testValues); // Instantiate a test user
                userDatabase.createUser(user);    // Add user to the database
            }

            values = userDatabase.getAllUsers(); // Retrieve newly added test users from the database

        }

        System.out.println("************************* Users in database =  " + values.size());

        // Output any users to the console
        for(User app : values) {
            app.printUser();
        }

        // Close the database connection
        userDatabase.close();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
