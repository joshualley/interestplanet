package com.example.interestplanet.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SQLiteRecordService extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PlanetSetting.db";
    public static final String TABLE_NAME = "user_record";
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SQLiteRecordService(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public class UserRecord {
        public String rid;
        public String username;
        public String password;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USERNAME TEXT," +
                "PASSWORD INTEGER," +
                "DATE DATETIME" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addOrUpdate(String username, String password) {
        UserRecord record = getByUsername(username);
        if (record == null) {
            return insert(username, password);
        } else {
            return update(record.rid, username, password);
        }
    }

    public boolean insert(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", password);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return !(result == -1);
    }

    public UserRecord getLastOne() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME +
                " order by DATE desc limit 1", null);
        UserRecord record = null;
        if (res.moveToNext()) {
            record = new UserRecord();
            record.rid = res.getString(0);
            record.username = res.getString(1);
            record.password = res.getString(2);
        }
        res.close();
        return record;
    }

    public UserRecord getByUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[] { username };
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where USERNAME = ?", args);
        UserRecord record = null;
        if (res.moveToNext()) {
            record = new UserRecord();
            record.rid = res.getString(0);
            record.username = res.getString(1);
            record.password = res.getString(2);
        }
        res.close();
        return record;
    }

    public boolean update(String id, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String now = FORMATTER.format(new Date(System.currentTimeMillis()));
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", password);
        contentValues.put("DATE", now);
        int result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return !(result == 0);
    }

    public Integer delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public Integer deleteByUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "USERNAME = ?", new String[]{username});
    }
}
