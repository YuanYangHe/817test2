package com.example.jimmy.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class selftest extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    RadioGroup rg;
    TextView tv;
    private LruCache<String, Bitmap> mLruCache;
    Handler mHandler;
    HandlerThread mHandlerThread;
    MyAdapter mAdapter;
    ListView lv;
    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selftest);
        settings = getSharedPreferences("studentuse_pref", 0);
        mHandlerThread = new HandlerThread("LRU Cache Handler");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 2;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        rg = (RadioGroup) findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(this);
        tv = (TextView) findViewById(R.id.textView16);
        getdata(false);
        lv = (ListView) findViewById(R.id.listView2);
        mAdapter = new MyAdapter();
        lv.setAdapter(mAdapter);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.easy) {
            tv.setText("規則說明: 答題時間15秒 錯誤次數3次則結束測驗");

            renew(false);
        } else {
            tv.setText("規則說明: 答題時間10秒 錯誤次數1次則結束測驗");
            renew(true);
        }
    }

    public void getdata(Boolean b) {
        String result;
        if (!b) {
            result = DBConnector.executeQuery("SELECT account,score FROM forself WHERE e0h1='" + 0 + "' order by score desc");
        } else {
            result = DBConnector.executeQuery("SELECT account,score FROM forself WHERE e0h1='" + 1 + "' order by score desc");
        }
        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < (jsonArray.length() > 10 ? 10 : jsonArray.length()); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject jsonData = jsonArray.getJSONObject(i);
                map.put("score", jsonData.getInt("score"));
                map.put("studentid", jsonData.getString("account"));
                map.put("url", "http://" + settings.getString("net","XXX") + "/uploads/s" + jsonData.getString("account") + ".png");
                lists.add(map);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void renew(boolean x) {
        lists.clear();
        mLruCache.evictAll();
        getdata(x);

        mAdapter = new MyAdapter();
        lv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    public void start(View v) {
        Intent it = new Intent(selftest.this, inselftest.class);
        if (rg.getCheckedRadioButtonId() == R.id.easy) {
            it.putExtra("eorh", "easy");
        } else it.putExtra("eorh", "hard");
        startActivity(it);
    }

    /////////////////////////////////////
    private class MyAdapter extends BaseAdapter {
        private Map<String, String> mLoadingMap;

        public MyAdapter() {
            mLoadingMap = new HashMap<String, String>();
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final Holder holder;
            if (null == v) {
                v = LayoutInflater.from(selftest.this).inflate(R.layout.itemforrank, null);
                holder = new Holder();
                holder.no = (TextView) v.findViewById(R.id.ininno);
                holder.img = (ImageView) v.findViewById(R.id.ininimg);
                holder.title = (TextView) v.findViewById(R.id.inintitle);
                holder.score = (TextView) v.findViewById(R.id.score);
                v.setTag(holder);
            } else {
                holder = (Holder) v.getTag();
            }
            holder.img.setImageResource(R.drawable.zzz);
            holder.title.setText(lists.get(position).get("studentid").toString());
            holder.no.setText(String.valueOf(position + 1));
            holder.score.setText(lists.get(position).get("score").toString());
            final String key = position + "_cache";
            Bitmap b = mLruCache.get(key);
            if (b == null && !mLoadingMap.containsKey(key)) {
                mLoadingMap.put(key, lists.get(position).get("url").toString());
                Log.e("lru", "load pic" + position);
                mHandler.post(new Runnable() {
                    Bitmap bmp;

                   @Override
                    public void run() {
                        bmp = decodeBitmap(lists.get(position).get("url").toString(), 200);
                        if (bmp != null) {
                            mLruCache.put(key, bmp);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                    mLoadingMap.remove(key);
                                }
                            });
                        }

                    }
                });
            } else {
                Log.e("lru", "cache");
                holder.img.setImageBitmap(b);
            }
            return v;
        }

        class Holder {
            TextView no, title, score;
            ImageView img;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeBitmap(String url, int maxWidth) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxWidth);
            InputStream is = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (MalformedInputException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
