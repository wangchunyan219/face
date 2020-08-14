package com.example.a11630.face_new;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a11630.domain.add_result_bean;
import com.example.a11630.tools.Base64Util;
import com.example.a11630.tools.GsonUtils;
import com.example.a11630.tools.HttpUtil;
import com.example.a11630.tools.MyHelper;
import com.example.a11630.tools.toolsUnit;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.a11630.tools.GsonUtils;
import com.example.a11630.tools.HttpUtil;
import com.example.a11630.tools.MyHelper;
import com.example.a11630.tools.toolsUnit;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import com.example.a11630.domain.add_result_bean;

import org.xml.sax.helpers.XMLFilterImpl;

public class xiugai extends AppCompatActivity implements View.OnClickListener {

    Button btn_confirm;
    EditText et_user,et_password,et_password1;
    String S_user,S_password,S_password1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiugai);
        btn_confirm=(Button)findViewById(R.id.xiugai_confirm);
        btn_confirm.setOnClickListener(this);
        et_user=(EditText)findViewById(R.id.xiugai_ID);
        et_password=(EditText)findViewById(R.id.xiugai_password);
        et_password1=(EditText)findViewById(R.id.xiugai_password1);

    }
    @Override
    public void onClick(View v)
    {
        S_user=et_user.getText().toString().trim();
        S_password=et_password.getText().toString().trim();
        S_password1=et_password1.getText().toString().trim();
        SQLiteDatabase db;
        MyHelper ggg= new MyHelper(xiugai.this);
        db=ggg.getWritableDatabase();
        if(S_user.equals(""))
        {
            Toast.makeText(xiugai.this, "请输入用户名", Toast.LENGTH_SHORT).show();
        }
        runthreaad();
        db.close();
    }
    void runthreaad()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String url ="https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/delete";
                try
                {
                    Map<String, Object> map = new HashMap<>();
                    map.put("group_id", "face");
                    map.put("user_id", S_user);
                    map.put("passwd_id", S_password);
                    String param = GsonUtils.toJson(map);
                    String clientId = "Ls2FDc39L5SdBd7QyG5NZkcl";
                    String clientSecret = "CNclU3QTqeooftivPa0cTIkXZk1nuGUi";
                    String accessToken = toolsUnit.getAuth(clientId, clientSecret);
                    String result = HttpUtil.post(url, accessToken, "application/json", param);
                    System.out.println(result);
                    Gson gson=new Gson();
                    add_result_bean Result_bean=gson.fromJson(result,add_result_bean.class);
                    System.out.println(Result_bean.getError_code());
                    int Error_code=Result_bean.getError_code();
                    if(Error_code==0)
                    {
                        String message = "id=\"" + S_user + "\"";
                        SQLiteDatabase db;
                        MyHelper ggg = new MyHelper(xiugai.this);
                        db = ggg.getWritableDatabase();
                        ggg.Delete(db,"password_id",message);
                        db.delete("password_id","id=? ",new String[] {"S_user"});
                        ggg.Insert_three(db, "password_id",S_password1,S_user);
                        Looper.prepare();
                        Toast.makeText(xiugai.this, "修改成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                        db.close();
                    }
                    else
                    {
                        String message="id=\""+  S_user   +"\"";
                        SQLiteDatabase db;
                        MyHelper ggg= new MyHelper(xiugai.this);
                        db=ggg.getWritableDatabase();
                        ggg.Delete(db,"password_id",message);
                        db.delete("password_id","id=? ",new String[] {"S_user"});
                        ggg.Insert_three(db, "password_id",S_password1,S_user);
                        String error_message="修改失败："+Result_bean.getError_msg();
                        System.out.println(error_message);
                        Looper.prepare();
                        Toast.makeText(xiugai.this,error_message , Toast.LENGTH_LONG).show();
                        Looper.loop();
                        db.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}