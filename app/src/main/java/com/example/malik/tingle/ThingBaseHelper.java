package com.example.malik.tingle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by malik on 3/14/16.
 */
public class ThingBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "thingBase.db";

    ThingBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ThingDbSchema.ThingTable.NAME+" ( "+
        "_id integer primary key autoincrement ,"+
        ThingDbSchema.Cols.WHAT+", "+
        ThingDbSchema.Cols.WHERE+","+
        ThingDbSchema.Cols.PHOTO+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
