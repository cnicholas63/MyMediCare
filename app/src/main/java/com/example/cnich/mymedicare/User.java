package com.example.cnich.mymedicare;

import android.database.Cursor;

import java.util.HashMap;

/**
 * Created by C Nicholas on 10/05/2016.
 * Class hold user details and functionality.
 */
public class User implements AppConstants {


    long id;         // User ID - primary key from database
    String userName; // Username
    String password; // Password
    String name;     // User's real name
    String age;      // User's age
    String address1; // Address line 1
    String postCode; // Post code
    String doctor;   // Doctor or nurse's name

    /**
     * Constructor, Constructs a new user,  using details provided in the hashmap
    */
    public User(HashMap<String, Object> details) {
        id = 0; // This user is not constructed from database cursor so not needed
        userName = (String)details.get(USERNAME); // Get the username
        password = (String)details.get(PASSWORD); // Get the password
        name = (String)details.get(NAME);         // Get the user's real name
        age = (String)details.get(AGE);           // Get the Date of Birth
        address1 = (String)details.get(ADDRESS1); // Get the Address
        postCode = (String)details.get(POSTCODE); // Get the Post Code
        doctor = (String)details.get(DOCTOR);     // Get the Doctor/Nurse's name
    }

    /**
     * Create new user from database cursor
     * @param cursor Cursor containing the new user's details
     */
    public User(Cursor cursor) {
        id = cursor.getInt(0);          // Get the ID from the cursor (primary key from database
        userName = cursor.getString(1); // Get the username
        password = cursor.getString(2); // Get the password
        name = cursor.getString(3);     // Get the user's real name
        age = cursor.getString(4);      // Get the age
        address1 = cursor.getString(5); // Get the Address
        postCode = cursor.getString(6); // Get the Post Code
        doctor = cursor.getString(7);   // Get the Doctor/Nurse's name
    }

    /**
     * Constructs new - empty user
     */
    public User() {
        // User details must be set using setters
    }

    // Get user ID
    public long getId() {
        return id;
    }

    // Sets user ID
    public void setID(long id) {
        this.id = id;
    }

    // Gets username
    public String getUserName() {
        return userName;
    }

    // Sets username
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Gets password
    public String getPassword() {
        return password;
    }

    // Sets the password
    public void setPassword(String password) {
        this.password = password;
    }

    // Gets proper name
    public String getName() {
        return name;
    }

    // Set proper name
    public void setName(String name) {
        this.name = name;
    }

    // Get DOB
    public String getAge() {
        return age;
    }

    // Set DOB
    public void setAge(String age) {
        this.age = age;
    }

    // Get Address line 1
    public String getAddress1() {
        return address1;
    }

    // Set Address line 1
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    // Get Post Code
    public String getPostCode() {
        return postCode;
    }

    // Set Post Code
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    // Get Doctor/Nurse's name
    public String getDoctor() {
        return doctor;
    }

    // Set Doctor/Nurse's name
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }


    // Outputs the contents of the user to the console
    public void printUser() {
        System.out.println("User:");
        System.out.println("ID = " + id);
        System.out.println("UserName = " + userName);
        System.out.println("Password = " + password);
        System.out.println("Name = " + name);
        System.out.println("Age = " + age);
        System.out.println("Addr1" + address1);
        System.out.println("Post Code = " + postCode);
        System.out.println("Doctor = " + doctor);
    }
}
