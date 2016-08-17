package com.example.jimmy.student;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class forgradeadapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater = null;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();
    private List<List<forgradeadapter.DataHolderchild>> iiitem = new ArrayList<>();

    public forgradeadapter(Context context, List<DataHolder> datalist, List<List<forgradeadapter.DataHolderchild>> in) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (datalist != null && datalist.size() > 0) {
            mDataList.addAll(datalist);
            iiitem.addAll(in);
        }
    }

    @Override
    public int getGroupCount() {
        return mDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return iiitem.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return iiitem.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return iiitem.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.forgradeitem, null);
            holder.no = (TextView) convertView.findViewById(R.id.no);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.title.setEllipsize(TextUtils.TruncateAt.END);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        DataHolder item = mDataList.get(groupPosition);
        int[] arr_pic = {R.drawable.chinese, R.drawable.english, R.drawable.math, R.drawable.science, R.drawable.socieity, R.drawable.other, R.drawable.all};
        holder.img.setImageResource(arr_pic[item.sort - 1]);
        holder.title.setText(item.title);
        holder.no.setText(String.valueOf(groupPosition + 1));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderchild holder;
        if (convertView == null) {
            holder = new ViewHolderchild();
            convertView = inflater.inflate(R.layout.forgradeitem3, null);
            holder.pfa = (TextView) convertView.findViewById(R.id.pfaa);
            holder.grade = (TextView) convertView.findViewById(R.id.grade);
            holder.grade.setEllipsize(TextUtils.TruncateAt.END);

            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolderchild) convertView.getTag();
        }
        DataHolderchild item = iiitem.get(groupPosition).get(childPosition);
        if (item.pfa != null) {
            holder.pfa.setText(item.pfa);
            holder.grade.setText(item.grade);
        } else {
            holder.grade.setText("----");
            holder.pfa.setText("----");
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ViewHolder {
        TextView title, no;
        ImageView img;
    }

    public static class ViewHolderchild {
        TextView grade, pfa;

    }

    public static class DataHolder {
        int sort;
        String title, id;
    }

    public static class DataHolderchild {
        String grade, pfa, pft;
    }
}
