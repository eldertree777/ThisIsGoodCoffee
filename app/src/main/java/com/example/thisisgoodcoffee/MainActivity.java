package com.example.thisisgoodcoffee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    int dbRows;
    ArrayList<String> strName, strDate, strLocate, strTaste, strCom;
    ArrayList<String> ImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("This is Good Coffee");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

        strName = new ArrayList<String>();
        strDate = new ArrayList<String>();
        strLocate = new ArrayList<String>();
        strTaste = new ArrayList<String>();
        strCom = new ArrayList<String>();
        //strRate = new ArrayList<String>();
        ImgUri = new ArrayList<String>();

        // ------------------------------------------------------------

        // ------------------------------------------------------------
        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();

        // 임시 table 초기화 코드
        //myHelper.onUpgrade(sqlDB, 1, 2);

        Cursor cursor;

        // DB 데이터 가져오기
        cursor = sqlDB.rawQuery("SELECT * FROM CoffeeTBL;", null);
        dbRows = cursor.getCount();


        while (cursor.moveToNext()) {
            strName.add(cursor.getString(0));
            strDate.add(cursor.getString(1));
            strLocate.add(cursor.getString(2));
            strTaste.add(cursor.getString(3));
            strCom.add(cursor.getString(4));
            ImgUri.add(cursor.getString(5));
        }



        final GridView gv = (GridView) findViewById(R.id.gridView);
        MyGridAdapter gAdapter = new MyGridAdapter(this);
        gv.setAdapter(gAdapter);

    }


    public class MyGridAdapter extends BaseAdapter {
        Context context;
        ImageView imageView;

        public MyGridAdapter(Context c) {
            context = c;
        }

        @Override
        public int getCount() {
            //return DB Select count(*) from DB
            return dbRows + 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5, 5, 5, 5);
            boolean addflag;

            final int pos = position;


            // 마지막 더하기 이미지 인가?
            if (pos < dbRows) {
                addflag = true;

                Bitmap bmp = BitmapFactory.decodeFile(ImgUri.get(pos));
                imageView.setImageBitmap(bmp);

            } else {
                addflag = false;
                imageView.setImageResource(R.drawable.ic_img);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (addflag == true) {
                        View dialogView = (View) View.inflate(MainActivity.this, R.layout.dialog, null);
                        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                        ImageView ivCoffee = (ImageView) dialogView.findViewById(R.id.ivCoffee);

                        Bitmap bmp = BitmapFactory.decodeFile(ImgUri.get(pos));
                        ivCoffee.setImageBitmap(bmp);
                        dlg.setTitle("이름: "+ strName.get(pos) + " | "+ "날짜: "+ strDate.get(pos));
                        dlg.setView(dialogView);
                        dlg.setNegativeButton("닫기", null);
                        dlg.show();
                    } else {
                        // 정보입력 페이지 이동
                        Intent intent = new Intent(getApplicationContext(), input_Info.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

            return imageView;
        }
    }
}