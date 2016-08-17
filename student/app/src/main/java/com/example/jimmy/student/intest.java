package com.example.jimmy.student;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
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

public class intest extends AppCompatActivity {
    String accesspin;
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MyTestAdapter adapter;
    ////
    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    List<MyTestAdapter.DataHolder> forans = new ArrayList<>();
    ProgressBar pg;
    int TIME = 0;//30秒
    TextView time, timemin;
    ImageView img;
    ///
    Socket soc;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startanimate);
        connectuse con = (connectuse) intest.this.getApplication();
        soc = con.getSocket();
        Intent it = getIntent();
        accesspin = it.getStringExtra("accesspin");
        Log.e("!!!!!!!!!!!!!!!!!!!!!", "inin");
        settings = getSharedPreferences("studentuse_pref", 0);
        ////
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() //探测SQLite数据库操作
                .penaltyLog() //打印logcat
                .penaltyDeath()
                .build());
        ////
        animate();
        Log.e("!!", "222");

    }

    public void animate() {
        //剛開始先有動畫

        final ImageView image = (ImageView) findViewById(R.id.imageView);
        final Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.animate);
        final Animation anim1 = AnimationUtils.loadAnimation(this,
                R.anim.anim1);
        final Animation anims = AnimationUtils.loadAnimation(this,
                R.anim.start);
        anim1.setFillAfter(true);
        anims.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(anim1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        new CountDownTimer(4200, 1000) {
            int count = 0;

            @Override
            public void onFinish() {
                setContentView(R.layout.activity_intest);
                Log.e("!!", "111");
                next();
                forbar();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                switch (count) {
                    case 0:
                        image.setImageResource(R.drawable.three);
                        image.startAnimation(anim);
                        break;
                    case 1:
                        image.setImageResource(R.drawable.two);
                        image.startAnimation(anim);
                        break;
                    case 2:
                        image.setImageResource(R.drawable.one);
                        image.startAnimation(anim);
                        break;
                    case 3:
                        image.setImageResource(R.drawable.start);
                        image.startAnimation(anims);
                        break;
                    default:
                        break;
                }
                count++;
            }
        }.start();//動畫CODE到此
    }

    public void next() {
        Intent it = getIntent();
        accesspin = it.getStringExtra("accesspin");
        TIME = it.getExtras().getInt("sec") * 10;
        ////
        fab = (FloatingActionButton) findViewById(R.id.fab);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyTestAdapter(lists, forans, intest.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition + 1 == adapter.getItemCount()) {
                        //最后一个itemView的position为adapter中最后一个数据时,说明该itemView就是底部的view了
                        //需要注意position从0开始索引,adapter.getItemCount()是数据量总数
                        fab.setVisibility(View.VISIBLE);
                        Toast.makeText(intest.this, "滑动到底了", Toast.LENGTH_SHORT).show();
                    }
                    //同理检测是否为顶部itemView时,只需要判断其位置是否为0即可
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && firstItemPosition == 0) {
                        Toast.makeText(intest.this, "滑动到頂了", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

        });
        /////


        String result = DBConnector.executeQuery("SELECT question.question,question.A,question.B,question.C,question.D,question.ans FROM record,testlist,question,testinside WHERE record.pinforaccess='" + accesspin + "' AND record.pinfortest=testlist.KEYIN AND testlist.num=testinside.testtitleid and testinside.questionid=question.id");
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                MyTestAdapter.DataHolder d = new MyTestAdapter.DataHolder();
                d.ans = "X";
                forans.add(d);
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


    }

    public void forbar() {

        time = (TextView) findViewById(R.id.textView);
        timemin = (TextView) findViewById(R.id.textView8);
        pg = (ProgressBar) findViewById(R.id.progressbar);
        img = (ImageView) findViewById(R.id.imageView);
        pg.setMax(TIME);
        pg.incrementProgressBy(+TIME);
        if (TIME != 18000) {
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
                    img.setAnimation(am1);
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
                        img.setAnimation(am);
                        am.startNow();
                    }

                    if (pg.getProgress() % 600 == 0) {
                        timemin.setText(String.valueOf(pg.getProgress() / 600 - 1));
                    } else {
                        time.setText(String.valueOf((pg.getProgress() - pg.getProgress() / 600 * 600) / 10.));
                    }
                    pg.incrementProgressBy(-1);
                    setProgress(pg.getProgress() * 100);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("PRO", String.valueOf(TIME));
                    //pg.setMax(TIME);
                   // pg.incrementProgressBy(+TIME);
                    timemin.setText(String.valueOf(TIME / 600));
                    while (true) {
                        if (pg.getProgress() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    end();
                                }
                            });
                            break;
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

        }else {//如果unlimit
            TextView dot=(TextView)findViewById(R.id.textView9);
            dot.setVisibility(View.GONE);timemin.setVisibility(View.GONE);
            time.setText("unlimit");
        }

    }


    public void fabc(View v) {
        end();
    }

    public void end() {
        double grade = 0;
        for (int i = 0; i < lists.size(); i++) {
            if (forans.get(i).ans.equals(lists.get(i).get("ans"))) {
                grade += (100. / (lists.size()));
                Log.e(forans.get(i).ans.toString(), String.valueOf(((int) grade)));
            } else {
                String x = DBConnector.executeQuery("insert into ansrecord(testpfa,saccount,wrongqnum,answer) values('" + accesspin + "','" + settings.getString("account","XXX") + "','" + (i + 1) + "','" + forans.get(i).ans.toString() + "')");
            }
            Log.e(forans.get(i).toString(), lists.get(i).get("ans").toString());
        }
        String x = DBConnector.executeQuery("insert into grade(testpfa,saccount,grade) values('" + accesspin + "','" + settings.getString("account","XXX") + "','" + grade + "')");
        Toast.makeText(this, String.valueOf((int) grade) + settings.getString("account","XXX"), Toast.LENGTH_SHORT).show();
        try {
            soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent it = new Intent(intest.this, inrealtime.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   //確定按下退出鍵and防止重複按下退出鍵
            dia();
        }
        return false;
    }

    public void dia() {
        new AlertDialog.Builder(intest.this)
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
                        Intent it = new Intent(intest.this, inpinreal.class);
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
