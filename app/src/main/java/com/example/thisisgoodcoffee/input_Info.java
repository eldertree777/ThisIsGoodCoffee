package com.example.thisisgoodcoffee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

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



        img = (ImageView)findViewById(R.id.imgView);
        Button btn = (Button)findViewById(R.id.btnstorage);
        EditText edtName = (EditText)findViewById(R.id.edtName);
        EditText edtDate = (EditText)findViewById(R.id.edtDate);
        EditText edtLocate = (EditText)findViewById(R.id.edtLocate);
        EditText edtTaste = (EditText)findViewById(R.id.edtTaste);
        EditText edtCom = (EditText)findViewById(R.id.comments);

        myHelper = new myDBHelper(this);

        // img 불러오는 작업
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_GALLERY_IMAGE);
            }
        });


        // 저장 버튼 입력 DB저장과 finish()
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                String str = "INSERT INTO artistTBL VALUES (" + edtName.getText().toString() + "," +
                        edtDate.getText().toString() + "," + edtLocate.getText().toString() + "," +
                        edtTaste.getText().toString() + "," + edtCom.getText().toString() + ","+ imgURI+ ");" ;

                sqlDB.execSQL(str);
                sqlDB.close();
                finish();
            }
        });
    }

    // img 불러오는 작업
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == GET_GALLERY_IMAGE && resultCode ==RESULT_OK && data != null && data.getData() !=null){
            Uri selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);

            // URI 변수저장
            imgURI = selectedImageUri.toString();
        }
    }




}
