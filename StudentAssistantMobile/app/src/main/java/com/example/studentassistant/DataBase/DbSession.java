package com.example.studentassistant.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentassistant.Model.SessionModel;

public class DbSession {
    private static final String SESSION_TABLE = "SESSION";

    public static long add(SQLiteDatabase db, SessionModel sessionModel) {
        ContentValues values = new ContentValues();

        values.put("IDSESSION", sessionModel.getId());
        values.put("SUBJECTID", sessionModel.getIdSubject());
        values.put("TYPE", sessionModel.getType());
        values.put("STATUS", sessionModel.getStatus());

        return db.insert(SESSION_TABLE, null, values);
    }

    public static int getCountOfExams(SQLiteDatabase db) {
        String query = "SELECT COUNT(*) FROM " + SESSION_TABLE + " WHERE TYPE = 'Экзамен'";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public static int getCountOfOffset(SQLiteDatabase db) {
        String query = "SELECT COUNT(*) FROM " + SESSION_TABLE + " WHERE TYPE = 'Зачёт'";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public static int getCountStatus(SQLiteDatabase db) {
        String query = "SELECT COUNT(*) FROM " + SESSION_TABLE + " WHERE STATUS = 'Не допущен'";
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public static double getAverageMark(SQLiteDatabase db) {
        String query = "SELECT AVG(MARK) FROM " + SESSION_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        double avgMark = 0;
        if (cursor.moveToFirst()) {
            avgMark = cursor.getDouble(0);
        }
        cursor.close();
        return avgMark;
    }

    public static Cursor getSessionBySubjectId(SQLiteDatabase db, int id) {
        return db.rawQuery("select * from " + SESSION_TABLE + " where " + "SUBJECTID = ?", new String[]{String.valueOf(id)});
    }

    public static long updateSessionBySubjectId(SQLiteDatabase db, int id,  String newStatus) {//допилить
        ContentValues values = new ContentValues();

        values.put("STATUS", newStatus);

        return db.update(SESSION_TABLE, values, "SUBJECTID = ?", new String[] { String.valueOf(id) });
    }

    public static long updateFullSessionBySubjectId(SQLiteDatabase db, int id, String newType, String newStatus, int newMark, String newDateTime, String newAuditorium) {//допилить
        ContentValues values = new ContentValues();

        values.put("TYPE", newType);
        values.put("STATUS", newStatus);
        values.put("MARK", newMark);
        values.put("DATETIME", newDateTime);
        values.put("AUDITORIUM", newAuditorium);

        return db.update(SESSION_TABLE, values, "SUBJECTID = ?", new String[] { String.valueOf(id) });
    }

}

