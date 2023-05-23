package com.github.vertineko.approvesys_android.Model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vertineko.approvesys_android.Utils.DefaultUrl;
import com.github.vertineko.approvesys_android.Utils.GetCourseTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Apply {

    private int course_id;
    private Status status;
    private int teacher1_id;
    private int teacher2_id;
    private int user_id;
    private String reason;
    private String note;
    private int id;



    public Apply(int course_id, Status status, int teacher1_id, int teacher2_id, int user_id, String reason,String note) {
        this.course_id = course_id;
        this.status = status;
        this.teacher1_id = teacher1_id;
        this.teacher2_id = teacher2_id;
        this.user_id = user_id;
        this.reason = reason;
        this.note = note;
    }

    public Apply(){
        super();
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTeacher1_id() {
        return teacher1_id;
    }

    public void setTeacher1_id(int teacher1_id) {
        this.teacher1_id = teacher1_id;
    }

    public int getTeacher2_id() {
        return teacher2_id;
    }

    public void setTeacher2_id(int teacher2_id) {
        this.teacher2_id = teacher2_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    @Override
    public String toString(){
        return getId() + " " + getCourse_id() + " " + getStatus();
    }


}
