package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ApproveActivity extends AppCompatActivity {


    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        listView = findViewById(R.id.approveList);
        String teacher_id = getIntent().getStringExtra("teacher_id");
        String teacher_role = getIntent().getStringExtra("teacher_role");
        ArrayAdapter<Apply> ApplyAdapter = new ArrayAdapter<Apply>(this, android.R.layout.simple_list_item_single_choice);
        listView.setAdapter(ApplyAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getApprove(ApplyAdapter,teacher_id,teacher_role);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] data = ApplyAdapter.getItem(i).toString().split(" ");
                String apply_id = data[0];
                Intent intent = new Intent(ApproveActivity.this,ApproveDetailsActivity.class);
                intent.putExtra("apply_id",apply_id);
                intent.putExtra("teacher_id",teacher_id);
                intent.putExtra("teacher_role",teacher_role);
                startActivity(intent);
            }
        });

    }

    protected void getApprove(ArrayAdapter<Apply> ApplyAdapter,String teacher_id,String teacher_role){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("teacher_id",teacher_id)
                .add("teacher_role",teacher_role)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/GetApproveServlet")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApproveActivity.this,"Network Error!", Toast.LENGTH_SHORT).show();
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
                        Log.i("test", message);
                        if(jsonObject.get("flag").toString().equals("true")){
                            List<Apply> applies = JSON.parseArray(JSON.toJSONString(jsonObject.get("applies")),Apply.class);
                            for(Apply apply : applies){
                                ApplyAdapter.add(apply);
                            }
                        }else {
                            Toast.makeText(ApproveActivity.this,"无待审批的申请！",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(ApproveActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.exchange:
                // TODO: 切换账号
                Intent intent1 = new Intent(ApproveActivity.this,WelcomeActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}