package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Model.Apply;
import com.github.vertineko.approvesys_android.Model.Course;
import com.github.vertineko.approvesys_android.R;
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

public class ApplyList extends AppCompatActivity {
    private List<Apply> applies;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list);
        String user_id = getIntent().getStringExtra("id");

        listView = findViewById(R.id.course_apply);
        ArrayAdapter<Apply> ApplyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
        listView.setAdapter(ApplyAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isApplyStatus(ApplyAdapter,i,user_id);
            }
        });
        showAllApply(user_id,ApplyAdapter);
    }

    protected void isApplyStatus(ArrayAdapter<Apply> adapter,int position,String user_id){
        String[] data = adapter.getItem(position).toString().split(" ");
        String apply_id = data[0];
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("apply_id",apply_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/IsApplyExistServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplyList.this, "Network Error!", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(ApplyList.this,ApplyDetailsActivity.class);
                            intent.putExtra("user_id",user_id);
                            intent.putExtra("course_id",jsonObject.get("course_id").toString());
                            intent.putExtra("apply_id",apply_id);
                            //Log.i("test",jsonObject.get("course_id").toString() );
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(ApplyList.this, "该申请不存在！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    protected void showAllApply(String user_id,ArrayAdapter<Apply> ApplyAdapter){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id",user_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/ShowProcessingApplyServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplyList.this, "Network Error!", Toast.LENGTH_SHORT).show();
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
                            applies = JSON.parseArray(jsonObject.get("courses").toString(),Apply.class);
                            for(Apply apply : applies){
                                ApplyAdapter.add(apply);
                            }
                        }else{
                            Toast.makeText(ApplyList.this, "当前没有待审批的申请！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}