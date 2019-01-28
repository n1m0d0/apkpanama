package com.example.john_pc.prueba;

import java.io.File;

public class obj_file {
    protected int id;
    protected String path;
    protected File file;

    public obj_file(int id, String path, File file) {

        this.id = id;
        this.path = path;
        this.file = file;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {

        return file;

    }

    public void setFile(File file) {

        this.file = file;

    }

}
