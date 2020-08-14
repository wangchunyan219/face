package com.example.a11630.face_new;

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

public class delete extends AppCompatActivity implements View.OnClickListener {

    Button btn_confirm;
    EditText et_user;
    String S_user;
    //EditText et_password;
   // String S_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        btn_confirm=(Button)findViewById(R.id.confirm);
        btn_confirm.setOnClickListener(this);
        et_user=(EditText)findViewById(R.id.user);
       // et_password =(EditText)findViewById(R.id.password);
    }

    @Override
    public void onClick(View v)
    {
       S_user=et_user.getText().toString().trim();
       if(S_user.equals(""))
       {
           Toast.makeText(delete.this, "请输入用户名", Toast.LENGTH_SHORT).show();
       }

       runthreaad();
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
                    //map.put("passwd_id", S_password);
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
                    if(Error_code==0)              //删除成功
                    {
                            String message = "id=\"" + S_user + "\"";
                            //String message1 = "password=\"" + S_password + "\"";
                            SQLiteDatabase db;
                            MyHelper ggg = new MyHelper(delete.this);
                            db = ggg.getWritableDatabase();
                            ggg.Delete(db, "name_id", message);  //将三张表中的信息一起删除
                            ggg.Delete(db,"time_id",message);
                            ggg.Delete(db,"password_id",message);
                            db.delete("name_id","id=? ",new String[] {"S_user"});
                            db.delete("time_id","id=? ",new String[] {"S_user"});
                            db.delete("password_id","id=? ",new String[] {"S_user"});
                            Looper.prepare();
                            Toast.makeText(delete.this, "删除成功", Toast.LENGTH_LONG).show();
                            Looper.loop();

                    }
                    else                          //删除失败
                    {
                        String message="id=\""+  S_user   +"\"";
                        //String message1="password=\""+  S_password   +"\"";
                        System.out.println("删除:"+message);
                       // System.out.println("删除:"+message1);
                        SQLiteDatabase db;
                        MyHelper ggg= new MyHelper(delete.this);
                        db=ggg.getWritableDatabase();
                        ggg.Delete(db,"name_id",message);
                        ggg.Delete(db,"time_id",message);
                        ggg.Delete(db,"password_id",message);
                        db.delete("name_id","id=? ",new String[] {"S_user"});
                        db.delete("time_id","id=? ",new String[] {"S_user"});
                        db.delete("password_id","id=? ",new String[] {"S_user"});
                        String error_message="删除失败："+Result_bean.getError_msg();
                        System.out.println(error_message);
                        Looper.prepare();
                        Toast.makeText(delete.this,error_message , Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}