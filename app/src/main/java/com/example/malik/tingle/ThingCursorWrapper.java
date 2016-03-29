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

        Thing thing=new Thing(what,where);
        return thing;
    }
}
