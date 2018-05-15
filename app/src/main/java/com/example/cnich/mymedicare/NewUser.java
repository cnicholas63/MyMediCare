package com.example.cnich.mymedicare;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.cnich.mymedicare.database.AccessDatabase;

import java.util.List;

public class NewUser extends AppCompatActivity implements AppConstants{
    AccessDatabase userDatabase; // Connection to the database
    List<String> userNamesList; // list for usernames
    String userName; // Will hold a validated username
    String password; // Will hold a valid password
    Intent intent; // Used when invoking new user create account activity
    int failedValidation = 0; // Flags an input element has failed validation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        userDatabase = new AccessDatabase(this); // Connection to the database
        userDatabase.open(); // Open the database

        userNamesList = userDatabase.getAllUserNames(); // Retrieve all user names from database

        userDatabase.close(); // Close database connection

    }

    // Accept button pressed, validate username and password
    public void acceptButtonPressed(View view) {
        final EditText uname = (EditText) findViewById(R.id.userNameText); // References to input fields
        final EditText pw1 = (EditText) findViewById(R.id.password1Text);
        final EditText pw2 = (EditText) findViewById(R.id.password2Text);

        String alertMessage = "";

        // Get the username from text field
        userName = uname.getText().toString().trim(); // Trim off any spaces

        if(userName.length() == 0 || userName.contains(" ")) { // No username entered
            failedValidation = 1; // Indicate bad username
            alertMessage =  getString(R.string.alertNoSpacesInUsername); // No spaces in username

        }
        else if(userNamesList.contains(userName)) { // Validate username not already taken
            failedValidation = 2; // Indicate taken username
            alertMessage = getString(R.string.alertUserNameTaken); // Username taken
        }
        else { // Validate both password fields are the same
            String pass1 = pw1.getText().toString().trim(); // Copy trimmed string
            String pass2 = pw2.getText().toString().trim();

            if((!pass1.equals(pass2) || pass1.length() == 0)) { // Passwords do not validate, or are zero length
                failedValidation = 3; // Indicate bad password
                alertMessage = getString(R.string.alertPasswordNoMatch); // Passwords don't match
            }
            else // Password ok
                password = pass1; // Get the password
        }


        if(failedValidation != 0) { // One of the validation checks was failed, display error dialog

            // Display a dialogue explaining error
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(alertMessage); // set alert message - as set in the validation checks

            alert.setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button - Clear the invalid fields
                    if (failedValidation == 1 || failedValidation == 2) {
                        uname.setText(""); // Clear the username field
                    }
                    else { // Clear password fields
                        pw1.setText("");
                        pw2.setText("");
                    }
                }
            });

            // Create the AlertDialog
            AlertDialog dialog = alert.show();
        }
        else { // All checks passed, new user can be created
            // Start User Account activity to get user details

            // Add username and password to bundle for passing to UserCreateAccount activity
            intent = new Intent(this, UserCreateAccount.class); // Intent for new account
            intent.putExtra(USERNAME, userName); // Add the user name to the bundle
            intent.putExtra(PASSWORD, password); // Add the user's password to the bundle

            finish(); // This activity is finished with so remove from stack
            startActivity(intent); // Start the create account activity
        }

        failedValidation = 0; // Reset validation failed indicator
    }

}
