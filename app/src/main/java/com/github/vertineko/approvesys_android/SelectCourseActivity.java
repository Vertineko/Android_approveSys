package com.github.vertineko.approvesys_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.github.vertineko.approvesys_android.Model.Course;
import com.github.vertineko.approvesys_android.R;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelectCourseActivity extends AppCompatActivity {

    private List<Course> courses;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);
        String user_id = getIntent().getStringExtra("id");

        listView = findViewById(R.id.course_select);
        ArrayAdapter<Course> CourseAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_list_item_single_choice);
        listView.setAdapter(CourseAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(SelectCourseActivity.this,CourseAdapter.getItem(i).toString(),Toast.LENGTH_SHORT).show();
                isApplyexist(CourseAdapter,i,user_id);
            }
        });
        showAllCourses(CourseAdapter);
    }

    protected void showAllCourses(ArrayAdapter<Course> CourseAdapter){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/GetCoursesServlet")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SelectCourseActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
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
                            //Log.i("Sdasdadasdasdasdasda",jsonObject.get("courses").toString() );
                            courses = JSON.parseArray(jsonObject.get("courses").toString(),Course.class);
                            for (Course course : courses){
                                //Log.i("Sdasdadasdasdasdasda", course.getName());
                                CourseAdapter.add(course);
                            }
                        }
                    }
                });
            }
        });
    }


    protected void isApplyexist(ArrayAdapter<Course> CourseAdapter ,int position,String user_id){
        String text = CourseAdapter.getItem(position).toString();
        String[] data = text.split(" ");
        String course_id = data[0];
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("course_id",course_id)
                .add("user_id",user_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/isApplyExisitServlet")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SelectCourseActivity.this,"Network Error!",Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(SelectCourseActivity.this,ApplyActivity.class);
                            intent.putExtra("user_id",user_id);
                            intent.putExtra("course_id",course_id);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SelectCourseActivity.this,"当前已有该课程申请在进行中或是已完成，无法重复申请！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}