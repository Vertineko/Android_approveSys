package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Model.Teacher;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TchLoginActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_login);

        account = findViewById(R.id.tch_login_account);
        password = findViewById(R.id.tch_login_password);

        findViewById(R.id.tch_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(account.getText().toString(),password.getText().toString());
            }
        });
    }

    protected void login(String account , String password){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("account",account)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/TchLoginServlet")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TchLoginActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
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
                            Teacher teacher = JSON.parseObject(JSON.toJSONString(jsonObject.get("teacher")),Teacher.class);
                            Intent intent = new Intent(TchLoginActivity.this,TchMenuActivity.class);
                            intent.putExtra("teacher_id",teacher.getId());
                            intent.putExtra("teacher_role",teacher.getRole().toString());
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(TchLoginActivity.this,"登陆失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}