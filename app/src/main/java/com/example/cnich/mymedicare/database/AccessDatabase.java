/**
 * Created by Chris on 19/05/2015.
 * Provides CRUD functionality for the database
 * **/

package com.example.cnich.mymedicare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.cnich.mymedicare.AppConstants;
import com.example.cnich.mymedicare.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccessDatabase implements AppConstants {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { // String array containing all database column names
            COLUMN_ID, USERNAME, PASSWORD,
            NAME, AGE, ADDRESS1, POSTCODE,
            DOCTOR
    };

    // Constructor
    public AccessDatabase(Context context) {
        dbHelper = new DatabaseHelper(context); // Instantiate DatabaseHelper object
    }

    // Open database connection
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Close database connection
    public void close() {
        dbHelper.close();
    }

    // Creates a User entry in the database
    public User createUser(User user) {
        ContentValues values = new ContentValues(); // Name value pairs = field name, value

        // Populate values with Appointment info
        values.put(USERNAME, user.getUserName());   // Add Username to values
        values.put(PASSWORD, user.getPassword());   // Add Password to values
        values.put(NAME, user.getName());           // Add User's real name
        values.put(AGE, user.getAge());             // Add DOB
        values.put(ADDRESS1, user.getAddress1());   // Add Address line 1
        values.put(POSTCODE, user.getPostCode());   // Add Post Code to values
        values.put(DOCTOR, user.getDoctor());       // Add Doctor to values

        // Add the record to the database
        long insertId = database.insert(TABLE_ENTRIES, null, values);

        // The following outputs the details to the console as validation
        // Code can be removed when debugging finished
        // Query the database as double check
        Cursor cursor = database.query(TABLE_ENTRIES,
                allColumns, COLUMN_ID + " = " + insertId, null, null, null, null);

        cursor.moveToFirst(); // Make sure cursor is at beginning of results

        User userTemp = cursorToUser(cursor); // Get the details from the cursor
        userTemp.printUser(); // Output to the console
        cursor.close();

        return user; // Return the user
    }

    // Retrieve user (by username) from the database
    public User getUser(String username) {
        User user;
        String queryString = "USERNAME = \"" + username + "\""; // Quotes around username

        String[] columns = {COLUMN_ID, USERNAME, PASSWORD, NAME, AGE, ADDRESS1, POSTCODE, DOCTOR }; // The query expects an array of rfequired column names

        // Run query just retrieving the user
        Cursor cursor = database.query(TABLE_ENTRIES, columns, queryString , null, null, null, null);
        cursor.moveToFirst(); // Make sure cursor is pointing at first result

        user = new User(cursor); // Create new user from cursor
        // make sure to close the cursor
        cursor.close();

        return user; // Return the user
    }

    // Delete a record from the database based on its ID
    public void deleteUser(User user) {
        long id = user.getId(); // Get the ID of the user to be deleted

        if(id == 0) { // This appointment was not retrieved from the database, do not try to delete it
            System.out.println("Invalid Appointment ID, record not deleted");
            return;
        }

        // Delete the record
        database.delete(TABLE_ENTRIES, COLUMN_ID + " = " + id, null);
        System.out.println("User deleted with id: " + id);
    }

    // Update a record from the database - just updates the whole record as opposed to individual fields
    public void updateUser(User user) {
        long id = user.getId();
        ContentValues values = new ContentValues(); // Name value pairs

        // Populate values with User info
        values.put(USERNAME, user.getUserName());   // Add Username to values
        values.put(PASSWORD, user.getPassword());   // Add Password to values
        values.put(NAME, user.getName());           // Add User's real name
        values.put(AGE, user.getAge());             // Add DOB
        values.put(ADDRESS1, user.getAddress1());   // Add Address line 1
        values.put(POSTCODE, user.getPostCode());   // Add Post Code to values
        values.put(DOCTOR, user.getDoctor());       // Add Doctor to values

        if(id == 0) { // This appointment was not retrieved from the database, do not try to update it
            System.out.println("Invalid User ID, record not updated");
            return;
        }

        // Update the record
        database.update(TABLE_ENTRIES, values, COLUMN_ID + " = " + id, null);
        System.out.println("User edited with id: " + id);
    }

    // Query the database for all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();

        // Run the query, cursor will point to the resultset
        Cursor cursor = database.query(TABLE_ENTRIES, allColumns, null, null, null, null, null);

        cursor.moveToFirst(); // Make sure cursor is pointing at first result

        // Run through the results adding users to the list
        while (!cursor.isAfterLast()) {
            User appointment = cursorToUser(cursor); // get a populated user
            users.add(appointment); // Add the appointment to the appointments ArrayList
            cursor.moveToNext(); // Move cursor to next record
        }

        // make sure to close the cursor
        cursor.close();

        // Return the user list
        return users;
    }

    // Query the database for all usernames
    public List<String> getAllUserNames() {
        List<String> usernames = new ArrayList<>(); // list for usernames
        String[] columns = {USERNAME}; // The query expects an array of column names = so array of 1

        // Run query just retrieving user names
        Cursor cursor = database.query(TABLE_ENTRIES, columns, null, null, null, null, null);

        cursor.moveToFirst(); // Make sure cursor is pointing at first result

        // Run through the results adding users to the list
        while (!cursor.isAfterLast()) {
            usernames.add(cursor.getString(0)); // Populate username list
            cursor.moveToNext(); // Move cursor to next record
        }

        // make sure to close the cursor
        cursor.close();

        // Return the user list
        return usernames;
    }

    // Take the cursor values and convert into a user
    private User cursorToUser(Cursor cursor) {

        // Instantiate and populate new user
        return new User(cursor);

    }
}
