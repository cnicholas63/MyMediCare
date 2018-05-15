package com.example.cnich.mymedicare;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cnich.mymedicare.database.AccessDatabase;

import java.util.HashMap;

public class UserCreateAccount extends AppCompatActivity implements AppConstants {
    String userName;
    String password;
    Bundle userNamePassWord;
    AccessDatabase userDatabase; // Connection to the database

    // User details
    String name, ageString, address, postCode, doctor;
    int age;

    int failedValidation = 0; // Non zero value indicates validation failed and reason code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView unameField;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_account);
        userNamePassWord = getIntent().getExtras(); // Get the bundled info

        unpackBundle(userNamePassWord); // Get username and password from bundle

        // Populate username field
        unameField = (TextView) findViewById(R.id.userName);
        unameField.setText(userName);
    }

    // User has clicked save button - validate and save user details
    void saveButtonClicked(View view) {
        final EditText nameField = (EditText) findViewById(R.id.nameField); // References to input fields
        final EditText ageField = (EditText) findViewById(R.id.ageField);
        final EditText addressField = (EditText) findViewById(R.id.addrField);
        final EditText pCodeField = (EditText) findViewById(R.id.pCodeField);
        final EditText doctorField = (EditText) findViewById(R.id.doctorField);
        eCode errorCode = eCode.OK; // Error code indicating validation status, eg OK

        AccessDatabase userDatabase; // Used when updating database

        String alertMessage = "";

        // Get the values from the EditText (text) fields
        name = nameField.getText().toString().trim(); // Trim off any spaces
        ageString = ageField.getText().toString().trim();
        address = addressField.getText().toString().trim();
        postCode = pCodeField.getText().toString().trim();
        doctor = doctorField.getText().toString().trim();

        if(name.length() == 0) {
            errorCode = eCode.BADNAME;
            alertMessage = getString(R.string.alertNoName);
        } else if (address.length() == 0) {
            errorCode = eCode.BADADDRESS;
            alertMessage = getString(R.string.alertNoAddress);
        }
        else if(postCode.length() == 0) {
            errorCode = eCode.BADPOSTCODE;
            alertMessage = getString(R.string.alertNoPostcode);
        }
        else if(doctor.length() == 0) {
            errorCode = eCode.BADDOCTOR;
            alertMessage = getString(R.string.alertNoDoctor);
        }
        else if(ageString.length() == 0) {// Get value from EditText (integer) field
            errorCode = eCode.NOAGE; // Nothing in age field
            alertMessage = getString(R.string.alertNoAge);
        }
        else if(ageString.length() != 0) { // Something in the age field
            try {
                age = Integer.parseInt(ageString); // Parse the integer from age field
                // got a value now check it's reasonable
                if(age < 10 || age > 110) {
                    errorCode = eCode.BADAGE; // Outside valid age range
                    alertMessage = getString(R.string.alertTooYoungOld);
                } else // Age is good
                    ageString = Integer.toString(age); // Parse back into string

            } catch (NumberFormatException e) { // Non numeric or non integer
                errorCode = eCode.BADAGE;
                alertMessage = getString(R.string.alertValidInteger);
            }
        }
        else { // All validated
            errorCode = eCode.OK;
        }


        if(errorCode != eCode.OK) { // One of the validation checks failed, display error dialog and clear offending field
            // Display a dialogue explaining error
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(alertMessage); // set alert message - as set in the validation checks

            final eCode finalErrorCode = errorCode; // Temp final for access be anonymous class

            alert.setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button - Clear the invalid fields
                    switch(finalErrorCode) {
                        case BADNAME:
                            nameField.setText(""); // Clear the username field
                            break;
                        case BADAGE:
                            ageField.setText(""); // Clear the age field
                            break;
                        case BADADDRESS:
                            addressField.setText(""); // Clear the address field
                            break;
                        case BADPOSTCODE:
                            pCodeField.setText(""); // Clear postcode field
                            break;
                        case BADDOCTOR:
                            doctorField.setText(""); // Clear doctor field
                            break;
                    }
                }
            });

            // Create the AlertDialog
            AlertDialog dialog = alert.show();
        }
        else { // All checks passed, user details can be added to the database
            // Create user and add to database then open DashBoard Activity

            Intent intent;
            HashMap userDetails = new HashMap(); // For user details


            userDetails.put(USERNAME, userName); // Populate hash map
            userDetails.put(PASSWORD, password);
            userDetails.put(NAME, name);
            userDetails.put(AGE, ageString);
            userDetails.put(ADDRESS1, address);
            userDetails.put(POSTCODE, postCode);
            userDetails.put(DOCTOR, doctor);

            User user = new User(userDetails); // Instantiate new User

            userDatabase = new AccessDatabase(this); // Connection to the database
            userDatabase.open(); // Open the database

            userDatabase.createUser(user); // Add the user to the database

            userDatabase.close(); // Close database connection

            Toast.makeText(this, "Added to database " + user.getName(), Toast.LENGTH_SHORT).show();
            user.printUser(); // Output ne user to the console

            // Start Dashboard activity with new user's details
            intent = new Intent(this, DashBoard.class);    // Intent for Dashboard
            intent.putExtra(USERNAME, user.getUserName()); // Pass username to dashboard

            finish(); // This activity is finished with so remove from stack before starting the Dashboard

            startActivity(intent); // Start the create account activity
        }

        failedValidation = 0; // Reset validation failed indicator
    }

    void unpackBundle(Bundle bundle) {
        userName = bundle.getString(USERNAME); // Unpack username from bundle
        password = bundle.getString(PASSWORD); // Unpack password from bundle
    }

}
