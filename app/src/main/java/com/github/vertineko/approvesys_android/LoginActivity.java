package com.github.vertineko.approvesys_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText Account;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_login);

        Account = findViewById(R.id.editTextTextPersonName);
        Password = findViewById(R.id.editTextTextPassword);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = Account.getText().toString();
                String password = Password.getText().toString();

                ///okhttp使用

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("account", account)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder()
                        .url(DefaultUrl.url + "/StuLoginServlet")
                        .post(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String message = response.body().string();
                        //Log.i("sdawdafasdqaw", "onResponse: "+message);
                        JSONObject jsonObject = JSON.parseObject(message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText();
                                if(jsonObject.get("flag").toString().equals("true")){
                                    Toast.makeText(LoginActivity.this,"登录成功!",Toast.LENGTH_SHORT).show();
                                    String id = jsonObject.get("id").toString();
                                    Intent intent = new Intent(LoginActivity.this,StuMenuActivity.class);
                                    //当前登陆学生的id
                                    intent.putExtra("id",id);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(LoginActivity.this,"登陆失败!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });
    }
}

