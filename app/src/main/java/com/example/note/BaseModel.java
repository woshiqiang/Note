package com.example.note;

import android.database.sqlite.SQLiteDatabase;



/**
 * Created by admin on 2018/7/24.
 */

public class BaseModel {
    public static SQLiteDatabase db =  new SQLDatabaseManager().openDatabase(MyApplication.getContextObject());
}
