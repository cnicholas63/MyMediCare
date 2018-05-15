package com.example.cnich.mymedicare;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cnich.mymedicare.database.AccessDatabase;

/**
 * Assesses user statistics based on user input
 *
 */
public class AssessStats extends AppCompatActivity implements AppConstants {
    User user; // User's record from the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_stats);

        TextView unameField = (TextView) findViewById(R.id.userNameTextView);
        Bundle bundle;
        String username;
        AccessDatabase userDatabase;

        // Get the username from the bundle
        bundle = getIntent().getExtras(); // Get the bundled info

        username = bundle.getString(USERNAME); // Unpack username from bundle

        // Get user names from database
        userDatabase = new AccessDatabase(this);
        userDatabase.open(); // Open the database
        user = userDatabase.getUser(username); // Get users from database
        userDatabase.close(); // Close the database

        // Populate username field
        unameField.setText(username);
    }

    /**
     * Assess the statistics entered and colour code the results
     * If any are deemed High Risk, offer to contact Doctor/Nurse
     */
    void assessStats(View view) {
        EditText temp = (EditText) findViewById(R.id.temperatureEditText); // References to input fields
        EditText bpl = (EditText) findViewById(R.id.bpLowerEditText);
        EditText bph = (EditText) findViewById(R.id.bpHigherEditText);
        EditText heart = (EditText) findViewById(R.id.heartRateEditText);

        // Reading values
        float temperature; // Temperature needs to be a decimal
        int bloodPressureLow, bloodPressureHigh, heartRate;
        // Category values holding assessment result
        int tmpCategory, bplCategory, bphCategory, hrCategory; //

        String tempValue; // Temp value whilst parsing values from input fields
        String temperatureReport, bloodPressureLowReport, bloodPressureHighReport, heartRateReport; // Report strings
        String report;

        // Parse the values from statistics input fields
        tempValue = temp.getText().toString().trim(); // Copy trimmed string
        try {
            temperature = Float.parseFloat(tempValue);
        } catch (NumberFormatException ne) {
            temperature = (float) 0.0;
        }

        tempValue = bpl.getText().toString().trim(); // Copy trimmed string
        try {
            bloodPressureLow = Integer.parseInt(tempValue);
        } catch(NumberFormatException ne) {
            bloodPressureLow = 0;
        }

        tempValue = bph.getText().toString().trim(); // Copy trimmed string
        try {
            bloodPressureHigh = Integer.parseInt(tempValue);
        } catch (NumberFormatException ne) {
            bloodPressureHigh = 0;
        }

        tempValue = heart.getText().toString().trim(); // Copy trimmed string
        try {
            heartRate = Integer.parseInt(tempValue);
        } catch(NumberFormatException ne) {
            heartRate = 0;
        }

        // Assess Temperature
        if(temperature <= 0.0) {
            tmpCategory = 0;  // Field empty
            temperatureReport = "Not taken";
        } else {
            if (temperature < 50.0) { // Temperature in celcius (I would hope!)
                if (temperature <= 37) {
                    tmpCategory = 1; // Normal
                    temp.setBackgroundColor(getResources().getColor(R.color.colourNormalRisk ));
                } else if (temperature > 37 && temperature <= 38) {
                    tmpCategory = 2; // Low Risk
                    temp.setBackgroundColor(getResources().getColor(R.color.colourLowRisk ));
                } else {
                    tmpCategory = 3; // High Risk
                    temp.setBackgroundColor(getResources().getColor(R.color.colourHighRisk ));
                }
            } else { // Temperature in Farenheit
                if (temperature <= 98.6) {
                    tmpCategory = 1; // Normal
                    temp.setBackgroundColor(getResources().getColor(R.color.colourNormalRisk ));
                } else if (temperature > 98.6 && temperature < 100.5) {
                    tmpCategory = 2; // Low Risk
                    temp.setBackgroundColor(getResources().getColor(R.color.colourLowRisk ));
                } else {
                    tmpCategory = 3; // High Risk
                    temp.setBackgroundColor(getResources().getColor(R.color.colourHighRisk ));
                }
            }
        }

        // Assess Blood Pressure Lower
        if(bloodPressureLow == 0) {
            bplCategory = 0; // Ignore, field empty
        } else {
            if(bloodPressureLow < 80) {
                bplCategory = 1; // Normal
                bpl.setBackgroundColor(getResources().getColor(R.color.colourNormalRisk ));
            } else if(bloodPressureLow <= 110) {
                bplCategory = 2; // Low Risk
                bpl.setBackgroundColor(getResources().getColor(R.color.colourLowRisk ));
            } else {
                bplCategory = 3; // High Risk
                bpl.setBackgroundColor(getResources().getColor(R.color.colourHighRisk ));
            }
        }

        // Assess Blood Pressure High
        if(bloodPressureHigh == 0) {
            bphCategory = 0; // Ignore, field empty
        } else {
            if(bloodPressureHigh < 120) {
                bphCategory = 1; // Normal
                bph.setBackgroundColor(getResources().getColor(R.color.colourNormalRisk ));
            } else if(bloodPressureHigh <= 180) {
                bphCategory = 2; // Low Risk
                bph.setBackgroundColor(getResources().getColor(R.color.colourLowRisk ));
            } else {
                bphCategory = 3; // High Risk
                bph.setBackgroundColor(getResources().getColor(R.color.colourHighRisk ));
            }
        }

        // Asses Heart Rate
        if(heartRate == 0) {
            hrCategory = 0; // Ignore, field empty
        } else {
            if(heartRate <=72) {
                hrCategory = 1; // Normal
                heart.setBackgroundColor(getResources().getColor(R.color.colourNormalRisk ));
            } else if(heartRate < 160) {
                hrCategory = 2; // Low Risk
                heart.setBackgroundColor(getResources().getColor(R.color.colourLowRisk ));
            } else {
                hrCategory = 3; // High Risk
                heart.setBackgroundColor(getResources().getColor(R.color.colourHighRisk ));
            }
        }

        // Assessments complete - compose and compile reports
        // Temperature Reports
        if(tmpCategory == 0)
            temperatureReport = getString(R.string.reportTemperatureNM);
        else if(tmpCategory == 1)
            temperatureReport = getString(R.string.reportTemperatureNorm) + temperature;
        else if(tmpCategory == 2)
            temperatureReport = getString(R.string.reportTemperatureLow) + temperature;
        else
            temperatureReport = getString(R.string.reportTemperatureHigh) + temperature;

        // Blood Pressure Lower Reading Report
        if(bplCategory == 0)
            bloodPressureLowReport = getString(R.string.reportBPLNM);
        else if(bplCategory == 1)
            bloodPressureLowReport = getString(R.string.reportBPLNorm) + bloodPressureLow;
        else if(bplCategory == 2)
            bloodPressureLowReport = getString(R.string.reportBPLLow) + bloodPressureLow;
        else
            bloodPressureLowReport = getString(R.string.reportBPLHigh) + bloodPressureLow;

        // Blood Pressure Upper Reading Report
        if(bphCategory == 0)
            bloodPressureHighReport = getString(R.string.reportBPHNM);
        else if(bphCategory == 1)
            bloodPressureHighReport = getString(R.string.reportBPHNorm) + bloodPressureLow;
        else if(bphCategory == 2)
            bloodPressureHighReport = getString(R.string.reportBPHLow) + bloodPressureLow;
        else
            bloodPressureHighReport = getString(R.string.reportBPHHigh) + bloodPressureLow;

        // Heart Rate Report
        if(hrCategory == 0)
            heartRateReport = getString(R.string.reportHeartRateNM);
        else if(hrCategory == 1)
            heartRateReport = getString(R.string.reportHeartRateNorm) + heartRate;
        else if(hrCategory == 2)
            heartRateReport = getString(R.string.reportHeartRateLow) + heartRate;
        else
            heartRateReport = getString(R.string.reportHeartRateHigh) + heartRate;

        report =
                "Dear " + user.getDoctor() + ",\n" +
                "Your patient " + user.getName() + " has used the MyMediCare Mobile App and with the following results:\n" +
                temperatureReport + "\n" +
                bloodPressureLowReport + "\n" +
                bloodPressureHighReport + "\n" +
                heartRateReport;


    }

//    /**
//     * Call now button clicked. This invokes the built in phone activity passing it the number to call
//     * param: view, the context for this view
//     */
//    public void callNowClicked(View view) {
//        String phoneNumber = getResources().getString(R.string.nyas_phone_number); // Get the phone number from resources
//
//        // Set the phone intent to open the telephone activity
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//
//        // fill in the phone number
//        intent.setData(Uri.parse(phoneNumber));
//
//        // Start the telephone intent with Intent.ACTION_DIAL - this completes the number ready to dial
//        // The number can be automatically dialled by using Intent.ACTION_CALL
//        // However, I wanted to allow the user to change their mind before dialing
//        startActivity(intent);
//    }
//
//    /**
//     * Email button clicked. This invokes a built in email activity. It fills in the email address ready to be sent
//     * @param view
//     */
//    public void emailClicked(View view) {
//        String emailAddress = getResources().getString(R.string.nyas_email_address); // Get the amil address from resources
//
//        // Set the email intent to open the email activity and complete the email address
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                "mailto", emailAddress, null));
//
//        // Fill in subject string
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Request");
//
//        // Start the email activity
//        startActivity(Intent.createChooser(emailIntent, "Email"));
//    }



}
