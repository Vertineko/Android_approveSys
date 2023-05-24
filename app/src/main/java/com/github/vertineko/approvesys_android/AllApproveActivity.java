package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Model.Apply;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AllApproveActivity extends AppCompatActivity {

    private EditText name;
    private EditText status;
    private TextView PageInfo;
    private ListView listView;
    private int pageNum = 3;
    private int pageNow = 1;
    private int pageAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_approve);

        String teacher_id = getIntent().getStringExtra("teacher_id");


        name = findViewById(R.id.approveName);
        status = findViewById(R.id.approveStatus);


        PageInfo = findViewById(R.id.pageinfo);
        listView = findViewById(R.id.allapprove);
        ArrayAdapter<Apply> ApplyAdapter = new ArrayAdapter<Apply>(this, android.R.layout.simple_list_item_single_choice);
        listView.setAdapter(ApplyAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        getPageAll(teacher_id);
        showApply(ApplyAdapter,teacher_id,pageNum+"",pageNow+"");


        //上一页
        findViewById(R.id.before).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageNow == 1){
                    Toast.makeText(AllApproveActivity.this, "当前已无上一页！", Toast.LENGTH_SHORT).show();
                }else {
                    pageNow = pageNow - 1;
                    PageInfo.setText("当前" + pageNow + "/共" + pageAll + "页");
                    showApply(ApplyAdapter,teacher_id,pageNum+"",pageNow+"");
                }
            }
        });
        //下页
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageNow > pageAll){
                    pageNow = pageAll;
                    PageInfo.setText("当前" + pageNow + "/共" + pageAll + "页");
                    showApply(ApplyAdapter,teacher_id,pageNum+"",pageNow+"");
                }else {
                    if(pageNow == pageAll){
                        Toast.makeText(AllApproveActivity.this, "已经是最后一页！", Toast.LENGTH_SHORT).show();
                    }else {
                        pageNow += 1;
                        PageInfo.setText("当前" + pageNow + "/共" + pageAll + "页");
                        showApply(ApplyAdapter,teacher_id,pageNum+"",pageNow+"");
                    }
                }
            }
        });

        findViewById(R.id.approve_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNum = 3;
                pageNow = 1;
                PageInfo.setText("当前" + pageNow + "/共" + pageAll + "页");
                PageInfo.setVisibility(View.VISIBLE);
                findViewById(R.id.next).setVisibility(View.VISIBLE);
                findViewById(R.id.before).setVisibility(View.VISIBLE);
                showApply(ApplyAdapter,teacher_id,pageNum+"",pageNow+"");
            }
        });

        findViewById(R.id.approve_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageInfo.setVisibility(View.INVISIBLE);
                findViewById(R.id.next).setVisibility(View.INVISIBLE);
                findViewById(R.id.before).setVisibility(View.INVISIBLE);
                search(ApplyAdapter,name.getText().toString(),status.getText().toString(),teacher_id);
            }
        });
    }

    protected void showApply(ArrayAdapter<Apply> ApplyAdapter,String user_id,String pageNum,String pageNow){
        ApplyAdapter.clear();
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("teacher_id",user_id)
                .add("pageNum",pageNum)
                .add("pageNow",pageNow)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/GetApprovePageServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败情况
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AllApproveActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 处理请求成功情况
                String message = response.body().string();
                Log.i("test", message);
                JSONObject jsonObject = JSON.parseObject(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(jsonObject.get("flag").toString().equals("true")){
                            List<Apply> applies = JSON.parseArray(JSON.toJSONString(jsonObject.get("applies")),Apply.class);
                            for (Apply apply : applies){
                                ApplyAdapter.add(apply);
                            }
                        }else {
                            Toast.makeText(AllApproveActivity.this, "您暂时没有任何申请！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    protected void getPageAll(String user_id){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("teacher_id",user_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/GetApproveCountServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AllApproveActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
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
                            pageAll = Integer.parseInt(jsonObject.get("count").toString())/pageNum + 1;
                            PageInfo.setText("当前" + pageNow + "/共" + pageAll + "页");
                        }else {
                            pageAll = 0;
                            Toast.makeText(AllApproveActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    protected void search(ArrayAdapter<Apply> ApplyAdapter,String name,String status,String user_id){
        ApplyAdapter.clear();
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("teacher_id",user_id)
                .add("name",name)
                .add("status",status)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/TchServletServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AllApproveActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
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
                            List<Apply> applies = JSON.parseArray(JSON.toJSONString(jsonObject.get("applies")),Apply.class);
                            for (Apply apply : applies){
                                ApplyAdapter.add(apply);
                            }
                        }else {
                            Toast.makeText(AllApproveActivity.this, "无搜索结果，请更换关键字后再试一次！", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AllApproveActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.exchange:
                // TODO: 切换账号
                Intent intent1 = new Intent(AllApproveActivity.this,WelcomeActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}