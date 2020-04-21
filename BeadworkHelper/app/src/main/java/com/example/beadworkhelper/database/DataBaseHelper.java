package com.example.beadworkhelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "beadwork";
    public static final String TABLE_WORKS = "work";
    public static final String TABLE_MATERIALS = "material";

    public static final String KEY_ID ="_id";
    public static final String KEY_NAME="name";
    public static final String KEY_LINK_PICTURE ="linkPicture";
    public static final String KEY_LINK_SCHEME ="linkScheme";
    public static final String KEY_NOTES="notes";
    public static final String KEY_STATE="state";

    public static final String MATERIAL_KEY_ID ="_id";
    public static final String MATERIAL_KEY_NAME="materialName";
    public static final String MATERIAL_KEY_DESCRIPTION ="description";
    public static final String MATERIAL_KEY_QUANTITY ="quantity";
    public static final String MATERIAL_KEY_PRICE="price";
    public static final String MATERIAL_KEY_WORK_ID="work_id";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_WORKS+" ("
                + KEY_ID + " integer primary key, "
                + KEY_NAME + " text, "
                + KEY_LINK_PICTURE + " text, "
                + KEY_LINK_SCHEME  + " text, "
                + KEY_NOTES + " text, "
                +KEY_STATE + " text"
                +")"
        );
        db.execSQL("create table "+TABLE_MATERIALS+"("
                    + MATERIAL_KEY_ID + " integer primary key, "
                    + MATERIAL_KEY_NAME + " text, "
                    + MATERIAL_KEY_DESCRIPTION + " text, "
                    + MATERIAL_KEY_QUANTITY + " integer, "
                    + MATERIAL_KEY_PRICE + " integer, "
                    + MATERIAL_KEY_WORK_ID + " integer"
                    + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" drop table if exists " + TABLE_WORKS);
        db.execSQL(" drop table if exists " + TABLE_MATERIALS);
        onCreate(db);
    }
}
