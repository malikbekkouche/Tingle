package com.example.malik.tingle;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Display extends SingleFragmentActivity {


@Override
    public Fragment createFragment(){
    return new DisplayFragment();
}



}
