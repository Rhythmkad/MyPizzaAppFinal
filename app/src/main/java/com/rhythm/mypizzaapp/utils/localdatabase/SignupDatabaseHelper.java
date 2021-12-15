package com.rhythm.mypizzaapp.utils.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.rhythm.mypizzaapp.getterAndSetterClasses.SignupBeanClass;

import java.util.ArrayList;

/** here we are creating a local SQL lite database
 * through SQLiteOpen helper
 *  In this class we create database, create table,
 *  than calling methods of this class
 *  to addUser, remove user, get all the users in the DB
 *  for Signup fragment
 * */

public class SignupDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SignupManager.db";
    // Signup table name
    private static final String TABLE_SIGNUP = "signup";

    // Signup Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_PHONE = "user_phone";
    private static final String COLUMN_USER_ADDRESS = "user_address";
    private static final String COLUMN_USER_GENDER = "user_gender";

    // create table sql query
    private String CREATE_SIGNUP_TABLE = "CREATE TABLE " + TABLE_SIGNUP + "("
   + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_PHONE + " NUMERIC," + COLUMN_USER_ADDRESS + " TEXT," +
            COLUMN_USER_GENDER + " TEXT" + ")";
    // drop table sql query
    private String DROP_SIGNUP_TABLE = "DROP TABLE IF EXISTS " + TABLE_SIGNUP;

    public SignupDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SIGNUP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    //Drop signup Table if exist
        sqLiteDatabase.execSQL(DROP_SIGNUP_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);

    }

    /**
     * This method is to create signup record
     *
     */
    public void addUser(SignupBeanClass data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, data.getEmail());
        values.put(COLUMN_USER_NAME, data.getName());
        values.put(COLUMN_USER_PHONE, data.getPhoneNumber());
        values.put(COLUMN_USER_PASSWORD, data.getPassword());
        values.put(COLUMN_USER_ADDRESS, data.getAddress());
        values.put(COLUMN_USER_GENDER, data.getGender());
        // Inserting Row
        db.insert(TABLE_SIGNUP, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public ArrayList<SignupBeanClass> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_PHONE,
                COLUMN_USER_ADDRESS,
                COLUMN_USER_GENDER
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        ArrayList<SignupBeanClass> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /**
         * Here query function is used to fetch records from signup table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM signup ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_SIGNUP, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SignupBeanClass user = new SignupBeanClass();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
                user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_USER_GENDER)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }

    /**
     * This method is to fetch single user and return the list of user's records
     *
     * @return list
     */
    public ArrayList<SignupBeanClass> getUserDetail(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_PHONE,
                COLUMN_USER_ADDRESS,
                COLUMN_USER_GENDER
        };

        ArrayList<SignupBeanClass> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query the user table
        /**
         * Here query function is used to fetch records from signup table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM signup WHERE user_email = 'xyz@gmail.com';
         */
        Cursor cursor = db.query(TABLE_SIGNUP, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SignupBeanClass user = new SignupBeanClass();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
                user.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_USER_GENDER)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }



    /**
     * This method to update signup record
     *
     */
    public void updateUser(SignupBeanClass data, String previousEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, data.getEmail());
        values.put(COLUMN_USER_NAME, data.getName());
        values.put(COLUMN_USER_PHONE, data.getPhoneNumber());
        values.put(COLUMN_USER_PASSWORD, data.getPassword());
        values.put(COLUMN_USER_ADDRESS, data.getAddress());
        values.put(COLUMN_USER_GENDER, data.getGender());
        // updating row
        db.update(TABLE_SIGNUP, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(previousEmail)});
        db.close();
    }

    /**
     * This method is to delete signup record
     *
     */
    public void deleteUser(SignupBeanClass data) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by email
        db.delete(TABLE_SIGNUP, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(data.getEmail())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     */
    public boolean deleteUser(String email) {
        // delete user record by email
        if (checkUser(email)) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_SIGNUP, COLUMN_USER_EMAIL + " = ?",
                    new String[]{String.valueOf(email)});
            db.close();
            return true;
        }else{
            return false;
        }

    }

    /**
     * This method to check user exist or not
     *
     * @return true/false
     */
    public boolean checkUser(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};
        // query user table with condition
        /**
         * Here query function is used to fetch records from signup table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM signup WHERE user_email = 'xyz@gmail.com';
         */
        Cursor cursor = db.query(TABLE_SIGNUP, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from signup table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM signup WHERE user_email = 'xyz@gmail.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_SIGNUP, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


}
