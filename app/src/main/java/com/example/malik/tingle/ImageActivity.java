package com.example.malik.tingle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class ImageActivity extends AppCompatActivity {

    private File mFile;
    private ImageView imageView;
    private static final String EXTRA="myFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String path=getIntent().getStringExtra(EXTRA);
        imageView=(ImageView) findViewById(R.id.big_photo);

        String s=createPath(path);

        File dir=getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mFile=new File(dir,path);
        Bitmap bitmap= BitmapMagic.getScaledBitmap(s,this);
        imageView.setImageBitmap(bitmap);
    }

    public String createPath(String path){
        String[] str=path.split("/");
        String ss="BIG"+str[str.length-1];
        str[str.length-1]=ss;
        String s="";
        for(int i=0;i<str.length;i++)
            s+="/"+str[i];
        return s;
    }



}
