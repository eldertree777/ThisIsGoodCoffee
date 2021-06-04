package com.example.thisisgoodcoffee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context) {
        super(context, "Coffee", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE  CoffeeTBL ( NAME CHAR(50), DATE CHAR(20), locate CHAR(50),taste CHAR(50) ,comment CHAR(200), IMGURI char(200));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CoffeeTBL");
        onCreate(db);
    }
}
