package com.example.a11630.face_new;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a11630.tools.MyHelper;

import java.util.Calendar;

public class item1 extends AppCompatActivity {

    TextView search_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        search_sum = (TextView) findViewById(R.id.tea_tv_sum);

        StringBuffer sum = new StringBuffer();
        SQLiteDatabase db;
        MyHelper ggg = new MyHelper(item1.this);
        db = ggg.getWritableDatabase();
        Cursor cursor = db.query("time_id", null,
                null, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            String id = cursor.getString(0);
            String name_time = cursor.getString(1);

            System.out.println("查询结果：\n");
            System.out.println(id + ":" + name_time);
            sum.append("查询结果：" + "\n    ID:" + id + "        time:" + name_time + "\n");

            while (cursor.moveToNext()) {
                String id1 = cursor.getString(0);
                String name_time1 = cursor.getString(1);
                System.out.println("查询结果：\n");
                System.out.println(id1 + ":" + name_time1);
                sum.append("    ID:" + id1 + "        time:" + name_time1 + "\n");
            }
        }
        cursor.close();
        db.close();
        search_sum.setText(sum.toString());

    }
}

