package com.example.thisisgoodcoffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class myDBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "CoffeeTBL";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DATE = "DATE";
    private static final String COLUMN_locate = "locate";
    private static final String COLUMN_taste = "taste";
    private static final String COLUMN_comment = "comment";
    private static final String COLUMN_IMGURI = "IMGURI";
    private Context context;

    public myDBHelper(Context context) {
        super(context, "Coffee", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE  CoffeeTBL ( NAME CHAR(50), DATE CHAR(20), locate CHAR(50),taste CHAR(50) ,comment CHAR(200), IMGURI char(1000));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CoffeeTBL");
        onCreate(db);
    }

    void addCoffee(String Name, String Date, String Locate,String taste,String comment,String IMGURI)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, Name);
        cv.put(COLUMN_DATE, Date);
        cv.put(COLUMN_locate, Locate);
        cv.put(COLUMN_taste, taste);
        cv.put(COLUMN_comment, comment);
        cv.put(COLUMN_IMGURI, IMGURI);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
        {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }
    }
}
