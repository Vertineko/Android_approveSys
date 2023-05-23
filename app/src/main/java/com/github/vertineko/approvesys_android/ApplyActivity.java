package com.github.vertineko.approvesys_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Model.Course;
import com.github.vertineko.approvesys_android.Model.User;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApplyActivity extends AppCompatActivity {
    private TextView apply_course_id;
    private TextView apply_course_code;
    private TextView apply_course_name;
    private TextView apply_course_cataloty;
    private TextView apply_course_creadit;
    private TextView apply_user_id;
    private TextView apply_user_name;
    private TextView apply_user_tele;
    private TextView url;
    private EditText reason;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        apply_course_id = findViewById(R.id.apply_course_id);
        apply_course_code = findViewById(R.id.apply_course_code);
        apply_course_cataloty = findViewById(R.id.apply_course_catalory);
        apply_course_name = findViewById(R.id.apply_course_name);
        apply_course_creadit = findViewById(R.id.apply_course_creadit);
        apply_user_id = findViewById(R.id.apply_user_id);
        apply_user_name = findViewById(R.id.apply_user_name);
        apply_user_tele = findViewById(R.id.apply_user_telephone);
        url = findViewById(R.id.url);
        reason = findViewById(R.id.apply_reason);
        String user_id = getIntent().getStringExtra("user_id");
        String course_id = getIntent().getStringExtra("course_id");
        getApplyInfo(user_id,course_id);

        findViewById(R.id.uploadVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(view);
            }
        });

        findViewById(R.id.sendApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!url.getText().equals("")){
                    Uri uri = Uri.parse(url.getText().toString());
                    File file = null;
                    try {

                        String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            file = new File(filePath);
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        sendApply(user_id,course_id,file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //Log.i("test",path);
                }else {
                    Toast.makeText(ApplyActivity.this,"未上传证明文件！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





    public void getApplyInfo(String user_id,String course_id){
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id",user_id)
                .add("course_id",course_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/ApplyInfoServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplyActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.body().string();
                JSONObject jsonObject = JSON.parseObject(message);
                JSONObject jsonCourse = jsonObject.getJSONObject("course");
                JSONObject jsonUser = jsonObject.getJSONObject("user");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(jsonObject.get("flag").toString().equals("true")){
                            Course course = JSON.parseObject(jsonCourse.toString(),Course.class);
                            User user = JSON.parseObject(jsonUser.toString(), User.class);
                            apply_course_id.setText("课程id：   " + course.getId());
                            apply_course_code.setText("课程代码：" + course.getCode());
                            apply_course_name.setText("课程名称：" + course.getName());
                            apply_course_cataloty.setText("课程分类：" + course.getCatalory());
                            apply_course_creadit.setText("课程学分：" + course.getCreadit());
                            apply_user_id.setText("申请人id:   " + user.getId());
                            apply_user_name.setText("申请人姓名：" + user.getName());
                            apply_user_tele.setText("申请人电话：" + user.getTelephone());
                        }else {
                            Toast.makeText(ApplyActivity.this, "该课程已不存在！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    protected void sendApply(String user_id, String course_id,File file) throws IOException{
        //Log.i("test", file.toString());
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType contentType = MediaType.parse("image/*");


//        String data = "";
//        FileReader fileReader = new FileReader(file);
//        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        String line = bufferedReader.readLine();
//        while (line != null) {
//            data += line;
//            line = bufferedReader.readLine();
//        }
//        bufferedReader.close();
//        fileReader.close();
//
//        Log.i("test2", data);



        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id",user_id)
                .addFormDataPart("course_id",course_id)
                .addFormDataPart("image",file.getName(),RequestBody.create(contentType, file))
                .addFormDataPart("reason",reason.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/CreateApplyServlet")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplyActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(ApplyActivity.this,"申请成功！",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ApplyActivity.this,"申请失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // 打开系统的文件选择器
    public void pickFile(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        this.startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            url.setText(uri.toString());

        }
    }

    public String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

}