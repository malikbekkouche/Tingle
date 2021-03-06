package com.example.malik.tingle;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by malik on 4/9/16.
 */
public class SearchClass extends AsyncTask<String,Void,Thing> {
    private ThingsDB thingsDB;
    private Context mContext;

    SearchClass(ThingsDB db,Context context){
        super();
        thingsDB=db;
        mContext=context;
    }

    @Override
    protected Thing doInBackground(String... params) {
        //ThingsDB thingsDB=ThingsDB.get(mContext);
        Log.d("thread3", params[0]);
        Thing thing=thingsDB.get(params[0]);
        Log.d("thread2", thing.getmWhere());
        return thing;
    }

   @Override
    protected void onPostExecute(Thing thing){
       Log.d("thread", thing.getmWhere());

       Toast.makeText(mContext, thing.getmWhere(), Toast.LENGTH_SHORT).show();
   }
}
