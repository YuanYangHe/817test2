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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class inselftest extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    List<Integer> record = new ArrayList<>();
    TextView TQ, time, score;
    int total = 0, nowposition = 0;
    Button bA, bB, bC, bD;
    ImageView clock, yn, yn1, yn2;
    ProgressBar pg;
    Socket soc;
    int grade = 0, TIME = 120, chance = 1;
    Boolean iseasy = true, clickbreak = false, isend = false, isdoubleclick = false, isback = false;
    connectuse con;
    LinearLayout outside;
    double getClick, saveClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inselftest);
        Intent it = getIntent();
        Log.e("XXXX", it.getStringExtra("eorh"));
        if (it.getStringExtra("eorh").equals("easy")) {
            iseasy = true;
            TIME = 120;
            chance = 3;
        } else {
            iseasy = false;
            TIME = 80;
            chance = 1;
        }
        TQ = (TextView) findViewById(R.id.textView10);
        score = (TextView) findViewById(R.id.textView17);
        time = (TextView) findViewById(R.id.textView8);
        bA = (Button) findViewById(R.id.buttonA);
        bB = (Button) findViewById(R.id.buttonB);
        bC = (Button) findViewById(R.id.buttonC);
        bD = (Button) findViewById(R.id.buttonD);
        clock = (ImageView) findViewById(R.id.imageView);
        pg = (ProgressBar) findViewById(R.id.progressbar);
        bA.setOnClickListener(this);
        bB.setOnClickListener(this);
        bC.setOnClickListener(this);
        bD.setOnClickListener(this);
        outside = (LinearLayout) findViewById(R.id.outside);
        outside.setOnTouchListener(this);
        yn = (ImageView) findViewById(R.id.imageView6);
        yn1 = (ImageView) findViewById(R.id.imageView7);
        yn2 = (ImageView) findViewById(R.id.imageView8);
        con = (connectuse) inselftest.this.getApplication();
        soc = con.getSocket();
        ////
        String temp = DBConnector.executeQuery("SELECT count(*) FROM question");
        try {
            JSONArray jsonArray = new JSONArray(temp);
            JSONObject jsonData = jsonArray.getJSONObject(0);
            total = Integer.parseInt(jsonData.getString("count(*)"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getQ();
        init();
        forbar();
        Log.e("!!!!!!!!CH", String.valueOf(chance));
    }

    public void init() {
        pg.setProgress(TIME);
        clickbreak = false;
        TQ.setText(lists.get(nowposition % 5).get("Q").toString());
        bA.setText(lists.get(nowposition % 5).get("A").toString());
        bB.setText(lists.get(nowposition % 5).get("B").toString());
        bC.setText(lists.get(nowposition % 5).get("C").toString());
        bD.setText(lists.get(nowposition % 5).get("D").toString());
        nowposition++;
        Log.e("!!!!!!!!", record.toString());
        Log.e("!!!!!!!NP", String.valueOf(nowposition));
    }

    public void getQ() {
        lists.clear();
        int count = 0;
        while (count != 5) {
            String result = DBConnector.executeQuery("SELECT * FROM question ORDER BY RAND() LIMIT 0,5 ");//一次選5題 都過關會再更新
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    if (!record.contains(Integer.parseInt(jsonData.getString("id")))) {
                        map.put("Q", jsonData.getString("question"));
                        map.put("A", jsonData.getString("A"));
                        map.put("B", jsonData.getString("B"));
                        map.put("C", jsonData.getString("C"));
                        map.put("D", jsonData.getString("D"));
                        map.put("ans", jsonData.getString("ans"));
                        lists.add(map);
                        record.add(Integer.parseInt(jsonData.getString("id")));
                        count++;
                    }
                    if (count == 5) {
                        Log.e("!!!!!", "ASSSA");
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
        ImageView temp=yn;
        Boolean yesorno = false;
        if (!clickbreak) {
            switch (chance) {
                case 1:
                    temp = yn2;
                    break;
                case 2:
                    temp = yn1;
                    break;
                case 3:
                    temp = yn;
                    break;
                default:
                    break;
            }
            clickbreak = true;
            switch (v.getId()) {
                case R.id.buttonA:
                    if (lists.get((nowposition - 1) % 5).get("ans").toString().contains("A")) {
                        yesorno = true;
                    } else {
                        yesorno = false;
                    }
                    break;
                case R.id.buttonB:
                    if (lists.get((nowposition - 1) % 5).get("ans").toString().contains("B")) {
                        yesorno = true;
                    } else {
                        yesorno = false;
                    }
                    break;
                case R.id.buttonC:
                    if (lists.get((nowposition - 1) % 5).get("ans").toString().contains("C")) {
                        yesorno = true;
                    } else {
                        yesorno = false;
                    }
                    break;
                case R.id.buttonD:
                    if (lists.get((nowposition - 1) % 5).get("ans").toString().contains("D")) {
                        yesorno = true;
                    } else {
                        yesorno = false;
                    }
                    break;
            }
            if(yesorno)
            {
                grade += 100;
                temp.setImageResource(R.drawable.gogo);
            }else  {temp.setImageResource(R.drawable.nono); chance--;}
            score.setText(String.valueOf(grade));
        }

    }

    public void next() {
        if (chance != 0) {
            if (nowposition == total)//如果數到題庫上限
            {
                Toast.makeText(inselftest.this, "結束拉-1", Toast.LENGTH_SHORT).show();
                end();
            } else if (nowposition == record.size())/// /如果占存題目不夠用了
            {
                if (total - 5 > record.size()) {
                    getQ();
                    init();//若還有足夠5題，則更新list
                } else {
                    Toast.makeText(inselftest.this, "結束拉-2", Toast.LENGTH_SHORT).show();
                    end();
                }
            } else {
                init();
            }
        } else end();

    }


    public void forbar() {
        time = (TextView) findViewById(R.id.textView);
        pg = (ProgressBar) findViewById(R.id.progressbar);
        clock = (ImageView) findViewById(R.id.imageView);
        pg.setMax(TIME);
        pg.incrementProgressBy(+TIME);
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
                while (!isback) {

                    if (pg.getProgress() == 0) {//自動跑完(作答時間截止
                        if (!isend) {
                            Log.e("FROM AUTO", "XXX");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    next();
                                }
                            });
                        } else break;
                    }
                    if (clickbreak == true) {//手動(要重置記數
                        pg.setProgress(TIME);
                        if (isdoubleclick) {
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
    }

    public void end() {
        isend = true;
        SharedPreferences settings = getSharedPreferences("studentuse_pref", 0);
        Toast.makeText(this, settings.getString("account","XXX") + "測驗結束 分數:" + String.valueOf(grade), Toast.LENGTH_SHORT).show();
        Intent it = new Intent(inselftest.this, inrealtime.class);
        startActivity(it);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (clickbreak) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                saveClick = getClick;
                getClick = System.currentTimeMillis();
                if (getClick - saveClick < 1000) {
//按兩下事件
                    isdoubleclick = true;
                    next();
                    isdoubleclick = false;
                }
            }
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            dia();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void dia() {
        new AlertDialog.Builder(inselftest.this)
                .setTitle("警告")
                .setMessage("是否要離開測驗")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isback = true;
                        Intent it = new Intent(inselftest.this, inrealtime.class);
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



