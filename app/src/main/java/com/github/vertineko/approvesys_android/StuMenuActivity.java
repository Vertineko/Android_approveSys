package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class StuMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_menu);


        String id = getIntent().getStringExtra("id");


        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StuMenuActivity.this,SelectCourseActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StuMenuActivity.this,ApplyList.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StuMenuActivity.this,AllApply.class);
                intent.putExtra("id",id);
                startActivity(intent);
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
                Intent intent = new Intent(StuMenuActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.exchange:
                // TODO: 切换账号
                Intent intent1 = new Intent(StuMenuActivity.this,WelcomeActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}