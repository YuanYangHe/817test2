package com.example.jimmy.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class inpinreal extends AppCompatActivity {
    EditText access;
    private ProgressDialog pd;
    String accesspin;
    Socket soc;
    BufferedReader brs;
    BufferedWriter bws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inpinreal);
        access = (EditText) findViewById(R.id.pin);
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
                    try {
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
                                bws.write(connect.accountname + "\n" + "test\n");
                                bws.flush();
                                handler.obtainMessage(1).sendToTarget();
                                soc.setOOBInline(false);//忽略心跳封包
                                String temp = brs.readLine();
                                Log.e("IMP",temp);
                                if (temp.contains("starttest"))//收到開始測驗的信息
                                {
                                    handler.obtainMessage(2).sendToTarget();
                                } else if (temp.contains("15")) {
                                    handler.obtainMessage(3).sendToTarget();
                                } else if(temp.contains("@_@")){
                                    handler.obtainMessage(4).sendToTarget();
                                }else handler.obtainMessage(6).sendToTarget();
                            }
                        }else {handler.obtainMessage(6).sendToTarget();}
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
                    Intent it = new Intent(inpinreal.this, intest.class);//把抓到的測驗代碼傳過去
                    it.putExtra("accesspin", accesspin);
                    startActivity(it);
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
                        Log.e("因為XXX","SO BREAK SOCKET");
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
    public boolean onKeyDown(int keyCode,KeyEvent event){

        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){   //確定按下退出鍵and防止重複按下退出鍵
            try {
                Log.e("!!!!","SOCCLOSW");
                soc.close();
                finish();

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


}
