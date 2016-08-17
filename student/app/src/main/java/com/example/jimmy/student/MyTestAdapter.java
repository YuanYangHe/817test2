package com.example.jimmy.student;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyTestAdapter extends RecyclerView.Adapter<MyTestAdapter.ViewHolder>  {
    private List<Map<String, Object>> x;
    private List<DataHolder> forans;
    private Context context;

    public MyTestAdapter(List<Map<String, Object>> dt, List<DataHolder> forans, Context context) {
        this.x = dt;
        this.context = context;
        this.forans = forans;
    }

    @Override
    public MyTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.testitem, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyTestAdapter.ViewHolder holder, final int position) {
        holder.ca.setChecked(false);
        holder.cb.setChecked(false);
        holder.cc.setChecked(false);
        holder.cd.setChecked(false);
        switch (forans.get(position).ans) {
            case "A":
                holder.ca.setChecked(true);
                break;
            case "B":
                holder.cb.setChecked(true);
                break;
            case "C":
                holder.cc.setChecked(true);
                break;
            case "D":
                holder.cd.setChecked(true);
                break;
            default:
                break;
        }
        Log.e(holder.toString(), "XXX");
        holder.TQ.setText(x.get(position).get("Q").toString());
        holder.TA.setText(x.get(position).get("A").toString());
        holder.TB.setText(x.get(position).get("B").toString());
        holder.TC.setText(x.get(position).get("C").toString());
        holder.TD.setText(x.get(position).get("D").toString());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ca.setChecked(false);
                holder.cb.setChecked(false);
                holder.cc.setChecked(false);
                holder.cd.setChecked(false);
                switch (v.getId()) {
                    case R.id.checkBox:
                        holder.ca.setChecked(true);
                        forans.get(position).ans = "A";
                        break;
                    case R.id.checkBox2:
                        holder.cb.setChecked(true);
                        forans.get(position).ans = "B";
                        break;
                    case R.id.checkBox3:
                        holder.cc.setChecked(true);
                        forans.get(position).ans = "C";
                        break;
                    case R.id.checkBox4:
                        holder.cd.setChecked(true);
                        forans.get(position).ans = "D";
                        break;
                }
            }
        };
        holder.ca.setOnClickListener(clickListener);
        holder.cb.setOnClickListener(clickListener);
        holder.cc.setOnClickListener(clickListener);
        holder.cd.setOnClickListener(clickListener);
    }


    @Override
    public int getItemCount() {
        return x.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TQ, TA, TB, TC, TD;
        CheckBox ca, cb, cc, cd;

        public ViewHolder(View itemView) {
            super(itemView);
            //this.setIsRecyclable(false);
            ca = (CheckBox) itemView.findViewById(R.id.checkBox);
            cb = (CheckBox) itemView.findViewById(R.id.checkBox2);
            cc = (CheckBox) itemView.findViewById(R.id.checkBox3);
            cd = (CheckBox) itemView.findViewById(R.id.checkBox4);
            TQ = (TextView) itemView.findViewById(R.id.textView);
            TA = (TextView) itemView.findViewById(R.id.textView2);
            TB = (TextView) itemView.findViewById(R.id.textView3);
            TC = (TextView) itemView.findViewById(R.id.textView4);
            TD = (TextView) itemView.findViewById(R.id.textView5);
        }
    }

    public static class DataHolder {
        String ans = "X";
    }

}
