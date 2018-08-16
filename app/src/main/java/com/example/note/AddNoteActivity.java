package com.example.note;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static com.example.note.Task.insertOneTask;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtNoteContent;

    private RelativeLayout selectDate, selectTime;
    private TextView currentDate, currentTime;
    private CustomDatePicker customDatePicker1, customDatePicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initView();
      /*  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        String time=simpleDateFormat.format(date);
        mTime=time;*/
        // Log.e("1111",time);
        initDatePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                save();
                if (mEtNoteContent.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddNoteActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AddNoteActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.selectTime:
                // 日期格式为yyyy-MM-dd HH:mm
                customDatePicker2.show(currentTime.getText().toString());
                break;
        }
    }

    private void save() {
        if (!mEtNoteContent.getText().toString().trim().isEmpty()) {
            String content = mEtNoteContent.getText().toString().trim();
            String id = UUID.randomUUID().toString();
            Task task = new Task();
            task.id = id;
            task.content = content;
            task.time = currentTime.getText().toString().split(" ")[0];
            insertOneTask(task);
        }
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        mEtNoteContent = findViewById(R.id.et_note_content);
        selectTime = (RelativeLayout) findViewById(R.id.selectTime);
        currentTime = (TextView) findViewById(R.id.currentTime);
        selectTime.setOnClickListener(this);
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        //currentDate.setText(now.split(" ")[0]);
        currentTime.setText(time);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                //currentDate.setText(time.split(" ")[0]);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentTime.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
