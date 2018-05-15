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

/**
 * Activity allows the user to change their details
 * User not allowed edit their user name
 */
public class ManageAccount extends AppCompatActivity implements AppConstants{
    User user; // User record will be retrieved from the database then used to update the database if there are changes to the information
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        TextView unameField;
        Bundle bundle;

        AccessDatabase userDatabase; // Used to retrieve and updata user details

        // Get the username from the bundle
        bundle = getIntent().getExtras(); // Get the bundled info

        username = bundle.getString(USERNAME); // Unpack username from bundle

        // Populate username field
        unameField = (TextView) findViewById(R.id.userName);
        unameField.setText(username);

        // Get user names from database
        userDatabase = new AccessDatabase(this);
        userDatabase.open(); // Open the database
        user = userDatabase.getUser(username); // Get users from database
        userDatabase.close(); // Close the database

        populateFields(user); // Populate the form fields with current information

    }

    // Verify entries, then update record in database
    void saveButtonClicked(View view) {
        final String name, ageString, address, postCode, doctor, password1, password2;
        int age;
        String alertMessage = "";
        String userAgeString = ""; // Non final age string for assigning age to after validation

        final EditText nameField = (EditText) findViewById(R.id.nameField); // References to input fields
        final EditText ageField = (EditText) findViewById(R.id.ageField);
        final EditText addressField = (EditText) findViewById(R.id.addrField);
        final EditText pCodeField = (EditText) findViewById(R.id.pCodeField);
        final EditText doctorField = (EditText) findViewById(R.id.doctorField);
        final EditText pw1 = (EditText) findViewById(R.id.password1Field);
        final EditText pw2 = (EditText) findViewById(R.id.password2Field);

        AccessDatabase userDatabase; // Used when updating database

        eCode errorCode = eCode.OK; // Error code indicating validation status, eg OK

        // Get the values from the EditText (text) fields
        name = nameField.getText().toString().trim(); // Trim off any spaces
        ageString = ageField.getText().toString().trim();
        address = addressField.getText().toString().trim();
        postCode = pCodeField.getText().toString().trim();
        doctor = doctorField.getText().toString().trim();
        password1 = pw1.getText().toString().trim();
        password2 = pw2.getText().toString().trim();


        errorCode = eCode.OK; // Set error code to OK, it will only change if validation fails

        // Validate that all fields have a valid entry
        if(name.length() == 0) {
            errorCode = eCode.BADNAME;
            alertMessage = getString(R.string.alertNoName);
        }
        else if (address.length() == 0) {
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
        else if(ageString.length() != 0) { // Something in the age field, validate it
            try {
                age = Integer.parseInt(ageString); // Parse the integer from age field
                // got a value now check it's reasonable
                if(age < 10 || age > 110) {
                    errorCode = eCode.BADAGE; // Outside valid age range
                    alertMessage = getString(R.string.alertTooYoungOld);
                }
                else // Age is good
                    userAgeString = Integer.toString(age); // Parse back into string

            } catch (NumberFormatException e) { // Non numeric or non integer
                errorCode = eCode.BADAGE;
                alertMessage = getString(R.string.alertValidInteger);
            }
        }

        if(errorCode == eCode.OK) { // Previous checks pass, now validate both password fields are the same
            if ((!password1.equals(password2) || password1.length() == 0)) { // Passwords do not validate, or are zero length
                errorCode = eCode.BADPASSWORD; // Indicate bad password
                alertMessage = getString(R.string.alertPasswordNoMatch); // Passwords don't match
            }
        }

        if(errorCode != eCode.OK) { // One of the validation checks failed, display error dialog and clear offending field
            // Display a dialogue explaining error
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(alertMessage); // set alert message - as set in the validation checks

            final eCode finalErrorCode = errorCode; // Temp final for access by anonymous class

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
                        case BADPASSWORD: // Clear the password fields
                            pw1.setText("");
                            pw2.setText("");
                    }
                }
            });

            // Create the AlertDialog
            AlertDialog dialog = alert.show();
        }
        else { // All checks passed, user details can be updated in the database
            Intent intent;

            // Update user details
            user.setPassword(password1);
            user.setName(name);
            user.setAge(userAgeString);
            user.setAddress1(address);
            user.setPostCode(postCode);
            user.setDoctor(doctor);

            userDatabase = new AccessDatabase(this); // Connection to the database
            userDatabase.open(); // Open the database

            userDatabase.updateUser(user); // Update the user in the database

            userDatabase.close(); // Close database connection

            Toast.makeText(this, "Details Updated: " + user.getName(), Toast.LENGTH_SHORT).show();
            user.printUser(); // Output ne user to the console
        }
    }


    // Populate the fields with user's details
    void populateFields(User user) {
        EditText nameField = (EditText) findViewById(R.id.nameField); // References to input fields
        EditText ageField = (EditText) findViewById(R.id.ageField);
        EditText addressField = (EditText) findViewById(R.id.addrField);
        EditText pCodeField = (EditText) findViewById(R.id.pCodeField);
        EditText doctorField = (EditText) findViewById(R.id.doctorField);
        EditText password1Field = (EditText) findViewById(R.id.password1Field);
        EditText password2Field = (EditText) findViewById(R.id.password2Field);

        // Populate fields with current values
        nameField.setText(user.getName());
        ageField.setText(user.getAge());
        addressField.setText(user.getAddress1());
        pCodeField.setText(user.getPostCode());
        doctorField.setText(user.getDoctor());
        password1Field.setText(user.getPassword());
        password2Field.setText(user.getPassword());

    }


}
