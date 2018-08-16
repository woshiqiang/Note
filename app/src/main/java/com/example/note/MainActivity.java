package com.example.note;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.note.widget.ContentAdapter;
import com.example.note.widget.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.note.Task.deleteAll;
import static com.example.note.Task.deleteById;
import static com.example.note.Task.findAll;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 0x111;
    private ListView mLvContent;
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();// 存放CheckBox的选中状态
    private List<Task> tasks;
    private List<Task> mCheckedTask = new ArrayList<>();//将选中数据放入里面
    private boolean isFirst = true;
    private ContentAdapter contentAdapter;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_add_note).setOnClickListener(this);
        isFirst = SharedPreferencesUtils.getBoolean("isFirst", true);
        mLvContent = findViewById(R.id.lv_content);
        findViewById(R.id.tv_delete).setOnClickListener(this);
        checkPermission();
        if (isFirst) {
            deleteAll();
            SharedPreferencesUtils.putBoolean("isFirst", false);
        }
        initData();
        mLvContent.setDividerHeight(0);//隐藏线
         contentAdapter = new ContentAdapter(MainActivity.this, tasks, stateCheckedMap);
        mLvContent.setAdapter(contentAdapter);

        setOnListViewItemClickListener();

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // (1) 使用handler发送消息
                Message message=new Message();
                message.what=0;
                mHandler.sendMessage(message);
            }
        },0,1000);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_note:
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_delete:
                deleteCheckedTime();
                break;
        }
    }
    // (2) 使用handler处理接收到的消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                Log.e("111",msg+"nnnnn");

                deleteCheckedTime();
                onRestart();
              /*  if(mWebSocketClient.getReadyState()!=1){
                    *//**
                     * 在这里写我们需要一直重复执行的代码
                     * *//*
                }*/
            }
        }
    };
private void deleteCheckedTime(){
  /*  SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String date1="2015-01-25 09:12:09";
    String date2="2015-01-29 09:12:11";*/
  /*  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
    Date date = new Date(System.currentTimeMillis());
    String time = simpleDateFormat.format(date);
    Date dtNow = null;
    Date dtReal = null;*/
    for(int i=0;i<mCheckedTask.size();i++){
        //String time1=mCheckedTask.get(i).time;
     /*   try {
            dtNow =  simpleDateFormat.parse(time);
            dtReal = simpleDateFormat.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
       // if(dtNow.getTime()>dtReal.getTime()){
            deleteById(mCheckedTask.get(i).id);
            tasks.remove(mCheckedTask.get(i));
            mCheckedTask.remove(i);
            stateCheckedMap.put(i, false);
            mLvContent.setItemChecked(i, false);
       // }
    }
    contentAdapter.notifyDataSetChanged();
}
    private void initData() {
        tasks = findAll();
        setStateCheckedMap(false);
    }
    private void setStateCheckedMap(boolean isSelectedAll) {
        for (int i = 0; i < tasks.size(); i++) {
            stateCheckedMap.put(i, isSelectedAll);
            mLvContent.setItemChecked(i, isSelectedAll);
        }
    }

    private void setOnListViewItemClickListener() {
        mLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentAdapter.ViewHolder holder = (ContentAdapter.ViewHolder) view.getTag();
                holder.mChbSelect.toggle();
                mLvContent.setItemChecked(position, holder.mChbSelect.isChecked());
                stateCheckedMap.put(position, holder.mChbSelect.isChecked());

                if (holder.mChbSelect.isChecked()) {
                    mCheckedTask.add(tasks.get(position));
                } else {
                    mCheckedTask.remove(tasks.get(position));
                }
            }
        });
    }

    private boolean checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean result = true;
            for (int granted : grantResults) {
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    result = false;
                    break;
                }
            }
            if (!result) {
                alert();
            }
        }
    }

    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告")
                .setMessage("缺少应用的必要运行权限，应用无法使用")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onRestart() {
        initData();
        ContentAdapter contentAdapter = new ContentAdapter(MainActivity.this, tasks, stateCheckedMap);
        mLvContent.setAdapter(contentAdapter);
        super.onRestart();
    }
}
