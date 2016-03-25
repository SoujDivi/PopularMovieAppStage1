package com.example.soujanya.moiveapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.example.soujanya.moiveapp.fragment.MainFragment;
import com.example.soujanya.moiveapp.utils.AppPreferences;
import com.example.soujanya.moiveapp.utils.Constants;

public class MainActivity extends AppCompatActivity implements Constants {


    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private MenuItem selectedContextItem;
    public AppPreferences prefs;
    private String sortData = "";
    public MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.fragment_container, mainFragment).commit();


        prefs = new AppPreferences(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menu.addSubMenu(0, 0, Menu.NONE, R.string.sort_by_popular);
        menu.addSubMenu(0, 1, Menu.NONE, R.string.sort_by_rate);

        if (sortData.equals("")) {
            selectedContextItem = menu.findItem(0);
        } else if (sortData.equals(SORT_RATED)) {
            selectedContextItem = menu.findItem(1);
        }
        makeItemColored(selectedContextItem, Color.RED);

        return true;
    }

    public void makeItemColored(MenuItem item, int color) {
        if (item != null) {
            String title = item.getTitle().toString();
            Spannable newTitle = new SpannableString(title);
            newTitle.setSpan(new ForegroundColorSpan(color), 0, newTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(newTitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() != 2) {
            if (selectedContextItem != null) {
                if (selectedContextItem.getItemId() != item.getItemId()) {
                    makeItemColored(item, Color.RED);
                    makeItemColored(selectedContextItem, Color.BLACK);

                }
            } else {
                makeItemColored(item, Color.RED);

            }

            selectedContextItem = item;
        }


        if (id == R.id.action_settings) {
            return true;
        }

        switch (id) {
            case 0:
                sortData = "";
                prefs.addString(SORT_TYPE, "popular");
                mainFragment.doSort("popular");


                return true;
            case 1:
                sortData = SORT_RATED;
                prefs.addString(SORT_TYPE, "rating");
                mainFragment.doSort("rating");

                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
