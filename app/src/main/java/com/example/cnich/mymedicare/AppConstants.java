package com.example.cnich.mymedicare;

/**
 * Created by Chris on 19/05/2015.
 * Class holds constants definitions
 *
 */
public interface AppConstants {
    // Define constants that will be used by the DatabaseHelper class
    String DATABASE_NAME = "users"; // The name of the database
    String TABLE_ENTRIES = "users"; // The name of the table that will hold the data - single table flat file
    int DATABASE_VERSION = 1;       // The database version, needed to facilitate database updates (structure not content)

    String COLUMN_ID = "_id";       // The ID field, Android likes the primary key to be called _id
    String USERNAME = "USERNAME";   // Username
    String PASSWORD = "PASSWORD";   // Password
    String NAME = "NAME";           // User's real name
    String AGE = "AGE";             // User's age
    String ADDRESS1 = "ADDRESS1";   // Address line 1
    String POSTCODE = "POSTCODE";   // Post code
    String DOCTOR = "DOCTOR";       // Doctor or nurse's name

    enum eCode { // Error codes for validtion
        OK, BADNAME, NOAGE, BADAGE, BADADDRESS, BADPOSTCODE, BADDOCTOR, BADPASSWORD
    };

    enum readingAssessment { // Statistic assessment results
        NORMAL, LOWRISK, HIGHRISK
    };

}
