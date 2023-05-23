package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Model.Apply;
import com.github.vertineko.approvesys_android.Model.Course;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApplyDetailsActivity extends AppCompatActivity {
    private TextView courseName;
    private TextView status;
    private TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_details);
        String user_id = getIntent().getStringExtra("user_id");
        String course_id = getIntent().getStringExtra("course_id");
        String apply_id = getIntent().getStringExtra("apply_id");
        courseName = findViewById(R.id.details_courseName);
        status = findViewById(R.id.details_status);
        note = findViewById(R.id.details_note);



        //
        getInfo(course_id,apply_id);



        findViewById(R.id.details_configure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configure(apply_id);
            }
        });
    }


    protected void getInfo(String course_id,String apply_id){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("course_id",course_id)
                .add("apply_id",apply_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/GetDetailsServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplyDetailsActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.body().string();
                JSONObject jsonObject = JSON.parseObject(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(jsonObject.get("flag").toString().equals("true")){
                            Apply apply = JSON.parseObject(jsonObject.getJSONObject("apply").toString(),Apply.class);
                            Course course = JSON.parseObject(jsonObject.getJSONObject("course").toString(),Course.class);
                            courseName.setText("课程名称：" + course.getName());
                            status.setText("申请状态：" + apply.getStatus().toString());
                            note.setText("教师备注" + apply.getNote());
                        }else {
                            Toast.makeText(ApplyDetailsActivity.this, "该申请表不存在！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }



    protected void configure(String apply_id){
        //Log.i("test", status.getText().toString());
        if(status.getText().toString().equals("申请状态：PROCESSING_STAGE2")){
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("apply_id",apply_id)
                    .build();
            Request request = new Request.Builder()
                    .url(DefaultUrl.url + "/ConfigureServlet")
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ApplyDetailsActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String message = response.body().string();
                    JSONObject jsonObject = JSON.parseObject(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(jsonObject.get("flag").toString().equals("true")){
                                Toast.makeText(ApplyDetailsActivity.this, "确认成功！", Toast.LENGTH_SHORT).show();

                                finish();
                            }else {
                                Toast.makeText(ApplyDetailsActivity.this, "该申请表不存在！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }else {
            Toast.makeText(this, "当前申请未审批完成，无法确认，请耐心等候！", Toast.LENGTH_SHORT).show();
        }
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
                Intent intent = new Intent(ApplyDetailsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.exchange:
                // TODO: 切换账号
                Intent intent1 = new Intent(ApplyDetailsActivity.this,WelcomeActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}