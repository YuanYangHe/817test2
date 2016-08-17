package com.example.jimmy.student;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class list extends AppCompatActivity {
    DrawerLayout drawerLayout;

    NavigationView view;
    Typeface typeFace;//設定字型
    String[] Textname = {"fonts/ARDESTINE.ttf","fonts/ARBERKLEY.ttf",
            "fonts/segoesc.ttf","fonts/segoescb.ttf","fonts/MTCORSVA.TTF",
            "fonts/BrushScriptStd.otf"};//設定字型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ///
        view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_1).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Toast.makeText(list.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Toast.makeText(list.this, "已經在主選單內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_2:
                        Intent it = new Intent(list.this, inrealtime.class);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_3:
                        break;
                    case R.id.navigation_item_4:
                        break;
                    case R.id.navigation_item_5:
                        break;
                    case R.id.navigation_item_6:
                        Intent it6 = new Intent(list.this, settings.class);
                        startActivity(it6);
                        break;
                }
                return true;
            }
        });
        ////以下是更新帳戶名s
        if (view.getHeaderCount() > 0) {
            View header = view.getHeaderView(0);
            TextView tv = (TextView) header.findViewById(R.id.textView2);
            TextView tv2 = (TextView) header.findViewById(R.id.name);
            connectuse x=(connectuse)list.this.getApplication();
            tv2.setText(x.accountname);
            tv.setText(x.email);

        }
        ///
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    protected void onResume() {
        super.onResume();
        view.getMenu().findItem(R.id.navigation_item_1).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
