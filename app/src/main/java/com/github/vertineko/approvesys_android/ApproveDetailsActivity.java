package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Model.Apply;
import com.github.vertineko.approvesys_android.Model.Course;
import com.github.vertineko.approvesys_android.Model.User;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApproveDetailsActivity extends AppCompatActivity {

    private TextView approve_id;
    private TextView courseName;
    private TextView name;
    private ImageView imageView;
    private TextView reason;
    private EditText note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_details);

        approve_id = findViewById(R.id.approve_id);
        courseName = findViewById(R.id.approve_courseName);
        name = findViewById(R.id.approve_name);
        imageView = findViewById(R.id.imageView);
        reason = findViewById(R.id.approve_reason);
        note = findViewById(R.id.note);

        String apply_id = getIntent().getStringExtra("apply_id");
        String teacher_id = getIntent().getStringExtra("teacher_id");
        String teacher_role = getIntent().getStringExtra("teacher_role");
        getApplyInfo(apply_id);
        findViewById(R.id.aggre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approve(1,teacher_id,apply_id);
            }
        });

        findViewById(R.id.reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(note.getText().toString().equals("")){
                    Toast.makeText(ApproveDetailsActivity.this, "驳回申请必须在备注一栏填写理由!", Toast.LENGTH_SHORT).show();
                }else {
                    approve(0,teacher_id,apply_id);
                }
            }
        });
    }

    protected void getApplyInfo(String apply_id){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("apply_id",apply_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/GetApproveInfoServlet")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApproveDetailsActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    String message = response.body().string();
                    //Log.i("test",message);
                    JSONObject jsonObject = JSON.parseObject(message);

                    @Override
                    public void run() {
                        //Log.i("sdsdsdsdsdsds", message);
                        if(jsonObject.get("flag").toString().equals("true")){
                            Course course = JSON.parseObject(JSON.toJSONString(jsonObject.get("course")),Course.class);
                            Apply apply = JSON.parseObject(JSON.toJSONString(jsonObject.get("apply")),Apply.class);
                            User user = JSON.parseObject(JSON.toJSONString(jsonObject.get("user")), User.class);
                            byte[] image = JSON.parseObject(JSON.toJSONString(jsonObject.get("image")),byte[].class);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
                            approve_id.setText("申请表id：" + apply.getId());
                            courseName.setText("课程名称：" + course.getName());
                            name.setText("申请人姓名：" + user.getName());
                            reason.setText("申请理由：" + apply.getReason());
                            imageView.setImageBitmap(bitmap);
                        }else {
                            Toast.makeText(ApproveDetailsActivity.this,"该申请不存在！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    protected void approve(int ispass , String teacher_id,String apply_id){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("ispass",ispass+"")
                .add("apply_id",apply_id)
                .add("teacher_id",teacher_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/ApproveServlet")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApproveDetailsActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    String message = response.body().string();
                    JSONObject jsonObject = JSON.parseObject(message);
                    @Override
                    public void run() {
                        //Log.i("test", message);
                        if(jsonObject.get("flag").toString().equals("true")){
                            if(ispass == 1){
                                Toast.makeText(ApproveDetailsActivity.this, "已同意该申请！", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ApproveDetailsActivity.this, "已驳回该申请！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else {
                            Toast.makeText(ApproveDetailsActivity.this,"该课程申请不存在！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeThemes:
                // TODO: 更换主题
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    // 当前处于深色模式，执行相应的操作
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    // 当前处于浅色模式，执行相应的操作
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                return true;
            case R.id.exit:
                // TODO: 退出登录
                Intent intent = new Intent(ApproveDetailsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.exchange:
                // TODO: 切换账号
                Intent intent1 = new Intent(ApproveDetailsActivity.this,WelcomeActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}