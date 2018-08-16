package com.example.note;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maosheng on 2018/7/24.
 */

public class Task extends BaseModel {
    public String id;
    public String content;
    public String time;
    private static final String TABLE_NAME = "tab_memo";

    public static List<Task> findAll() {
        List<Task> taskList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select ID,Content,Time from tab_memo", null);
        while (cursor.moveToNext()) {
            Task task = new Task();
            task.id = cursor.getString(0);
            task.content = cursor.getString(1);
            task.time = cursor.getString(2);
            taskList.add(task);
        }
        cursor.close();
        return taskList;
    }
    public static void insertOneTask(Task task) {
        db.execSQL("INSERT INTO " + TABLE_NAME + "(ID,Content,Time) VALUES(?,?,?)",
                new Object[]{task.id, task.content,  task.time});
    }

    public static Task findOneWithId(String id) {
        Task task = new Task();
        Cursor cursor = db.rawQuery("select ID,Content,Time from tab_memo  WHERE ID = '" + id + "'", null);
        while (cursor.moveToNext()) {
            task.id = cursor.getString(0);
            task.content = cursor.getString(1);
            task.time = cursor.getString(2);
        }
        cursor.close();
        return task;
    }

    public static void deleteById(String Id) {
        String sql = "DELETE FROM " + TABLE_NAME + " where ID='" + Id + "'";
        db.execSQL(sql);
    }
    public static void deleteAll() {
        String sql = "DELETE FROM " + TABLE_NAME ;
        db.execSQL(sql);
    }
    public static void updateOneById(Task task) {
        db.execSQL("update tab_memo set Content=?,Time=? where ID=? ",
                new Object[]{task.content,task.time,task.id});
    }
}
