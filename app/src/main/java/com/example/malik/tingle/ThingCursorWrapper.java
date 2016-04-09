package com.example.malik.tingle;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by malik on 3/14/16.
 */
public class ThingCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ThingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Thing getThing(){
        String what=getString(getColumnIndex(ThingDbSchema.Cols.WHAT));
        String where=getString(getColumnIndex(ThingDbSchema.Cols.WHERE));
        String photo=getString(getColumnIndex(ThingDbSchema.Cols.PHOTO));

        Thing thing=new Thing(what,where);
        thing.setPhoto(photo);
        return thing;
    }

    public Thing getThingWithID(){
        String what=getString(getColumnIndex(ThingDbSchema.Cols.WHAT));
        String where=getString(getColumnIndex(ThingDbSchema.Cols.WHERE));
        int id=getInt(getColumnIndex("_id"));

        Thing thing=new Thing(what,where);
        thing.setID(id);

        return thing;
    }
}
