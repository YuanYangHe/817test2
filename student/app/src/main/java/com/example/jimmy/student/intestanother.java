package com.example.jimmy.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class intestanother extends AppCompatActivity implements View.OnClickListener {
    TextView TQ, time, nowpage, maxpage;
    Button bA, bB, bC, bD;
    ImageView clock;
    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    String accesspin;
    int nowposition = 0;
    List<String> stuans = new ArrayList<>();
    ProgressBar pg;
    int TIME = 30;
    Boolean clickbreak = false;
    Socket soc;
    Boolean isend = false;
    SharedPreferences settings;
    ///間距時間
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intestanother);
        settings = getSharedPreferences("studentuse_pref", 0);
        TQ = (TextView) findViewById(R.id.textView10);
        time = (TextView) findViewById(R.id.textView8);
        nowpage = (TextView) findViewById(R.id.textView11);
        maxpage = (TextView) findViewById(R.id.textView13);
        bA = (Button) findViewById(R.id.button13);
        bB = (Button) findViewById(R.id.button14);
        bC = (Button) findViewById(R.id.button15);
        bD = (Button) findViewById(R.id.button16);
        clock = (ImageView) findViewById(R.id.imageView);
        pg = (ProgressBar) findViewById(R.id.progressbar);
        bA.setOnClickListener(this);
        bB.setOnClickListener(this);
        bC.setOnClickListener(this);
        bD.setOnClickListener(this);
        connectuse con = (connectuse) intestanother.this.getApplication();
        soc = con.getSocket();
        ////
        Intent it = getIntent();
        accesspin = it.getStringExtra("accesspin");
        TIME = it.getExtras().getInt("sec") * 10;
        String result = DBConnector.executeQuery("SELECT question.question,question.A,question.B,question.C,question.D,question.ans FROM record,testlist,question,testinside WHERE record.pinforaccess='" + accesspin + "' AND record.pinfortest=testlist.KEYIN AND testlist.num=testinside.testtitleid and testinside.questionid=question.id");
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonData = jsonArray.getJSONObject(i);
                map.put("Q", jsonData.getString("question"));
                map.put("A", jsonData.getString("A"));
                map.put("B", jsonData.getString("B"));
                map.put("C", jsonData.getString("C"));
                map.put("D", jsonData.getString("D"));
                map.put("ans", jsonData.getString("ans"));
                lists.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ///
        maxpage.setText(String.valueOf(lists.size()));
        init();
        forbar();
    }

    public void init() {
        pg.setProgress(TIME);
        clickbreak = false;
        TQ.setText(lists.get(nowposition).get("Q").toString());
        bA.setText(lists.get(nowposition).get("A").toString());
        bB.setText(lists.get(nowposition).get("B").toString());
        bC.setText(lists.get(nowposition).get("C").toString());
        bD.setText(lists.get(nowposition).get("D").toString());
        nowposition++;
        nowpage.setText(String.valueOf(nowposition));
    }

    @Override
    public void onClick(View v) {
        clickbreak = true;
        switch (v.getId()) {
            case R.id.button13:
                stuans.add(nowposition - 1, "A");
                break;
            case R.id.button14:
                stuans.add(nowposition - 1, "B");
                break;
            case R.id.button15:
                stuans.add(nowposition - 1, "C");
                break;
            case R.id.button16:
                stuans.add(nowposition - 1, "D");
                break;
        }
       next();
    }
    public  void next()
    {
        if (nowposition < lists.size()) {
            Log.e("!NOW", String.valueOf(nowposition));
            Log.e("MAX", String.valueOf(lists.size()));

            init();
        } else {
            Log.e("!!!!", stuans.toString());
            Log.e("FROM CLICK", "XXX");
            end();
        }
    }

    public void forbar() {
        time = (TextView) findViewById(R.id.textView);
        pg = (ProgressBar) findViewById(R.id.progressbar);
        clock = (ImageView) findViewById(R.id.imageView);
        pg.setMax(TIME);
        pg.incrementProgressBy(+TIME);
        if (TIME != 310) {
            final Animation am = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            am.setDuration(2500);
            am.setFillAfter(true);
            final Animation am1 = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            am1.setDuration(2500);
            am1.setFillAfter(true);
            am.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clock.setAnimation(am1);
                    am1.startNow();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            setProgressBarVisibility(true);
            final Handler h = new Handler();
            final Runnable callback1 = new Runnable() {
                @Override
                public void run() {
                    if (pg.getProgress() % 50 == 0) {
                        clock.setAnimation(am);
                        am.startNow();
                    }
                    time.setText(String.valueOf((pg.getProgress() - pg.getProgress() / 600 * 600) / 10.));
                    pg.incrementProgressBy(-1);
                    setProgress(pg.getProgress() * 100);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("PRO", String.valueOf(TIME));

                    while (true) {
                        if (pg.getProgress() == 0) {//自動跑完(作答時間截止
                            Log.e("FROM AUTO", "XXX");
                            if (!isend) {
                                stuans.add(nowposition - 1, "X");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       next();
                                    }
                                });
                            } else  break;
                        }
                        if ((clickbreak == true)) {//手動(要停止記數
                            if (nowposition != lists.size()) {
                                pg.setProgress(TIME);
                                clickbreak = false;
                            }
                        }
                        try {
                            h.post(callback1);
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {//如果unlimit
            time.setText("umlimit");
        }
    }

    public void end() {
        isend = true;
        double grade = 0;
        connectuse con = (connectuse) intestanother.this.getApplication();
        for (int i = 0; i < lists.size(); i++) {
            if (stuans.get(i).equals(lists.get(i).get("ans"))) {
                grade += (100. / (lists.size()));
                Log.e(stuans.get(i), String.valueOf(((int) grade)));
            } else {
                String x = DBConnector.executeQuery("insert into ansrecord(testpfa,saccount,wrongqnum,answer) values('" + accesspin + "','" + settings.getString("account","XXX") + "','" + (i + 1) + "','" + stuans.get(i) + "')");
            }
            Log.e(stuans.get(i), lists.get(i).get("ans").toString());
        }
        String x = DBConnector.executeQuery("insert into grade(testpfa,saccount,grade) values('" + accesspin + "','" + settings.getString("account","XXX") + "','" + grade + "')");
        Toast.makeText(this, settings.getString("account","XXX") + "測驗結束 分數:" + String.valueOf((int) grade), Toast.LENGTH_SHORT).show();
        try {
            soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent it = new Intent(intestanother.this, inrealtime.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intestanother, menu);
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
        new AlertDialog.Builder(intestanother.this)
                .setTitle("警告")
                .setMessage("是否要離開測驗(將會使此次測驗失效)")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBConnector.executeQuery("delete from buffer where testpfa='" + accesspin + "'");
                        DBConnector.executeQuery("delete from record where pinforaccess='" + accesspin + "'");
                        try {
                            soc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent it = new Intent(intestanother.this, inpinreal.class);
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
