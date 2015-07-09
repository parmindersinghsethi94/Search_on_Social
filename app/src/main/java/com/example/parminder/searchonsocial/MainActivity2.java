package com.example.parminder.searchonsocial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


public class MainActivity2 extends MainActivity {
    ViewPager viewPager=null;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        toolbar= (Toolbar) findViewById(R.id.app_br);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager fragmentManager=getSupportFragmentManager();
        viewPager.setAdapter(new MyAdapter(fragmentManager));
    }



}

class MyAdapter extends FragmentStatePagerAdapter{

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        if(i==0){
            fragment=new FragmentA();
        }
        if(i==1){
            fragment=new FragmentB();
        }if(i==2){
            fragment=new FragmentC();
        }
        if(i==3){
            fragment= new FragmentD();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position){
        if(position==0){
            return "InstaGram";
        }
        if(position==1){
            return "Facebook";
        }
        if(position==2){
            return "GitHub";
        }
        if(position==3){
            return "Images";
        }
        return null;
    }

}
