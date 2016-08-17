package com.example.jimmy.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class inpinreal extends AppCompatActivity {
    EditText access;
    private ProgressDialog pd;
    String accesspin,account;
    Socket soc;
    BufferedReader brs;
    BufferedWriter bws;
    DrawerLayout drawerLayout;
    Boolean ispressed = false;//判斷有沒有連過SOC 這樣在斷掉得時後才知道要不要切掉
ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inpinreal);
        access = (EditText) findViewById(R.id.pin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ///
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_2).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(inpinreal.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(inpinreal.this, inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Toast.makeText(inpinreal.this, "已經在即時測驗內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_3:
                        Intent its = new Intent(inpinreal.this, forgrade.class);
                        its.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(its);
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
            connectuse x = (connectuse) inpinreal.this.getApplication();
            SharedPreferences settings = getSharedPreferences("studentuse_pref", 0);
            tv2.setText(settings.getString("email","XXX"));
            tv.setText(account=settings.getString("account","XXX"));
            img=(ImageView)header.findViewById(R.id.profile_image);
            img.setImageBitmap(x.b);
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
        /////////////////////////////////
    }

    public void connects(View v) {
        accesspin = access.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectuse connect = (connectuse) inpinreal.this.getApplication();
                connect.init();
                soc = connect.getSocket();
                brs = connect.getread();
                bws = connect.getwrite();
                if (soc.isConnected()) {
                    ispressed = true;
                    try {
                        bws.write("test\n");
                        bws.flush();
                        bws.write(accesspin.toUpperCase() + "\n");
                        bws.flush();
                        if (brs.readLine().contains("hr"))// 在SOC房間
                        {
                            String x = DBConnector.executeQuery("select * from record where pinforaccess='" + accesspin.toUpperCase() + "'");
                            if (!x.contains("null")) {
                                bws.write("fail\n");
                                bws.flush();
                                handler.obtainMessage(6).sendToTarget();
                            } else {
                                bws.write("ok\n");
                                bws.write(account + "\n");
                                bws.flush();
                                handler.obtainMessage(1).sendToTarget();
                                soc.setOOBInline(false);//忽略心跳封包
                                String temp = brs.readLine();
                                Log.e("IMP", temp);
                                if (temp.contains("starttest"))//收到開始測驗的信息
                                {
                                    int types = Integer.parseInt(brs.readLine());
                                    int times = Integer.parseInt(brs.readLine());//second
                                    handler.obtainMessage(2, types, times).sendToTarget();
                                } else if (temp.contains("startrace")) {
                                    handler.obtainMessage(3).sendToTarget();
                                } else if (temp.contains("@_@")) {
                                    handler.obtainMessage(4).sendToTarget();
                                } else handler.obtainMessage(6).sendToTarget();
                            }
                        } else {
                            handler.obtainMessage(6).sendToTarget();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("out", "out");
                    }
                } else {
                    handler.obtainMessage(5).sendToTarget();
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    pd = ProgressDialog.show(inpinreal.this, "标题", "連接中，请稍后……", true);
                    break;
                case 2:
                    pd.cancel();
                    Log.e(String.valueOf(msg.arg1), String.valueOf(msg.arg2));
                    if (msg.arg1 == 3) {
                        Intent it = new Intent(inpinreal.this, intest.class);//把抓到的測驗代碼傳過去
                        it.putExtra("accesspin", accesspin);
                        it.putExtra("sec", msg.arg2);
                        startActivity(it);
                    } else {//msg.arg1==2
                        Intent it = new Intent(inpinreal.this, intestanother.class);//把抓到的測驗代碼傳過去
                        it.putExtra("accesspin", accesspin);
                        it.putExtra("sec", msg.arg2);
                        startActivity(it);
                    }

                    break;
                case 3:
                    pd.cancel();
                    Intent its = new Intent(inpinreal.this, inrace.class);//把抓到的測驗代碼傳過去
                    its.putExtra("accesspin", accesspin);
                    startActivity(its);
                    break;
                case 4://等帶中老師段線的狀況
                    pd.cancel();
                    Toast.makeText(inpinreal.this, "老師端網路異常　或伺服器問題", Toast.LENGTH_SHORT).show();
                    try {
                        soc.close();
                        Log.e("因為XXX", "SO BREAK SOCKET");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    Toast.makeText(inpinreal.this, "伺服器未開啟，或網路異常", Toast.LENGTH_SHORT).show();
                    try {
                        soc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6://輸入錯誤的狀況
                    Toast.makeText(inpinreal.this, "PIN碼不正確，或測驗未開放", Toast.LENGTH_SHORT).show();
                    try {
                        soc.close();
                        Log.e("因為XXX", "SO BREAK SOCKET");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public  void back(View v )
    {

        Intent it = new Intent(inpinreal.this, inrealtime.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   //確定按下退出鍵and防止重複按下退出鍵
            try {
                Log.e("!!!!", "SOCCLOSW");
                if (ispressed) {
                    soc.close();
                }
                Intent it = new Intent(inpinreal.this, inrealtime.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inpinreal, menu);
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
    public void headclick(View v) {
        drawerLayout.closeDrawers();
        Intent it = new Intent(inpinreal.this, fixhead.class);
        startActivityForResult(it, 0);
    }
    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectuse x = (connectuse) inpinreal.this.getApplication();
                    img.setImageBitmap(x.b);
                }
            });
        }
    }

}
