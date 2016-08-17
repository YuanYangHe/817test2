package com.example.jimmy.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ranking extends AppCompatActivity {
    int max = 0;
    int now = 0;
    BufferedReader brs;
    String pfa;
    List<adapterforrank.DataHolder> items = new ArrayList<>();
    ListView lv;
    private adapterforrank adt;
    Socket soc;
    connectuse con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Bundle bd = getIntent().getExtras();
        max = bd.getInt("max");
        now = bd.getInt("now");
        pfa = bd.getString("pfa");
        TextView maxtext = (TextView) findViewById(R.id.max);
        maxtext.setText(String.valueOf(max));
        TextView numtext = (TextView) findViewById(R.id.num);
        numtext.setText(String.valueOf(now));
        con = (connectuse) ranking.this.getApplication();
        brs = con.getread();
        soc = con.getSocket();
        listen ls = new listen();
        ls.start();
        /////
        lv = (ListView) findViewById(R.id.listView);
        getdata();
        adt = new adapterforrank(this, items);
        lv.setAdapter(adt);
    }

    class listen extends Thread {
        String temp;

        @Override
        public void run() {
            try {
                temp = brs.readLine();
                Log.e("!!!QQ", temp);
                if (temp.contains("end")) {
                    Log.e("!!!", "!!!!2~~");
                    handler.obtainMessage(2, temp).sendToTarget();
                } else if (temp.contains("@_@")) {
                    handler.obtainMessage(3, temp).sendToTarget();
                } else handler.obtainMessage(1, temp).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    finish();
                    break;
                case 2:
                    Toast.makeText(ranking.this, "TEST OVER", Toast.LENGTH_SHORT).show();
                    try {
                        soc.close();
                        Log.e("SOC", "CLOSE");
                        Intent it = new Intent(ranking.this, inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    Toast.makeText(ranking.this, "本次考試做廢", Toast.LENGTH_SHORT).show();
                    DBConnector.executeQuery("delete from buffer where testpfa='" + pfa + "'");
                    DBConnector.executeQuery("delete from record where  pinforaccess='" + pfa + "'");
                    try {
                        soc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent it = new Intent(ranking.this, inrealtime.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                    break;
            }
        }
    };


    public void getdata() {
        String result = DBConnector.executeQuery("select  saccount,grade  from buffer where testpfa='" + pfa + "' order by grade desc");
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                adapterforrank.DataHolder item = new adapterforrank.DataHolder();
                item.score = jsonData.getInt("grade");
                item.studentid = jsonData.getString("saccount");
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ranking, menu);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   //確定按下退出鍵and防止重複按下退出鍵
            dia();
        }
        return false;
    }

    public void dia() {
        new AlertDialog.Builder(ranking.this)
                .setTitle("警告")
                .setMessage("是否要離開測驗(離開測驗成績將不計算)")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBConnector.executeQuery("delete from buffer where testpfa='" + pfa + "' and saccount='" + con.accountname + "'");
                        try {
                            soc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent it = new Intent(ranking.this, inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }
}
