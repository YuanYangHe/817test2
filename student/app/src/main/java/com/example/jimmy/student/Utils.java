package com.example.jimmy.student;

import android.app.Activity;

import android.content.Intent;

/**
 * Created by root on 2016/8/10.
 */
public class Utils  {
    private static int sTheme;

    public final static int Basic_style = 0;
    public final static int blue = 1;

    public static void change_sTheme(int theme){
        sTheme = theme;
    }
    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case Basic_style:
                activity.setTheme(R.style.Basic_style);
                break;
            case blue:
                activity.setTheme(R.style.blue);
                break;
        }
    }

}
