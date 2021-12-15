package com.rhythm.mypizzaapp.utils.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.rhythm.mypizzaapp.getterAndSetterClasses.PaymentCardListBeanClass;

import java.util.ArrayList;

public class PaymentCardDatabaseHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PaymentCardManager.db";
    // Signup table name
    private static final String TABLE_CARD = "cardDetails";

    // Signup Table Columns names
    private static final String COLUMN_CARD_ID = "card_id";
    private static final String COLUMN_CARD_HOLDER_NAME = "card_holder_name";
    private static final String COLUMN_CARD_NUMBER = "card_number";
    private static final String COLUMN_CARD_CVV = "card_cvv";
    private static final String COLUMN_EXPIRY_DATE = "card_expiry";

    public PaymentCardDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create table sql query
    private String CREATE_SIGNUP_TABLE = "CREATE TABLE " + TABLE_CARD + "("
            + COLUMN_CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CARD_NUMBER +
            " NUMERIC NOT NULL UNIQUE," + COLUMN_CARD_HOLDER_NAME + " TEXT," + COLUMN_CARD_CVV + " TEXT," +
            COLUMN_EXPIRY_DATE + " TEXT" + ")";
    // drop table sql query
    private String DROP_CARD_TABLE = "DROP TABLE IF EXISTS " + TABLE_CARD;
    private String DELETE_CARD_TABLE = "DELETE FROM " + TABLE_CARD;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SIGNUP_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop cardDetail Table if exist
        sqLiteDatabase.execSQL(DROP_CARD_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /**
     * This method is to create payment card record
     */
    public void addCard(PaymentCardListBeanClass data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARD_HOLDER_NAME, data.getCardHolderName());
        values.put(COLUMN_CARD_NUMBER, data.getCardNumber());
        values.put(COLUMN_CARD_CVV, data.getCardCvv());
        values.put(COLUMN_EXPIRY_DATE, data.getCardExpiryDate());
        // Inserting Row
        db.insert(TABLE_CARD, null, values);
        db.close();
    }

    /**
     * This method is to fetch all the cards and return the list of card records
     *
     * @return list
     */
    public ArrayList<PaymentCardListBeanClass> getAllCards() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CARD_ID,
                COLUMN_CARD_HOLDER_NAME,
                COLUMN_CARD_NUMBER,
                COLUMN_CARD_CVV,
                COLUMN_EXPIRY_DATE
        };
        // sorting orders
        String sortOrder =
                COLUMN_CARD_ID + " ASC";
        ArrayList<PaymentCardListBeanClass> cardsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the cardDetail table
        /**
         * Here query function is used to fetch records from cardDetails table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT card_id,card_HolderName,card_number, card_cvv, card_expiry FROM cardDetails ORDER BY card_Id;
         */
        Cursor cursor = db.query(TABLE_CARD, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PaymentCardListBeanClass cards = new PaymentCardListBeanClass();
                cards.setCardHolderName(cursor.getString(cursor.getColumnIndex(COLUMN_CARD_HOLDER_NAME)));
                cards.setCardNumber(cursor.getString(cursor.getColumnIndex(COLUMN_CARD_NUMBER)));
                cards.setCardCvv(cursor.getString(cursor.getColumnIndex(COLUMN_CARD_CVV)));
                cards.setCardExpiryDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPIRY_DATE)));
                // Adding user record to list
                cardsList.add(cards);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return card list
        return cardsList;
    }

    /**
     * This method is to fetch single card and return the list of card's records
     *
     * @return list
     */
    public ArrayList<PaymentCardListBeanClass> getCardDetail(String cardNumber) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CARD_ID,
                COLUMN_CARD_HOLDER_NAME,
                COLUMN_CARD_NUMBER,
                COLUMN_CARD_CVV,
                COLUMN_EXPIRY_DATE
        };

        ArrayList<PaymentCardListBeanClass> cardsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CARD_NUMBER + " = ?";
        // selection argument
        String[] selectionArgs = {cardNumber};
        // query the user table
        /**
         * Here query function is used to fetch records from cardDetails table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT card_id,card_HolderName,card_number, card_cvv, card_expiry FROM cardDetails WHERE card_number = '1234 1234 1234 1234';
         */
        Cursor cursor = db.query(TABLE_CARD, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PaymentCardListBeanClass cards = new PaymentCardListBeanClass();
                cards.setCardHolderName(cursor.getString(cursor.getColumnIndex(COLUMN_CARD_HOLDER_NAME)));
                cards.setCardNumber(cursor.getString(cursor.getColumnIndex(COLUMN_CARD_NUMBER)));
                cards.setCardCvv(cursor.getString(cursor.getColumnIndex(COLUMN_CARD_CVV)));
                cards.setCardExpiryDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPIRY_DATE)));
                // Adding user record to list
                cardsList.add(cards);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return cardsList;
    }

    /**
     * This method is to delete single card record
     */
    public boolean deleteCard(String cardNumber) {
        // delete user record by email
        if (checkCard(cardNumber)) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_CARD, COLUMN_CARD_NUMBER + " = ?",
                    new String[]{String.valueOf(cardNumber)});
            db.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method to check user exist or not
     *
     * @return true/false
     */
    public boolean checkCard(String cardNumber) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CARD_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CARD_NUMBER + " = ?";
        // selection argument
        String[] selectionArgs = {cardNumber};
        // query user table with condition
        /**
         * Here query function is used to fetch records from cardDetail table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT card_id FROM signup WHERE card_number = '1234 1234 1234 1234';
         */
        Cursor cursor = db.query(TABLE_CARD, //Table to query
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

    /** this will delete the table when user is logged out from the application
     * */
    public void deleteTable(){
        //Drop cardDetail Table if exist
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(DELETE_CARD_TABLE);
    }
}
