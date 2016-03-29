package com.example.malik.tingle;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TingleActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tingle);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.tingle_fragment);
        if(fragment==null){
         fragment=new TingleFragment();
            fm.beginTransaction().add(R.id.tingle_fragment,fragment).commit();
        }
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            Fragment fragment2=fm.findFragmentById(R.id.list_tingle);
            if(fragment2==null){
                fragment2=new ListFragment();
                fm.beginTransaction().add(R.id.list_tingle,fragment2).commit();
            }
        }


    }


}
