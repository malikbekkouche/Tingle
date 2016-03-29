package com.example.malik.tingle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malik on 2/11/16.
 */
public class ThingsDB {
    private static ThingsDB sThingsDB;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static ThingsDB get(Context context){
    if(sThingsDB==null)
        sThingsDB=new ThingsDB(context.getApplicationContext());
    return sThingsDB;
    }
    public ArrayList<Thing> getThingsDB(){
        ArrayList<Thing> things=new ArrayList<Thing>();

        ThingCursorWrapper thingCursorWrapper=queryThings(null,null);
        try{
            thingCursorWrapper.moveToFirst();
            while(!thingCursorWrapper.isAfterLast()){
                things.add(thingCursorWrapper.getThing());
                thingCursorWrapper.moveToNext();
            }
        }finally {
            thingCursorWrapper.close();
        }
        return things;
    }

    public void addThing(Thing thing){
        ContentValues values=getValues(thing);
        mDatabase.insert(ThingDbSchema.ThingTable.NAME,null,values);
    }
    public int size(){
        ThingCursorWrapper thingCursorWrapper=queryThings(null,null);
        int size=0;
        try{
            thingCursorWrapper.moveToFirst();
            while(!thingCursorWrapper.isAfterLast()){
                thingCursorWrapper.moveToNext();
                size++;
            }
        }finally {
            thingCursorWrapper.close();
        }
        return size;
    }
    public Thing get(int i){
        ThingCursorWrapper thingCursorWrapper=queryThings(null,null);
        Thing thing= new Thing(null,null);
        try {
            thingCursorWrapper.moveToPosition(i);
            thing=thingCursorWrapper.getThing();
        }finally {
            thingCursorWrapper.close();
        }
        return thing;
    }

    public void delete(int p){
        mDatabase.execSQL("DELETE FROM "+ThingDbSchema.ThingTable.NAME+" WHERE _id in (SELECT _id FROM "+ThingDbSchema.ThingTable.NAME+" LIMIT 1 OFFSET "+p+")");
    }

    private ThingsDB(Context context){
        mContext=context.getApplicationContext();
        mDatabase=new ThingBaseHelper(mContext).getWritableDatabase();

    }

    public static ContentValues getValues(Thing thing){
        ContentValues contentValues=new ContentValues();
        contentValues.put(ThingDbSchema.Cols.WHAT,thing.getmWhat());
        contentValues.put(ThingDbSchema.Cols.WHERE,thing.getmWhere());
        return contentValues;
    }

    private ThingCursorWrapper queryThings(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ThingDbSchema.ThingTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new ThingCursorWrapper(cursor);
    }


}
