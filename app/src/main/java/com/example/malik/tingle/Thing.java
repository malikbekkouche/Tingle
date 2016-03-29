package com.example.malik.tingle;

import java.io.Serializable;

/**
 * Created by malik on 2/4/16.
 */
public class Thing  {

    private String mWhat=null;
    private String mWhere=null;

    public Thing(String what,String where){
        mWhat=what;
        mWhere=where;
    }

    @Override
    public String toString(){
        return oneLine("Item","is here :");
    }

    public String getmWhat() {
        return mWhat;
    }

    public String getmWhere() {
        return mWhere;
    }

    public void setmWhat(String mWhat) {
        this.mWhat = mWhat;
    }

    public void setmWhere(String mWhere) {
        this.mWhere = mWhere;
    }

    private String oneLine(String pre, String post) {
        return(pre+" "+mWhat+" "+post+" "+mWhere);
    }


}