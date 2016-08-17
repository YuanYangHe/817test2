package com.example.jimmy.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class intest extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    String accesspin;
    MyTestAdapter myTestAdapter;
    Button b6;
    TextView max, num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startanimate);
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
    }
    public void animate()
    {
        //剛開始先有動畫
        Intent it=getIntent();
        accesspin=it.getStringExtra("accesspin");
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
                next();
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
                }count++;
            }
        }.start();//動畫CODE到此
    }
    public void next()
    {
        b6 = (Button) findViewById(R.id.button6);
        Intent it=getIntent();
        accesspin=it.getStringExtra("accesspin");

             /////
        String result = DBConnector.executeQuery("SELECT question.question,question.A,question.B,question.C,question.D,question.ans FROM record,testlist,question,testinside WHERE record.pinforaccess='"+accesspin+"' AND record.pinfortest=testlist.KEYIN AND testlist.num=testinside.testtitleid and testinside.questionid=question.id");
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
                map.put("ans",jsonData.getString("ans"));
                lists.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        max = (TextView) findViewById(R.id.max);
        max.setText(String.valueOf(lists.size()));
        num = (TextView) findViewById(R.id.num);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        TestFragments fragmentItem;
        for (int i = 0; i < lists.size(); i++) {
            fragmentItem = new TestFragments(intest.this, i, lists.get(i));
            fragments.add(fragmentItem);
        }
        myTestAdapter = new MyTestAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myTestAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        initDots();
    }
    private ImageView[] dotViews;
    private void initDots() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.zzz);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(10, 0, 10, 0);//设置小圆点左右之间的间隔
        int dotsize;
        if (lists.size() > 5) {
            dotsize = 5;
        } else dotsize = lists.size();
        dotViews = new ImageView[dotsize];
        for (int i = 0; i < dotsize; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.dot_selector);
            if (i == 0) {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            } else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            layout.addView(imageView);//添加到布局里面显示
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intest, menu);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i < dotViews.length; i++) {
            if (position % 5 == i) {
                dotViews[i].setSelected(true);
            } else {
                dotViews[i].setSelected(false);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == lists.size() - 1) {
            b6.setVisibility(View.VISIBLE);
        }
        num.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    public void oc(View v) {
        ArrayList ans = new ArrayList<String>();
        double grade=0;
        for (int i = 0; i < lists.size(); i++) {
            if(myTestAdapter.getItem(i).getArguments().get("ans").equals(lists.get(i).get("ans")))
            {
                grade+=(100./(lists.size()));
                Log.e(myTestAdapter.getItem(i).getArguments().get("ans").toString(), String.valueOf(((int) grade)));
            }


            ans.add(myTestAdapter.getItem(i).getArguments().get("ans"));
        }
        //String x=DBConnector.executeQuery("insert into grade(saccount,grade,testpfa) values('"++"')")
        connectuse c=(connectuse)intest.this.getApplication();
        Toast.makeText(this, String.valueOf((int) grade)+c.accountname, Toast.LENGTH_SHORT).show();

    }
}
