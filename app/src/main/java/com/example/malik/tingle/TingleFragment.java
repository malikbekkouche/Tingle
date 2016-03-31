package com.example.malik.tingle;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by malik on 2/18/16.
 */
public class TingleFragment extends Fragment{

    private static ThingsDB thingsDB;
    private static final String KEY="f6957104c74fdf82e23499b5ad1f82c1";

    private Button addThing,searchThing,mShowAll,mCamera;
    private TextView lastAdded;
    private TextView newWhat,newWhere;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thingsDB=ThingsDB.get(this.getContext());
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tingle, parent, false);

        lastAdded=(TextView) v.findViewById(R.id.last_thing);
        // updateUI();


        searchThing=(Button) v.findViewById(R.id.search);
        addThing=(Button) v.findViewById(R.id.add_button);

        newWhat=(TextView) v.findViewById(R.id.what_text);
        newWhere=(TextView) v.findViewById(R.id.where_text);


        // version=(TextView) findViewById(R.id.version);
        // version.setText(Build.VERSION.CODENAME);

        mCamera=(Button)v.findViewById(R.id.barcode);
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                startActivityForResult(intent,0);
            }
        });

        addThing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thingsDB.addThing(new Thing(newWhat.getText().toString(), newWhere.getText().toString()));
                newWhat.setText("");
                newWhere.setText("");
                updateUI();
                FragmentManager fm=getActivity().getSupportFragmentManager();
                ListFragment fragment_list=(ListFragment)fm.findFragmentById(R.id.list_tingle);
                if(fragment_list!=null)
                    fragment_list.updateUI();
            }
        });


        searchThing.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String thing=newWhat.getText().toString();
                Thing t=searchMyThing(thing);
                Toast.makeText(TingleFragment.this.getContext(), t.getmWhere(), Toast.LENGTH_SHORT).show();


            }
        });

        if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE) {
            mShowAll = (Button) v.findViewById(R.id.list_all);
            mShowAll.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(TingleFragment.this.getContext(), ListActivity.class);
                    ArrayList<String> allThings = copyToString((ArrayList<Thing>) thingsDB.getThingsDB());
                    intent.putExtra("all", allThings);
                    startActivity(intent);
                }
            });
        }

        return v;
    }

    private void updateUI() {
        int s=thingsDB.size();
        if(s>0)
            lastAdded.setText(thingsDB.get(s-1).toString());
    }


    private Thing searchMyThing(String s){

        String where="";
        for (int i=0;i<thingsDB.size();i++){
            if(thingsDB.get(i).getmWhat().equals(s))
                where=thingsDB.get(i).getmWhere();
        }
        if (where.equals(""))
            where="object not found";
        return new Thing(s,where);
    }

    private ArrayList<String> copyToString(ArrayList<Thing> thingsDB){
        ArrayList<String> allThings=new ArrayList<String>();
        for(int i=0;i<thingsDB.size();i++)
            allThings.add(thingsDB.get(i).toString());
        return allThings;
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        String contents = intent.getStringExtra("SCAN_RESULT");
        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
        //Toast.makeText(this.getContext(),contents,Toast.LENGTH_SHORT).show();
        new FetchOutpanTask().execute(contents);
    }

    public class CodeBarConnectivity {

        public String getProduct(String url) {
            ConnectivityManager conMan = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                   return getUrlString(url);
                }catch(IOException e){

                }
            }
            return null;
        }

        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }
        public String getUrlString(String urlSpec) throws IOException {
            return new String(getUrlBytes(urlSpec));
        }
    }

    public class FetchOutpanTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
           return new CodeBarConnectivity().getProduct("https://api.outpan.com/v2/products/"+params[0]+"/?"+KEY);
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();

        }
    }

}
