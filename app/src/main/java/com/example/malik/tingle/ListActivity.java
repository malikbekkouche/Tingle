package com.example.malik.tingle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment(){
        return new ListFragment();
    }
}
