package com.github.vertineko.approvesys_android.Model;

public class User {
    private int id;
    private String name;
    private String telephone;

    public User( String name, String telephone, String account, String password) {

        this.name = name;
        this.telephone = telephone;
        this.account = account;
        this.password = password;
    }
    public User(){
        super();
    }
    private String account;
    private String password;

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

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }
}
