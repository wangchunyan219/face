package com.example.a11630.face_new;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import com.example.a11630.tools.MyHelper;
public class daka_in extends AppCompatActivity   {
    //定义登录的ID
    String  IDs;
    //定义登录的密码
    String passwds;

    //定义登录按钮
    Button btn_Login;

    //定义输入的文本域
    EditText et_ID;
    EditText et_passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daka_register);
        et_ID = (EditText) findViewById(R.id.daka_ID);
        et_passwd = (EditText) findViewById(R.id.daka_password);
        btn_Login = (Button) findViewById(R.id.daka_btn_login);
        btn_Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                IDs = et_ID.getText().toString().trim();
                passwds = et_passwd.getText().toString().trim();
                SQLiteDatabase db;
                MyHelper ggg = new MyHelper(daka_in.this);
                db = ggg.getReadableDatabase();
                if (IDs.equals(""))
                {
                    Toast.makeText(daka_in.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                        if (passwds.equals("")) {
                            Toast.makeText(daka_in.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            Cursor cursor = db.query("password_id", new String[]{"id","password"}, "id=? and password=? ", new String[]{IDs,passwds}, null, null,null);
                            if (cursor.moveToNext()) {
                                    String id_query = cursor.getString(cursor.getColumnIndex("id"));
                                    String password_query = cursor.getString(cursor.getColumnIndex("password"));
                                    if(id_query.equals(IDs) && password_query.equals(passwds)) {
                                        Toast.makeText(daka_in.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(daka_in.this, opt.class);
                                        startActivity(intent);
                                    }
                                   if (id_query.equals(IDs) && !(password_query.equals(passwds))) {
                                        Toast.makeText(daka_in.this, "密码不匹配", Toast.LENGTH_SHORT).show();
                                    }
                                    if(!id_query.equals(IDs)) {
                                        Toast.makeText(daka_in.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                    }
                            } else {
                                Toast.makeText(daka_in.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        }
              }
                db.close();
            }

        });
    }

}
