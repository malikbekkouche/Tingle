package com.example.malik.tingle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by malik on 3/8/16.
 */
public class PagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private static ThingsDB allThings;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        mViewPager=new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        allThings=ThingsDB.get(this);

        FragmentManager fm=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return DisplayFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return allThings.size();
            }
        });

        int position=getIntent().getIntExtra("position",0);
        mViewPager.setCurrentItem(position);


    }
}
