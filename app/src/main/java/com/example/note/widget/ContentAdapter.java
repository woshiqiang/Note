package com.example.note.widget;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.note.MyApplication;
import com.example.note.R;
import com.example.note.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by maosheng on 2018/8/4.
 */

public class ContentAdapter extends BaseAdapter {
    private Context mContext;
    private int code;
    private String airportName;
    private static List<Task> mDatas = new ArrayList<>();
    Task preTask = new Task();
    private boolean isShowTvNone=false;
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();// 存放CheckBox的选中状态
    /**
     * Contains the list of objects that represent the data of this Adapter.
     * Adapter数据源
     */


    private LayoutInflater mInflater;

    public ContentAdapter(Context context, List<Task> mDatas, SparseBooleanArray stateCheckedMap) {
        mContext = context;
        this.mDatas = mDatas;
        sortDatasByTime(this.mDatas);
        mInflater = LayoutInflater.from(context);
        this.stateCheckedMap=stateCheckedMap;
    }

    public List<Task> visibleList() {
        return mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size() > 0 ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_lv_content, parent, false);
            holder = new ViewHolder();
            holder.mChbSelect = (CheckBox) convertView.findViewById(R.id.chb_select);
            holder.mTvContent = (TextView) convertView.findViewById(R.id.tv_content);
            holder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mTvNone = (TextView) convertView.findViewById(R.id.tv_none);
            holder.mLlShowTime = (LinearLayout) convertView.findViewById(R.id.ll_show_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mChbSelect.setChecked(stateCheckedMap.get(position));
        holder.mTvContent.setText(mDatas.get(position).content);
        holder.mTvTime.setText(mDatas.get(position).time);

        if(position!=0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
            try {
                int i= (int) (simpleDateFormat.parse(mDatas.get(position).time).getTime() - simpleDateFormat.parse(preTask.time).getTime());
                if (i == 0) {
                    holder.mLlShowTime.setVisibility(View.GONE);
                }else{
                    if(isShowTvNone){
                        isShowTvNone=false;
                    }else {
                        isShowTvNone=true;
                    }
                 /*   if(!isShowTvNone){

                    }*/

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            preTask=mDatas.get(position);
        }else {
            preTask=mDatas.get(position);
           // isShowTvNone=false;
        }

        if(isShowTvNone){
            holder.mTvNone.setVisibility(View.VISIBLE);
        }else {
            holder.mTvNone.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static List<Task> getDatas() {
        return mDatas;
    }

    public class ViewHolder {
        public  CheckBox mChbSelect;
        TextView mTvContent;
        TextView mTvTime,mTvNone;
        LinearLayout mLlShowTime;
    }
    private void sortDatasByTime(List<Task> mDatas){
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        Collections.sort(mDatas, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                int i=0;
                try {
                 /*   Date  dtNow =  simpleDateFormat.parse(o1.time);
                    Date  dtReal = simpleDateFormat.parse(o2.time);*/

                     i= (int) (simpleDateFormat.parse(o1.time).getTime() - simpleDateFormat.parse(o2.time).getTime());
                    if (i == 0) {
                        return i;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return i;
            }
        });
        for(int i=0;i<mDatas.size();i++){
            Log.e("time",i+mDatas.get(i).time);
        }
    }

}