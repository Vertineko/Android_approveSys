package com.github.vertineko.approvesys_android.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Model.Course;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetCourseTask extends AsyncTask<String, Void, Course> {
    private OnCourseListener mListener;

    public GetCourseTask(OnCourseListener listener) {
        mListener = listener;
    }

    @Override
    protected Course doInBackground(String... params) {
        String course_id = params[0];
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("course_id", course_id)
                .build();
        Request request = new Request.Builder()
                .url(DefaultUrl.url + "/getCourseByIdServlet")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String message = response.body().string();
                JSONObject jsonObject = JSON.parseObject(message);
                if (jsonObject.get("flag").toString().equals("true")) {
                    Course course = JSON.parseObject(jsonObject.getJSONObject("course").toString(), Course.class);
                    Log.i("test", course.getName());
                    return course;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Course course) {
        if (mListener != null) {
            mListener.onCourseReceived(course);
        }
    }

    public interface OnCourseListener {
        void onCourseReceived(Course course);
    }
}