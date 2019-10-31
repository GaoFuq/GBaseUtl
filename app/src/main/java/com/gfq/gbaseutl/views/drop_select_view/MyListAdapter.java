package com.gfq.gbaseutl.views.drop_select_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yagujinfu.android.R;

import java.util.List;

/**
 * 作者：高富强
 * 日期：2019/8/16 14:09
 * 描述：
 */
public class MyListAdapter extends BaseAdapter {
    private List<SearchBean> mData;
    private Context mContext;
    private View mView;

    public MyListAdapter(Context mContext,List<SearchBean> mData) {
        this.mData = mData;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_popwin,null);
            holder.textView = convertView.findViewById(R.id.listview_popwind_tv);
            holder.imageView = convertView.findViewById(R.id.iv_check);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.textView.setText(mData.get(position).getName());
        holder.imageView.setImageResource(mData.get(position).getImgResId());
        return convertView;

    }



    public class Holder{
        TextView textView;
        ImageView imageView;
    }
}