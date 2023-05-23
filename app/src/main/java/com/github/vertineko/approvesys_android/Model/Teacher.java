package com.github.vertineko.approvesys_android.Model;

public class Teacher {
    private int id;
    private int course_id;
    private String account;
    private String password;
    private String name;
    private String telephone;
    private Role role;

    public Teacher(String account, String password, String name, String telephone, Role role,int course_id) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.telephone = telephone;
        this.role = role;
        this.course_id = course_id;
    }
    public Teacher(){
        super();
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
