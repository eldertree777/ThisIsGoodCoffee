package com.example.thisisgoodcoffee;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class input_Info extends Activity {

    final int GET_GALLERY_IMAGE = 200;
    ImageView img;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    String imgURI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_info);


        // xml -> java
        img = (ImageView) findViewById(R.id.imgView);
        Button btn = (Button) findViewById(R.id.btnstorage);
        EditText edtName = (EditText) findViewById(R.id.edtName);
        EditText edtDate = (EditText) findViewById(R.id.edtDate);
        EditText edtLocate = (EditText) findViewById(R.id.edtLocate);
        EditText edtTaste = (EditText) findViewById(R.id.edtTaste);
        EditText edtCom = (EditText) findViewById(R.id.comments);


        // DB 객체 만들기!
        myHelper = new myDBHelper(this);


        // img 불러오는 작업
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });


        // 저장 버튼 입력 DB저장과 finish()  OK!
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //addCoffee insert 하기!
                myHelper.addCoffee(edtName.getText().toString(), edtDate.getText().toString(), edtLocate.getText().toString(), edtTaste.getText().toString(), edtCom.getText().toString(), imgURI);


                // 메인 액티비티 다시 불러오기
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                // 종료
                finish();
            }
        });
    }

    // img 불러오는 작업
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {

                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(in);
                in.close();

                String path = getRealPathFromURI(data.getData());
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                img.setImageBitmap(bitmap);
                imgURI = path;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    // Uri로 절대 경로값구하기
    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        }
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }



}
