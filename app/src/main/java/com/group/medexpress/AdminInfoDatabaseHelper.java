package com.group.medexpress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminInfoDatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Admins";
    private static final String COL1 = "ID";
    private static final String COL2 = "admin_id";
    private static final String COL3 = "personal_id";
    private static final String COL4 = "role";
    private static final String COL5 = "image_url";


    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 + " TEXT," +
                    COL3 + " TEXT," +
                    COL4 + " TEXT," +
                    COL5 + " TEXT)";


    public AdminInfoDatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, id);


        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;

    }


    public void addDataByImageUrl(String image_url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL5, image_url);

        // Which row to update, based on the title
        String selection = COL5 + " LIKE ?";
        String[] selectionArgs = { "" };

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void deleteDataByName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "name=?", new String[]{name});
        db.close();
    }


    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }


    public boolean isEmpty(){
        SQLiteDatabase database = this.getReadableDatabase();
        long NoOfRows = DatabaseUtils.queryNumEntries(database,TABLE_NAME);

        if (NoOfRows == 0){
            return true;
        } else {
            return false;
        }
    }




    public void updateUserName(String oldName, String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, newName);
        db.update(TABLE_NAME, cv, COL2 + "=?", new String[] {oldName});
        db.close();
    }


}
