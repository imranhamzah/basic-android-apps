package com.easytravelapp17.jodoh;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1 = "user_id";
    public static final String COL_2 = "user_name";
    public static final String COL_3 = "fullname";
    public static final String COL_4 = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (user_id INTEGER PRIMARY KEY AUTOINCREMENT,user_name VARCHAR,fullname TEXT,password TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String userName,String fullName,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,userName);
        contentValues.put(COL_3,fullName);
        contentValues.put(COL_4,password);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor validateUser(String LoginId, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME + " WHERE user_name = '"+LoginId+"' AND password = '"+password+"' LIMIT 1",null);
        return res;
    }

    public Cursor getUserDataByLoginId(String LoginId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME + " WHERE user_name = '"+LoginId+"'",null);
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String userId, String userName,String fullName,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,userId);
        contentValues.put(COL_2,userName);
        contentValues.put(COL_3,fullName);
        contentValues.put(COL_4,password);
        db.update(TABLE_NAME, contentValues, "user_id = ?",new String[] { userId });
        return true;
    }

    public Integer deleteData (String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {userId});
    }
}