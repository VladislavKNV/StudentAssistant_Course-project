package com.example.studentassistant.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int SCHEMA = 1;
    private static final String DATABASE_NAME = "STUDENTASSISTANTDB";
    private static final String USER_TABLE = "USER";
    private static final String ROLES_TABLE = "ROLES";
    private static final String SUBJECTS_TABLE = "SUBJECTS";
    private static final String LABS_TABLE = "LABS";
    private static final String SESSION_TABLE = "SESSION";

    private static DbHelper instance = null;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    public static DbHelper getInstance(Context context) {
        if(instance == null) instance = new DbHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ROLES_TABLE + " (                      "
                + "IDROLE integer primary key not null,                     "
                + "ROLENAME text                                        );  "
        );
        db.execSQL("create table " + USER_TABLE + " (                           "
                + "IDUSER integer primary key not null,           "
                + "ROLEID integer,                                              "
                + "LOGIN text not null UNIQUE,                                  "
                + "EMAIL text not null UNIQUE,                                  "
                + "PASSWORD text not null,                                      "
                + "foreign key(ROLEID) references " + ROLES_TABLE + "(IDROLE)   "
                + " on delete cascade                                        ); "
        );
        db.execSQL("create table " + SUBJECTS_TABLE + " (                       "
                + "IDSUBJECT integer primary key not null,        "
                + "USERID integer,                                              "
                + "SUBJECTNAME text not null UNIQUE,                            "
                + "foreign key(USERID) references " + USER_TABLE + "(IDUSER)    "
                + " on delete cascade                                       );  "
        );
        db.execSQL("create table " + LABS_TABLE + " (                                   "
                + "IDLAB integer primary key not null,                    "
                + "SUBJECTID integer,                                                   "
                + "LABPROTECTED integer not null,                                       "
                + "foreign key(SUBJECTID) references " + SUBJECTS_TABLE + "(IDSUBJECT)  "
                + " on delete cascade                                                 );"
        );
        db.execSQL("create table " + SESSION_TABLE + " (                                "
                + "IDSESSION integer primary key not null,                "
                + "SUBJECTID integer,                                                   "
                + "TYPE text not null,                                                  "
                + "STATUS text not null,                                                "
                + "MARK integer,                                                        "
                + "DATETIME text,                                                       "
                + "AUDITORIUM text,                                                     "
                + "foreign key(SUBJECTID) references " + SUBJECTS_TABLE + "(IDSUBJECT)  "
                + " on delete cascade                                                 );"
        );

        // добавление данных в таблицу ROLES_TABLE
        ContentValues adminValues = new ContentValues();
        adminValues.put("IDROLE", 1);
        adminValues.put("ROLENAME", "Administrator");
        db.insert(ROLES_TABLE, null, adminValues);

        ContentValues userValues = new ContentValues();
        userValues.put("IDROLE", 2);
        userValues.put("ROLENAME", "User");
        db.insert(ROLES_TABLE, null, userValues);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + USER_TABLE);
        db.execSQL("drop table if exists " + ROLES_TABLE);
        db.execSQL("drop table if exists " + SUBJECTS_TABLE);
        db.execSQL("drop table if exists " + LABS_TABLE);
        db.execSQL("drop table if exists " + SESSION_TABLE);
        onCreate(db);
    }
}
