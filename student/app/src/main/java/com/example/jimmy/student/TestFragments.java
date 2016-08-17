package com.example.jimmy.student;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class TestFragments extends Fragment implements View.OnClickListener {
    private Context context;
    private int position;
    private String  Qf, Af, Bf, Cf, Df;
    private TextView textView1;
    Button b1, b2, b3, b4;
    Bundle bundle;

    public TestFragments(Context context, int position, Map<String, Object> map) {
        this.context = context;
        bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("Q", map.get("Q").toString());
        bundle.putString("A", map.get("A").toString());
        bundle.putString("B", map.get("B").toString());
        bundle.putString("C", map.get("C").toString());
        bundle.putString("D", map.get("D").toString());
        bundle.putString("ans", "null");
        setArguments(bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_test_fragments, container, false);

        textView1 = (TextView) v.findViewById(R.id.textView);
        textView1.setOnClickListener(this);

        b1 = (Button) v.findViewById(R.id.button2);
        b2 = (Button) v.findViewById(R.id.button3);
        b3 = (Button) v.findViewById(R.id.button4);
        b4 = (Button) v.findViewById(R.id.button5);


        v.findViewById(R.id.button2).setOnClickListener(this);
        v.findViewById(R.id.button3).setOnClickListener(this);
        v.findViewById(R.id.button4).setOnClickListener(this);
        v.findViewById(R.id.button5).setOnClickListener(this);
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView1.setText(getArguments().getString("Q"));
        b1.setText( getArguments().getString("A"));
        b2.setText( getArguments().getString("B"));
        b3.setText( getArguments().getString("C"));
        b4.setText( getArguments().getString("D"));
    }

    @Override
    public void onClick(View v) {
        if(bundle.get("ans")!=null)
        {
            switch(bundle.get("ans").toString())
            {
                case "A":
                    b1.setEnabled(true);
                case "B":
                    b2.setEnabled(true);
                case "C":
                    b3.setEnabled(true);
                case "D":
                    b4.setEnabled(true);
                default :
                    break;
            }
        }
        switch (v.getId()) {
            case R.id.button2:
                bundle.putString("ans", "A");
                Toast.makeText(v.getContext(), "A", Toast.LENGTH_SHORT).show();
                b1.setEnabled(false);
                break;
            case R.id.button3:
                bundle.putString("ans", "B");
                Toast.makeText(v.getContext(), "B", Toast.LENGTH_SHORT).show();
                b2.setEnabled(false);
                break;
            case R.id.button4:
                bundle.putString("ans", "C");
                Toast.makeText(v.getContext(), "C", Toast.LENGTH_SHORT).show();
                b3.setEnabled(false);
                break;
            case R.id.button5:
                bundle.putString("ans", "D");
                Toast.makeText(v.getContext(), "D", Toast.LENGTH_SHORT).show();
                b4.setEnabled(false);
                break;
        }



    }
}
