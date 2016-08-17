package com.example.jimmy.student;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText acc, pwd;
    Button bt;
    Typeface typeFace;//設定字型
    String[] Textname = {"fonts/ARDESTINE.ttf","fonts/ARBERKLEY.ttf",
            "fonts/segoesc.ttf","fonts/segoescb.ttf","fonts/MTCORSVA.TTF",
            "fonts/BrushScriptStd.otf"};//設定字型
    public static final String KEY = "com.my.package.app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //===========================================================
        SharedPreferences settings = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        Utils.change_sTheme(settings.getInt("THEMES",0));
        connectuse.currentPosition = settings.getInt("THEMES",0);
        //===========================================================
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        typeFace = Typeface.createFromAsset(getAssets(),Textname[5]);   //設定字型
        tv = (TextView) findViewById(R.id.textView); //設定字型
        acc = (EditText) findViewById(R.id.editText);
        pwd = (EditText) findViewById(R.id.e2);
        bt = (Button) findViewById(R.id.button);
        bt.setTypeface(typeFace);   //設定字型
        tv.setTypeface(typeFace);   //設定字型


    }
    public void login(View v) {
        Log.e("123","qwqwq");
        Thread thread = new Thread() {
            Bundle bundle = new Bundle();
            Message msg = new Message();
            @Override
            public void run() {
                String stracc = acc.getText().toString();
                String strpwd = pwd.getText().toString();
                String result = DBConnector.executeQuery("select * from user where account='" + stracc + "' and pwd='" + strpwd + "' and TorS='S'");

                if (result.contains("null")) {
                    mhandler.obtainMessage(2).sendToTarget();
                } else if (result.contains("timed out")) {
                        mhandler.obtainMessage(3).sendToTarget();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        connectuse x=(connectuse)MainActivity.this.getApplicationContext();
                        x.accountname=jsonData.getString("account");
                        x.email=jsonData.getString("email");
                        mhandler.obtainMessage(1).sendToTarget();
                    } catch (Exception e) {
                        Log.e("log_tag", e.toString());
                    }
                }
                msg.setData(bundle);
                mhandler.sendMessage(msg);
            }
        };
        thread.start();
    }
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast tos=Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT);
            switch(msg.what)
            {
                case 1:
                    Intent it = new Intent(MainActivity.this, list.class);
                    startActivity(it);
                    break;
                case 2:
                    tos.setText("無此帳號!請重新輸入");
                    tos.show();
                    break;
                case 3:
                    tos.setText("連線逾時");
                    tos.show();
                    break;
            }
        }
    };
    public void register(View v) {
        Intent intentreg = new Intent(this, register.class);
        startActivity(intentreg);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
