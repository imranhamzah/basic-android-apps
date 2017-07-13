package com.easytravelapp17.jodoh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelperTableWhishList extends SQLiteOpenHelper {

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "myDb";
    public static final String TABLE_NAME = "tbl_whishlist";
    public static final String COLUMN_WHISHLIST_ID = "whishlist_id";
    public static final String COLUMN_SRC_USR_ID = "src_usr_id";
    public static final String COLUMN_TGT_USR_ID = "trg_usr_id";
    public static final String COLUMN_SYNC_STATUS = "sync_status    ";

    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DbHelperTableWhishList(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME +
                 "(" +
                    COLUMN_WHISHLIST_ID + " INTEGER AUTO_INCREMENT, " +
                    COLUMN_SRC_USR_ID + " INTEGER NOT NULL DEFAULT '0', " +
                    COLUMN_TGT_USR_ID + " INTEGER NOT NULL DEFAULT '0'," +
                    COLUMN_SYNC_STATUS + " TINYINT" +
                 ");";
        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS tbl_whishlist";
        db.execSQL(sql);
        onCreate(db);
    }

    /*
    * This method is taking two arguments
    * first one is the name that is to be saved
    * second one is the status
    * 0 means the name is synced with the server
    * 1 means the name is not synced with the server
    * */
    public boolean manageAddToWhishList(int src_usr_id, int tgt_usr_id, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SRC_USR_ID, src_usr_id);
        contentValues.put(COLUMN_TGT_USR_ID, tgt_usr_id);
        contentValues.put(COLUMN_SYNC_STATUS, sync_status);


        Log.i("database_insert",contentValues.toString());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    /*
    * This method taking two arguments
    * first one is the id of the name for which
    * we have to update the sync status
    * and the second one is the status that will be changed
    * */
    public boolean updateWhishListStatus(int whishListId, int syncStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SYNC_STATUS, syncStatus);
        db.update(TABLE_NAME, contentValues, COLUMN_WHISHLIST_ID + "=" + whishListId, null);
        db.close();
        return true;
    }

    /*
    * this method will give us all the name stored in sqlite
    * */
    public Cursor getWhishListBySrcUsrId(int src_usr_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SRC_USR_ID  + " = '"+src_usr_id+"' LIMIT 1";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
    * this method is for getting all the unsynced whishlist status
    * so that we can sync it with database
    * */
    public Cursor getUnsyncedWhishList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SYNC_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
}
