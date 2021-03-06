package com.example.malik.tingle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by malik on 2/18/16.
 */
public class TingleFragment extends Fragment{

    private static ThingsDB thingsDB;
    private static final String KEY="f6957104c74fdf82e23499b5ad1f82c1";
    private static final String INTERNET="no internet connection";
    private final int REQUEST_SCAN=1;
    private final int REQUEST_PHOTO=2;

    private Button addThing,searchThing,mShowAll,mCamera;
    private TextView lastAdded;
    private TextView newWhat,newWhere;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private Thing thing;
    private File mFile;
    public static String BIG="BIG";


    @Override
    public void onResume(){
        super.onResume();
        updateUI(thing);
        Log.d("azer","resume");
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thingsDB=ThingsDB.get(this.getContext());
        prepare();
        mFile=thingsDB.getPhotoFile(thing);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tingle, parent, false);

        lastAdded=(TextView) v.findViewById(R.id.last_thing);
        //updatePhoto();


        searchThing=(Button) v.findViewById(R.id.search);
        addThing=(Button) v.findViewById(R.id.add_button);

        newWhat=(TextView) v.findViewById(R.id.what_text);
        newWhere=(TextView) v.findViewById(R.id.where_text);



        mCamera=(Button)v.findViewById(R.id.barcode);
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                startActivityForResult(intent,REQUEST_SCAN);
            }
        });

        addThing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thing = new Thing(newWhat.getText().toString(), newWhere.getText().toString());
                thing.setPhoto(mFile.getPath());
                thingsDB.addThing(thing);

                newWhat.setText("");
                newWhere.setText("");
                prepare();
                updateUI(thing);
                Log.d("added", thing.toString());
                mFile = thingsDB.getPhotoFile(thing);
                usePhoto();
                mPhotoView.setImageResource(android.R.color.transparent);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ListFragment fragment_list = (ListFragment) fm.findFragmentById(R.id.list_tingle);
                if (fragment_list != null)
                    fragment_list.updateUI();
            }
        });


        searchThing.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String thing=newWhat.getText().toString();
                SearchClass sc=new SearchClass(thingsDB,getContext());
                sc.execute(thing);



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
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPhotoView = (ImageView) v.findViewById(R.id.tingle_photo);
            mPhotoButton = (ImageButton) v.findViewById(R.id.use_camera);
            usePhoto();

        }






        return v;
    }

    private void usePhoto(){
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mFile != null;//&& intent.resolveActivity(pm)
        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mFile);
            Log.d("uri",mFile.getName());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });


    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public void copy2(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }



    private void updatePhoto(){
        /*
        if(mFile==null || !mFile.exists()){
            mPhotoView.setImageDrawable(null);
            Log.d("here", "without you");}
        else{*/
        File dir=getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file=new File(dir,BIG+mFile.getName());
            if(file.exists())
                Log.d("ee","dadadada");
            try {
                copy(mFile, file);
                Log.d(mFile.getName(),file.getName());
            }catch(IOException e){
                Log.d("IOEXCEPTION",file.getPath());
            }
            Bitmap bitmap=BitmapMagic.getScaledBitmap(mFile.getPath(),getActivity());
            Log.d("gege",mFile.getName());
            mPhotoView.setImageBitmap(bitmap);


    }

    private void updateUI(Thing thing) {
        if(thing!=null)
            lastAdded.setText(thing.toString()+thing.getID());
        else
            lastAdded.setText("Empty database");
    }

    private void prepare(){
        int s=thingsDB.size();
        if(s>0)
            thing=thingsDB.getLast();

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
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode==REQUEST_SCAN){
        String contents = intent.getStringExtra("SCAN_RESULT");
        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

        new FetchOutpanTask().execute(contents);
        }else if(requestCode==REQUEST_PHOTO){
            Log.d("here",mFile.getName());
            updatePhoto();
        }
    }

    public class CodeBarConnectivity {

        public String getProduct(String url) {
            ConnectivityManager conMan = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    String result= getUrlString(url);
                    JSONObject json=new JSONObject(result);
                    return parse(json);
                }catch(IOException e){

                }catch(JSONException ee){

                }
            }else{
                return INTERNET;
            }
            return null;
        }

        public String parse(JSONObject json)throws JSONException{
           return json.getString("name");
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
            Log.d("ici",params[0]);
           return new CodeBarConnectivity().getProduct("https://api.outpan.com/v2/products/"+params[0]+"/?apikey="+KEY);
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
           // Log.d("ici", str);
            if(!str.equals(INTERNET))
            newWhat.setText(str);

        }
    }

}
