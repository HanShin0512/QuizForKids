package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseHelperLogin extends SQLiteOpenHelper {

    public static final String database_name = "account_info.db";
    public static final String table_accounts = "accounts"; // table

    //column
    private static final String column_id = "ID";
    private static final String column_email = "email";
    private static final String column_name = "username";
    private static final String column_psw = "password";
    private static final String column_logged_in = "logged_in";
    private static final String column_timestamp = "timestamp";


    //create table sql statement
    private static final String create_table_accounts =
            "CREATE TABLE " + table_accounts + "(" +
                    column_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    column_email + " TEXT, " +
                    column_name + " TEXT, " +
                    column_psw + " TEXT, " +
                    column_logged_in + " INTEGER DEFAULT 0, " +
                    column_timestamp + " DATETIME DEFAULT CURRENT_TIMESTAMP" + // Default timestamp
                    ")";

    public DatabaseHelperLogin(Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create tables
        db.execSQL(create_table_accounts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop existing table and just create new one to upgrade
        db.execSQL("DROP TABLE IF EXISTS " + table_accounts);
        onCreate(db);
    }

    //store login info
    public boolean insertSignUpInfo(String email, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column_email, email);
        cv.put(column_name, username);
        cv.put(column_psw, password);
        long result = db.insert(table_accounts, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean changePassword(String email, String newPsw){
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to hold the new password
        ContentValues cv = new ContentValues();
        cv.put(column_psw, newPsw);

        // Perform the update operation
        int numRowsUpdated = db.update(table_accounts, cv, column_email + " = ?", new String[]{email});

        // Close the database connection
        db.close();

        // Check if any rows were updated (i.e., if the password was changed)
        return numRowsUpdated > 0;
    }

    public boolean checkLogin(String email, String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {column_email};

        // Define the selection criteria
        String selection = column_email + "=? AND " + column_name + "=? AND " + column_psw + "=?";

        // Define the selection arguments
        String[] selectionArgs = {email, username, password};

        // Query the database
        Cursor cursor = db.query(table_accounts, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        // Close cursor and database
        cursor.close();
        db.close();

        // Return true if count > 0, indicating login credentials are correct
        return count > 0;
    }

    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {column_name};

        // Define the selection criteria
        String selection = column_email + "=?";

        // Define the selection argument
        String[] selectionArgs = {email};

        // Query the database
        Cursor cursor = db.query(table_accounts, columns, selection, selectionArgs, null, null, null);

        String username = null;
        // Check if the cursor has data
        if (cursor.moveToFirst()) {
            // Retrieve the username from the cursor
            int index = cursor.getColumnIndex(column_name);
            username = cursor.getString(index);
        }

        // Close cursor and database
        cursor.close();
        db.close();

        return username;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {column_email};

        // Define the selection criteria
        String selection = column_email + "=?";

        // Define the selection argument
        String[] selectionArgs = {email};

        // Query the database
        Cursor cursor = db.query(table_accounts, columns, selection, selectionArgs, null, null, null);

        boolean exists = cursor.getCount() > 0;

        // Close cursor and database
        cursor.close();
        db.close();

        return exists;
    }

    // Method to mark user as logged in
    public void setUserLoggedIn(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column_logged_in, 1); // Set logged_in to 1
        db.update(table_accounts, cv, column_email + "=?", new String[]{email});
        db.close();
    }

    // Method to check if user is logged in
    public boolean isUserLoggedIn(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_accounts, new String[]{column_logged_in}, column_email + "=? AND " + column_logged_in + "=?", new String[]{email, "1"}, null, null, null);
        boolean loggedIn = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return loggedIn;
    }

    // Method to get the email of the last added account
    public String getLastAddedEmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        String email = null;
        Cursor cursor = db.query(table_accounts, new String[]{column_email}, null, null, null, null, column_timestamp + " DESC", "1");
        int index = cursor.getColumnIndex(column_email);
        if (cursor.moveToFirst()) {
            email = cursor.getString(index);
        }
        cursor.close();
        db.close();
        return email;
    }


    // Method to log out user
    public void logoutUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column_logged_in, 0); // Set logged_in to 0
        db.update(table_accounts, cv, column_email + "=?", new String[]{email});
        db.close();
    }


    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table_accounts);
        db.close();
    }


    }
