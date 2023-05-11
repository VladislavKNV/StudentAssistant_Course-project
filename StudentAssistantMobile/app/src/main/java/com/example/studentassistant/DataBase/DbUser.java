package com.example.studentassistant.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studentassistant.Model.UserModel;


public class DbUser {

    private static final String USER_TABLE = "USER";

    public static long add(SQLiteDatabase db, UserModel userModel) {
        ContentValues values = new ContentValues();

        values.put("IDUSER", userModel.getId());
        values.put("ROLEID", userModel.getRoleId());
        values.put("LOGIN", userModel.getLogin());
        values.put("EMAIL", userModel.getEmail());
        values.put("PASSWORD", userModel.getPassword());

        return db.insert(USER_TABLE, null, values);
    }

    public static Cursor getAll(SQLiteDatabase db) {//пусть будет, но потом убрать
        return db.rawQuery("select * from " + USER_TABLE + ";", null);
    }

    public static Cursor getUserByLogin(SQLiteDatabase db, String login) {
        return db.rawQuery("select * from " + USER_TABLE + " where " + "LOGIN = ?", new String[]{String.valueOf(login)});//string
    }

    public static long deleteUser(SQLiteDatabase db, int id) {
        return db.delete(USER_TABLE, "IDUSER = ?", new String[] {String.valueOf(id)});
    }
}
