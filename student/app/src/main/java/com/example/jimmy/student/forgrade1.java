package com.example.jimmy.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class forgrade1 extends AppCompatActivity {
    private List<List<String>> iiitem = new ArrayList<>();
    private List<forgradeadapter1.DataHolder> items = new ArrayList<>();
    DrawerLayout drawerLayout;
    String id, pfa;
    String account;
    ImageView img;connectuse x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgrade1);
        //////////////////////////////////////
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_3).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(forgrade1.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(forgrade1.this, inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Intent its = new Intent(forgrade1.this, inpinreal.class);
                        its.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(its);
                        break;
                    case R.id.navigation_item_3:
                        Toast.makeText(forgrade1.this, "已經在查詢成績內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_4:
                        break;
                    case R.id.navigation_item_5:
                        break;

                }
                return true;
            }
        });
        ////以下是更新帳戶名
        if (view.getHeaderCount() > 0) {
            View header = view.getHeaderView(0);
            TextView tv = (TextView) header.findViewById(R.id.textView2);
            TextView tv2 = (TextView) header.findViewById(R.id.name);
            x = (connectuse) forgrade1.this.getApplication();
            SharedPreferences settings = getSharedPreferences("studentuse_pref", 0);
            tv2.setText(settings.getString("email","XXX"));
            tv.setText(account=settings.getString("account","XXX"));
            img=(ImageView)header.findViewById(R.id.profile_image);
            img.setImageBitmap(x.b);
        }
//////
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
//////////////
        //////////////////////////////////////
        Intent it = getIntent();
        pfa = it.getStringExtra("pfa");
        id = it.getStringExtra("id");
        getdata();
        ExpandableListView myExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        myExpandableListView.setAdapter(new forgradeadapter1(this, items, iiitem));

    }

    public void getdata() {
        items = new ArrayList<>();

        String result = DBConnector.executeQuery("select * from question,testinside where testinside.questionid=question.id  and testinside.testtitleid='" + id + "'");//不分類
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                forgradeadapter1.DataHolder s = new forgradeadapter1.DataHolder();
                s.sort = jsonData.getInt("sort");
                s.title = jsonData.getString("question");
                s.ans = jsonData.getString("ans");
                try {
                    String temp = DBConnector.executeQuery("select answer from ansrecord where saccount='" + account + "' and testpfa='" + pfa + "' and wrongqnum='" + (i + 1) + "'");
                    if (temp != null) {
                        JSONArray jsonArray1 = new JSONArray(temp);
                        JSONObject jsonData1 = jsonArray1.getJSONObject(0);
                        s.sans = jsonData1.getString("answer");
                    }
                } catch (org.json.JSONException xxx) { s.sans = "null";
                }

                items.add(s);
                List<String> ss = new ArrayList<>();
                ss.add(jsonData.getString("A"));
                ss.add(jsonData.getString("B"));
                ss.add(jsonData.getString("C"));
                ss.add(jsonData.getString("D"));

                iiitem.add(ss);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgrade1, menu);
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
