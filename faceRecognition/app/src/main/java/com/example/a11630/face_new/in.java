package com.example.a11630.face_new;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import com.example.a11630.domain.add_result_bean;
import com.example.a11630.tools.Base64Util;
import com.example.a11630.tools.GsonUtils;
import com.example.a11630.tools.HttpUtil;
import com.example.a11630.tools.MyHelper;
import com.example.a11630.tools.toolsUnit;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class in extends AppCompatActivity implements View.OnClickListener {
    //定义登录的姓名和ID号
    String names, IDs;

    //定义登录的密码
    String passwds, passwds1;

    //定义登录按钮
    Button btn_Login;

    //定义显示相册图片的图框
    ImageView imageView;

    //定义输入的文本域
    EditText et_name, et_ID;

    //定义输入密码的文本域
    EditText et_passwd, et_passwd1;

    //定义图片的路径
    private String imagePath = null;

    //定义图片的uri
    private Uri imageUri;

    //定义返回值，判断图片的来源
    private int Photo_ALBUM = 1, CAMERA = 2;

    int FLAG = 0;
    private Bitmap bp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        btn_Login = (Button) findViewById(R.id.btn_login);
        btn_Login.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.name);
        et_ID = (EditText) findViewById(R.id.ID);
        et_passwd = (EditText) findViewById(R.id.passwd);
        et_passwd1 = (EditText) findViewById(R.id.passwd1);
        FLAG = 0;
    }
    //检查登录密码是否有效
    //设定的密码只能由数字或字母组成
    boolean check(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    //登录按钮被点击
    @Override
    public void onClick(View v)
    {
        names = et_name.getText().toString().trim();
        IDs = et_ID.getText().toString().trim();
        passwds = et_passwd.getText().toString().trim();
        passwds1 = et_passwd1.getText().toString().trim();
        SQLiteDatabase db;
        MyHelper ggg = new MyHelper(in.this);
        db = ggg.getReadableDatabase();
        if (IDs.equals(""))
        {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Cursor cursor =  db.query("name_id",new String[]{"id"},"id=?",new String[]{IDs},null,null,null);
            if(cursor.moveToNext()) {
                String id_query = cursor.getString(cursor.getColumnIndex("id"));
                if(id_query.equals(IDs)) {
                    Toast.makeText(this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (names.equals("")) {
                    Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (passwds.equals("")) {
                        Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (check(passwds) == false) {
                            Toast.makeText(this, "密码非法", Toast.LENGTH_SHORT).show();
                        } else {
                            if (passwds1.equals("")) {
                                Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                if (check(passwds1) == false) {
                                    Toast.makeText(this, "确认密码非法", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (!passwds.equals(passwds1)) {
                                        Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // 设置对话框标题
                                        new AlertDialog.Builder(in.this)
                                                .setTitle("系统提示")

                                                // 设置显示的内容
                                                .setMessage("请选择上传方式")


                                                //右边按钮
                                                .setPositiveButton("返回",
                                                        new DialogInterface.OnClickListener() {// 添加确定按钮

                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int which) {// 确定按钮的响应事件
                                                            }

                                                        })
                                                //中间按钮
                                                .setNeutralButton("从相册上传", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        //选择数据
                                                        Intent in = new Intent(Intent.ACTION_PICK);
                                                        //选择的数据为图片
                                                        in.setType("image/*");
                                                        startActivityForResult(in, Photo_ALBUM);
                                                    }
                                                })
                                                //左边按钮
                                                .setNegativeButton("拍照",
                                                        new DialogInterface.OnClickListener() {

                                                            @TargetApi(Build.VERSION_CODES.M)
                                                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //7.0拍照必加
                                                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                                                StrictMode.setVmPolicy(builder.build());
                                                                builder.detectFileUriExposure();
                                                                //临时照片存储地
                                                                File outputImage = new File(Environment.getExternalStorageDirectory() + File.separator + "face.jpg");
                                                                try {
                                                                    if (outputImage.exists()) {
                                                                        outputImage.delete();
                                                                    }
                                                                    //创建临时地址
                                                                    outputImage.createNewFile();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                imageUri = Uri.fromFile(outputImage);   //将file转成uri对象
                                                                imagePath = outputImage.getAbsolutePath();
                                                                Log.i("拍照图片路径", imagePath);
                                                                //跳转相机
                                                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                                                //返回照片路径
                                                                startActivityForResult(intent, CAMERA);
                                                            }
                                                        }).show();// 在按键响应事件中显示此对话框
                                    }

                                }
                            }
                        }
                    }

                }

            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // 相册选择图片
        if (requestCode == Photo_ALBUM)
        {
            if (data != null)
            {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToNext();

                //获得图片的绝对路径
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                cursor.close();
                Log.i("图片路径", imagePath);
                bp = toolsUnit.getimage(imagePath);
                runthreaad();
            }
        } else if (requestCode == CAMERA) {
            bp = toolsUnit.getimage(imagePath);
            runthreaad();
        }
    }

    void runthreaad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
                try {
                    byte[] bytes1 = toolsUnit.getBytesByBitmap(bp);
                    String image1 = Base64Util.encode(bytes1);

                    Map<String, Object> map = new HashMap<>();
                    map.put("image", image1);
                    map.put("group_id", "face");
                    map.put("user_id", IDs);
                    map.put("passwd_id",passwds);
                    map.put("user_info", "abc");
                    map.put("liveness_control", "NORMAL");
                    map.put("image_type", "BASE64");
                    map.put("quality_control", "LOW");
                    String param = GsonUtils.toJson(map);

                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。

                    String clientId = "Ls2FDc39L5SdBd7QyG5NZkcl";
                    String clientSecret = "CNclU3QTqeooftivPa0cTIkXZk1nuGUi";
                    String accessToken = toolsUnit.getAuth(clientId, clientSecret);

                    //  = "24.470560ecfc8ded10d622b3dd4e258f34.2592000.1563086633.282335-15236904";

                    String result = HttpUtil.post(url, accessToken, "application/json", param);
                    System.out.println(result);
                    Gson gson = new Gson();
                    add_result_bean Result_bean = gson.fromJson(result, add_result_bean.class);

                    int Error_code = Result_bean.getError_code();
                    if (Error_code == 0) {
                        SQLiteDatabase db;
                        MyHelper ggg = new MyHelper(in.this);
                        db = ggg.getWritableDatabase();
                        ggg.Insert(db, "name_id", names, IDs);         //把昵称用户名传入name_id表中
                        ggg.Insert_three(db, "password_id",passwds,IDs);     //把昵称密码存入password_id表
                        Looper.prepare();
                        Toast.makeText(in.this, "注册成功", Toast.LENGTH_LONG).show();
;
                        Looper.loop();

                    } else {
                        String error_message = "注册失败，请重新注册：" + Result_bean.getError_msg();
                        System.out.println( error_message);
                        Looper.prepare();
                        Toast.makeText(in.this, error_message, Toast.LENGTH_LONG).show();
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

