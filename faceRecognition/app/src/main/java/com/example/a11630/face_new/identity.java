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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import com.example.a11630.tools.MyHelper;
public class identity extends AppCompatActivity implements View.OnClickListener {
    Button btn_tea;
    Button btn_stu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identity);
        btn_tea = (Button) findViewById(R.id.tea_identity);
        btn_tea.setOnClickListener(this);
        btn_stu = (Button) findViewById(R.id.stu_identity);
        btn_stu.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.tea_identity) {
            Intent intent = new Intent(identity.this, tea_seacher.class);
            startActivity(intent);
        } else {
            Intent intent1 = new Intent(identity.this, stu_seacher.class);
            startActivity(intent1);
        }
    }
}
