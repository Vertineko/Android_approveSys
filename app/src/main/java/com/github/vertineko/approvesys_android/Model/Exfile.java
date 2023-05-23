package com.github.vertineko.approvesys_android.Model;

public class Exfile {
    private int id;
    private byte[] file;
    private String type;


    public Exfile(int id, String type, byte[] file) {
        this.id = id;
        this.file = file;
        this.type = type;
    }
    public Exfile(){
        super();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
