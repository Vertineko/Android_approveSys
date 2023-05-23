package com.github.vertineko.approvesys_android.Model;

public class Course {
    private int id;
    private String code;
    private String name;
    private String catalory;
    private int creadit;


    public Course(String code, String name, String catalory, int creadit) {

        this.code = code;
        this.name = name;
        this.catalory = catalory;
        this.creadit = creadit;

    }

    public Course(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalory() {
        return catalory;
    }

    public void setCatalory(String catalory) {
        this.catalory = catalory;
    }

    public int getCreadit() {
        return creadit;
    }

    public void setCreadit(int creadit) {
        this.creadit = creadit;
    }
    @Override
    public String toString(){
        return getId() + " " + getCode() + " " + getName();
    }
}
