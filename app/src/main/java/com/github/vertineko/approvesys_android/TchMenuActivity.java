package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TchMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String teacher_id = getIntent().getStringExtra("teacher_id");
        String teacher_role = getIntent().getStringExtra("teacher_role");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tch_menu);

        findViewById(R.id.menu_approve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TchMenuActivity.this,ApproveActivity.class);
                intent.putExtra("teacher_id",teacher_id);
                intent.putExtra("teacher_role",teacher_role);
                startActivity(intent);
            }
        });

        findViewById(R.id.menu_allapprove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TchMenuActivity.this,AllApproveActivity.class);
                intent.putExtra("teacher_id",teacher_id);
                intent.putExtra("teacher_role",teacher_role);
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
                Intent intent = new Intent(TchMenuActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.exchange:
                // TODO: 切换账号
                Intent intent1 = new Intent(TchMenuActivity.this,WelcomeActivity.class);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}