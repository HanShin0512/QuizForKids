package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelperQuiz extends SQLiteOpenHelper {

    public static String database_name = "score_record.db";
    public static final String table_name = "scores_table";
    private static final String column_email= "email";
    private static final String column_quizType = "quiz_type";
    private static final String column_score = "score";
    private static final String dateAndTime = "attempted_at DATETIME DEFAULT CURRENT_TIMESTAMP";
    public static final String create_table =
            "CREATE TABLE " + table_name + "(" +
                    column_email + " TEXT, " +
                    column_quizType + " TEXT, " +
                    column_score + " INTEGER, " +
                    dateAndTime + ")";

    public DatabaseHelperQuiz(Context context) {
        super(context, database_name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public void addScore(String email, String quizType, int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_email, email);
        values.put(column_quizType, quizType);
        values.put(column_score, score);

        // Format the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String formattedDateTime = sdf.format(new Date());

        // Add the formatted date and time to the ContentValues
        values.put("attempted_at", formattedDateTime);

        // Insert the values into the database
        db.insert(table_name, null, values);
        db.close();
    }

    public List<String> displayScoreHistory(String email) {
        List<String> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + column_email + " = ?", new String[]{email});
        int quizTypeIndex = cursor.getColumnIndex(column_quizType);
        int scoreIndex = cursor.getColumnIndex(column_score);
        int dateAndTimeIndex = cursor.getColumnIndex("attempted_at");
        if (cursor.moveToFirst()) {
            do {
                String quizType = cursor.getString(quizTypeIndex);
                int score = cursor.getInt(scoreIndex);
                // Retrieve formatted date and time from the cursor
                String formattedDateTime = cursor.getString(dateAndTimeIndex);
                // Construct the formatted score string
                String formattedScore = "\"" + quizType + "\" area - attempt started on " + formattedDateTime + " – points earned " + score;
                scoreList.add(formattedScore);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreList;
    }

    public int getTotalScore(String email) {
        int totalScore = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + column_score + " FROM " + table_name + " WHERE " + column_email + " = ?", new String[]{email});
        int scoreIndex = cursor.getColumnIndex(column_score);
        if (cursor.moveToFirst()) {
            do {
                int score = cursor.getInt(scoreIndex);
                totalScore += score;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return totalScore;
    }


    public List<String> filterByDateASC(String email) {
        List<String> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + column_email + " = ? ORDER BY attempted_at ASC", new String[]{email});
        int quizTypeIndex = cursor.getColumnIndex(column_quizType);
        int scoreIndex = cursor.getColumnIndex(column_score);
        int dateAndTimeIndex = cursor.getColumnIndex("attempted_at");
        if (cursor.moveToFirst()) {
            do {
                String quizType = cursor.getString(quizTypeIndex);
                int score = cursor.getInt(scoreIndex);
                // Retrieve formatted date and time from the cursor
                String formattedDateTime = cursor.getString(dateAndTimeIndex);
                // Construct the formatted score string
                String formattedScore = "\"" + quizType + "\" area - attempt started on " + formattedDateTime + " – points earned " + score;
                scoreList.add(formattedScore);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreList;
    }

    public List<String> filterByDateDESC(String email) {
        List<String> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + column_email + " = ? ORDER BY attempted_at DESC", new String[]{email});
        int quizTypeIndex = cursor.getColumnIndex(column_quizType);
        int scoreIndex = cursor.getColumnIndex(column_score);
        int dateAndTimeIndex = cursor.getColumnIndex("attempted_at");
        if (cursor.moveToFirst()) {
            do {
                String quizType = cursor.getString(quizTypeIndex);
                int score = cursor.getInt(scoreIndex);
                // Retrieve formatted date and time from the cursor
                String formattedDateTime = cursor.getString(dateAndTimeIndex);
                // Construct the formatted score string
                String formattedScore = "\"" + quizType + "\" area - attempt started on " + formattedDateTime + " – points earned " + score;
                scoreList.add(formattedScore);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreList;
    }

    public List<String> filterByQuizType(String email, String quizType) {
        List<String> scoreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + column_email + " = ? AND " + column_quizType + " = ?", new String[]{email, quizType});
        if (cursor.moveToFirst()) {
            do {
                int index = cursor.getColumnIndex(column_score);
                int score = cursor.getInt(index);
                // Retrieve formatted date and time from the cursor
                int dateIndex = cursor.getColumnIndex("attempted_at");
                String formattedDateTime = cursor.getString(dateIndex);
                // Construct the formatted score string
                String formattedScore = "\"" + quizType + "\" area - attempt started on " + formattedDateTime + " – points earned " + score;
                scoreList.add(formattedScore);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return scoreList;
    }


}
