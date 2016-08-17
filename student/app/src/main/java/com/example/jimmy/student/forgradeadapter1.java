package com.example.jimmy.student;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class forgradeadapter1 extends BaseExpandableListAdapter {
    private LayoutInflater inflater = null;
    private Context context;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();
    List<List<String>> iiitem;

    public forgradeadapter1(Context context, List<DataHolder> datalist, List<List<String>> s) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (datalist != null && datalist.size() > 0) {
            mDataList.addAll(datalist);

        }
        this.iiitem = s;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return iiitem.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderitem holderi;
        if (convertView == null) {
            holderi = new ViewHolderitem();
            convertView = inflater.inflate(R.layout.forgradeitem1, null);
            holderi.anstitle = (TextView) convertView.findViewById(R.id.title);
            holderi.no = (TextView) convertView.findViewById(R.id.no);

            convertView.setTag(holderi);
        } else {
            holderi = (ViewHolderitem) convertView.getTag();
        }

        switch (childPosition % 4) {
            case 0:
                holderi.no.setText("A");
                break;
            case 1:
                holderi.no.setText("B");
                break;
            case 2:
                holderi.no.setText("C");
                break;
            case 3:
                holderi.no.setText("D");
                break;

        }
        holderi.anstitle.setText(iiitem.get(groupPosition).get(childPosition).toString());
        return convertView;
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
    public int getGroupCount() {
        return mDataList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataHolder item = mDataList.get(groupPosition);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.forgradeitem2, null);
            holder.no = (TextView) convertView.findViewById(R.id.no);
            holder.ans = (TextView) convertView.findViewById(R.id.ans);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.title.setEllipsize(TextUtils.TruncateAt.END);
            holder.sans = (TextView) convertView.findViewById(R.id.sans);
            convertView.setTag(holder);
            if (item.sans != "null") {
                holder.sans.setText(item.sans);
            }
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ans.setText("(" + item.ans + ")");
        holder.title.setText(item.title);
        holder.no.setText(String.valueOf(groupPosition + 1));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ViewHolder {
        TextView title, no, ans, sans;
    }

    public static class DataHolder {
        int sort;
        String title, ans, sans;
    }

    public static class ViewHolderitem {
        TextView anstitle, no;

    }
}