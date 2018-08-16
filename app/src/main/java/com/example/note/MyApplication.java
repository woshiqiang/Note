package com.example.note;

import android.app.Application;
import android.content.Context;

import com.example.note.widget.SharedPreferencesUtils;

/**
 * Created by admin on 2018/7/24.
 */

public class MyApplication extends Application {

    public static MyApplication MyInstance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyInstance = this;
        context = getApplicationContext();
        SharedPreferencesUtils.init(context);
    }

    public static Context getContextObject() {
        return context;
    }

    public static MyApplication getMyInstance() {
        return MyInstance;
    }
}