package com.phamngoc.sofusers.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phamngoc.sofusers.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SOFUsers.db";
    private static final String TABLE_BOOKMARKEDUSERS = "BookmarkedUsers";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AVATAR = "avatar";
    private static final String REPUTATION = "reputation";
    private static final String LOCATION = "location";
    private static final String LASTACCESSDATE = "lastAccessDate";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "CREATE TABLE " + TABLE_BOOKMARKEDUSERS + " ( " +
                ID + " VARCHAR PRIMARY KEY, " +
                NAME + " TEXT, " +
                AVATAR + " TEXT, " +
                REPUTATION + " INTEGER," +
                LOCATION + " TEXT, " +
                LASTACCESSDATE + " TEXT" +
                ")";

        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void BookMarkUser(User user){
        SQLiteDatabase db = getReadableDatabase();
        String queryInsertOrUpdate = "INSERT OR REPLACE INTO " + TABLE_BOOKMARKEDUSERS + " ( " +
                ID +", " + NAME +", " + AVATAR +", " +REPUTATION +", " + LOCATION +", " + LASTACCESSDATE +" ) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        db.execSQL(queryInsertOrUpdate, new String[]{user.id, user.name, user.avatar, String.valueOf(user.reputation), user.location, String.valueOf(user.lastAccessDate)});
    }

    public List<User> GetAllBookmarked(){
        List<User> users = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ID +", " + NAME +", " + AVATAR +", " +REPUTATION +", " + LOCATION +", " + LASTACCESSDATE + " from " + TABLE_BOOKMARKEDUSERS, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            while (true) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String avatar = cursor.getString(2);
                Double reputation = cursor.getDouble(3);
                String location = cursor.getString(4);
                long lastAccessDate = cursor.getLong(5);

                users.add(new User(id, name, avatar, reputation, location, lastAccessDate, true));

                if(cursor.isLast()){
                    break;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();

        return users;
    }

    public void RemoveBookmard(String userid){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOOKMARKEDUSERS + " where id = ?", new String[]{userid});
    }

    public List<String> GetAllBookmarkedUserIDs(){

        List<String> ids = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ID + " from " + TABLE_BOOKMARKEDUSERS, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            while (true) {
                String id = cursor.getString(0);

                ids.add(id);

                if(cursor.isLast()){
                    break;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();

        return ids;
    }

}
