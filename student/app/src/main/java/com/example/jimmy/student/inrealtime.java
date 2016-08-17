package com.example.jimmy.student;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class inrealtime extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawerLayout;
    private int navItemId;
    private static final String NAV_ITEM_ID = "nav_index";
    Button b5, b6, b7, b8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inrealtime);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);
        b7 = (Button) findViewById(R.id.button7);
        b8 = (Button) findViewById(R.id.button8);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_2).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(inrealtime.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(inrealtime.this, list.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Toast.makeText(inrealtime.this, "已經在及時測驗內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_3:
                        break;
                    case R.id.navigation_item_4:
                        break;
                    case R.id.navigation_item_5:
                        break;
                    case R.id.navigation_item_6:
                        break;
                }
                return true;
            }
        });
        ////以下是更新帳戶名
        if(view.getHeaderCount() > 0) {
            View header = view.getHeaderView(0);
            Intent it=getIntent();
            TextView tv = (TextView) header.findViewById(R.id.textView2);
            TextView tv2 = (TextView) header.findViewById(R.id.name);
            connectuse x=(connectuse)inrealtime.this.getApplication();
            tv2.setText(x.accountname);
            tv.setText(x.email);
        }
///
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.openDrawer , R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super .onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super .onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        /////////////////////////////////


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inrealtime, menu);
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, navItemId);
    }
    @Override
    public void onClick(View v) {
        Toast toss = Toast.makeText(v.getContext(), "XXX", Toast.LENGTH_SHORT);
        switch (v.getId())
        {
            case R.id.button5:
                toss.setText("55555");
                toss.show();
                Intent it=new Intent(inrealtime.this,inpinreal.class);
                startActivity(it);
                break;
            case R.id.button6:
                toss.setText("66666");
                toss.show();
                break;
            case R.id.button7:
                toss.setText("77777");
                toss.show();
                break;
            case R.id.button8:
                toss.setText("8888");
                break;
        }
    }
}
