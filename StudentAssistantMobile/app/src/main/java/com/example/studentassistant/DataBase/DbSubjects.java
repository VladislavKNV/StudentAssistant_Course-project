package com.example.studentassistant.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.Model.UserModel;

public class DbSubjects {

    private static final String SUBJECTS_TABLE = "SUBJECTS";

    public static long add(SQLiteDatabase db, SubjectModel subjectModel) {
        ContentValues values = new ContentValues();

        values.put("USERID", subjectModel.getIdUser());
        values.put("SUBJECTNAME", subjectModel.getSubjectName());

        return db.insert(SUBJECTS_TABLE, null, values);
    }

    public static Cursor getAllSubjects(SQLiteDatabase db) {
        return db.rawQuery("select * from " + SUBJECTS_TABLE + ";", null);
    }

    public static Cursor getSubjectByName(SQLiteDatabase db, String name) {
        return db.rawQuery("select * from " + SUBJECTS_TABLE + " where " + "SUBJECTNAME = ?", new String[]{String.valueOf(name)});
    }

    public static Cursor getUserSubjectsByIdUser(SQLiteDatabase db, int id) {
        return db.rawQuery("select * from " + SUBJECTS_TABLE + " where USERID = ? ORDER BY SUBJECTNAME", new String[]{String.valueOf(id)});
    }

    public static long updateSubjectById(SQLiteDatabase db, int id,  String newName) {//допилить
        ContentValues values = new ContentValues();

        values.put("SUBJECTNAME", newName);

        return db.update(SUBJECTS_TABLE, values, "IDSUBJECT = ?", new String[] { String.valueOf(id) });
    }

    public static long deleteSubject(SQLiteDatabase db, int id) {
        return db.delete(SUBJECTS_TABLE, "IDSUBJECT = ?", new String[] {String.valueOf(id)});
    }
}
