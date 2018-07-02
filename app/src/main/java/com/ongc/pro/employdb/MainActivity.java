package com.ongc.pro.employdb;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.Menu;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    BottomBar mBottomBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomBar = BottomBar.attach(this , savedInstanceState);

        mBottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.Bottombaritemtwo)
                {

                    Favouritefragment f = new Favouritefragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                }
               else if (menuItemId == R.id.Bottombaritemone)
                {
                    Searchfragment f = new Searchfragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame , f).commit();
                }
                else if (menuItemId == R.id.Bottombaritemthree)
                {
                    Profilefragment f = new Profilefragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                }
                else if (menuItemId == R.id.Bottombaritemfour)
                {
                    infofragment f = new infofragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        mBottomBar.mapColorForTab(0 , "#2196F3");
        mBottomBar.mapColorForTab(1 , "#F44336");
        mBottomBar.mapColorForTab(2 , "#AB47BC");
        mBottomBar.mapColorForTab(3 , "#FF5722");

    }

}
