package com.example.jimmy.student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import static com.example.jimmy.student.R.id.spinner;

public class settings extends AppCompatActivity {
    private Spinner spThemes;
    public static final String KEY = "com.my.package.app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);

        setContentView(R.layout.activity_settings);

        setupSpinnerItemSelection();


    }
    private void setupSpinnerItemSelection() {
        spThemes = (Spinner) findViewById(spinner);
        spThemes.setSelection(connectuse.currentPosition);
        connectuse.currentPosition = spThemes.getSelectedItemPosition();

        spThemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (connectuse.currentPosition != position) {
                    Utils.changeToTheme(settings.this, position);
                }
                connectuse.currentPosition = position;
                SharedPreferences settings = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("THEMES",position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
