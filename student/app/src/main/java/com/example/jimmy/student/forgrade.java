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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class forgrade extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ExpandableListView.OnChildClickListener {
    DrawerLayout drawerLayout;
    ExpandableListView lv;
    private forgradeadapter fgadt;
    final List<forgradeadapter.DataHolder> items = new ArrayList<>();
    private List<List<forgradeadapter.DataHolderchild>> iiitem = new ArrayList<>();
    Spinner sp;
    int forsp = 0;
    String account;
ImageView img;  connectuse x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgrade);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_3).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(forgrade.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(forgrade.this, inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Intent its = new Intent(forgrade.this, inpinreal.class);
                        its.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(its);
                        break;
                    case R.id.navigation_item_3:
                        Toast.makeText(forgrade.this, "已經在查詢成績內", Toast.LENGTH_SHORT).show();
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
           x = (connectuse) forgrade.this.getApplication();
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
        sp = (Spinner) findViewById(R.id.spinnerfg);
        sp.setOnItemSelectedListener(this); lv = (ExpandableListView) findViewById(R.id.expandableListView2);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(forgrade.this, forgrade1.class);
                it.putExtra("id", items.get(position).id);
                startActivity(it);
            }
        });
        lv.setOnChildClickListener(this);
    }

    private void getdata() {
        String result;
    Log.e("!!!~~","GETDD");
        if (forsp != 0) {
            result = DBConnector.executeQuery("select num,title,sort,editor from testlist  where testlist.sort='" + forsp + "' order by num desc");
        } else {
            result = DBConnector.executeQuery("select num,title,sort,editor from testlist order by num desc");//不分類
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                forgradeadapter.DataHolder item = new forgradeadapter.DataHolder();
                item.title = jsonData.getString("title");
                item.sort = jsonData.getInt("sort");//1~7 7是不分類的測驗
                item.id = jsonData.getString("num");
                items.add(item);
                ////
                String x = DBConnector.executeQuery("SELECT record.pinforaccess,record.pinfortest,grade.grade FROM testlist,record,grade where testlist.KEYIN=record.pinfortest and testlist.num='" + jsonData.getString("num") + "'  and grade.saccount='" + account + "' and grade.testpfa=record.pinforaccess");
                if (!x.contains("null")) {
                    JSONArray jsonArray1 = new JSONArray(x);
                    List<forgradeadapter.DataHolderchild> s = new ArrayList<>();
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonData1 = jsonArray1.getJSONObject(j);
                        forgradeadapter.DataHolderchild ss = new forgradeadapter.DataHolderchild();
                        ss.pfa = jsonData1.getString("pinforaccess");
                        ss.pft = jsonData1.getString("pinfortest");
                        ss.grade = jsonData1.getString("grade");
                        s.add(ss);
                    }
                    iiitem.add(s);
                } else {
                    List<forgradeadapter.DataHolderchild> s = new ArrayList<>();
                    forgradeadapter.DataHolderchild ss = new forgradeadapter.DataHolderchild();
                    ss.pfa = null;
                    s.add(ss);
                    iiitem.add(s);
                }

                ////
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgrade, menu);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        forsp = position;
        renew();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

  /*  @Override
    public void onResume() {
        super.onResume();
        renew();
    }*/

    public void renew() {//刷新ADAPTER
        items.clear();
        getdata();
        fgadt = new forgradeadapter(this, items, iiitem);
        lv.setAdapter(fgadt);
        fgadt.notifyDataSetChanged();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (iiitem.get(groupPosition).get(childPosition).pfa != null) {
            Intent it = new Intent(forgrade.this, forgrade1.class);
            it.putExtra("pfa", iiitem.get(groupPosition).get(childPosition).pfa);
            it.putExtra("id", items.get(groupPosition).id);
            startActivity(it);
        }

        return true;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   //確定按下退出鍵and防止重複按下退出鍵
            Intent it = new Intent(forgrade.this, inrealtime.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        }
        return false;
    }
    public void headclick(View v) {
        drawerLayout.closeDrawers();
        Intent it = new Intent(forgrade.this, fixhead.class);
        startActivityForResult(it, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectuse x = (connectuse) forgrade.this.getApplication();
                    img.setImageBitmap(x.b);
                }
            });
        }
    }

}
