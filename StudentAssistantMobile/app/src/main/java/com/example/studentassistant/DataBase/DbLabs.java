package com.example.studentassistant.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.SubjectModel;

public class DbLabs {

    private static final String LABS_TABLE = "LABS";

    public static long add(SQLiteDatabase db, LabModel labModel) {
        ContentValues values = new ContentValues();

        values.put("SUBJECTID", labModel.getIdSubject());
        values.put("LABPROTECTED", labModel.getLabProtected());

        return db.insert(LABS_TABLE, null, values);
    }

    public static Cursor getLabsBySubjectId(SQLiteDatabase db, int id) {
        return db.rawQuery("select * from " + LABS_TABLE + " where " + "SUBJECTID = ?", new String[]{String.valueOf(id)});//string
    }


    public static long deleteLab(SQLiteDatabase db, int id) {
        return db.delete(LABS_TABLE, "IDLAB = ?", new String[] {String.valueOf(id)});
    }

    public static long updateLabById(SQLiteDatabase db, int labId,  int labProtected) {
        ContentValues values = new ContentValues();

        values.put("LABPROTECTED", labProtected);

        return db.update(LABS_TABLE, values, "IDLAB = ?", new String[] { String.valueOf(labId) });
    }

    public static int getNumberOfLabsBySubjectId(SQLiteDatabase db, int subjectId) {
        String query = "SELECT COUNT(*) FROM " + LABS_TABLE + " WHERE SUBJECTID = ?";
        String[] selectionArgs = {String.valueOf(subjectId)};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public static int getNumberOfProtectedLabsBySubjectId(SQLiteDatabase db, int subjectId) {
        String query = "SELECT COUNT(*) FROM " + LABS_TABLE + " WHERE SUBJECTID = ? AND LABPROTECTED = 1";
        String[] selectionArgs = {String.valueOf(subjectId)};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

}
