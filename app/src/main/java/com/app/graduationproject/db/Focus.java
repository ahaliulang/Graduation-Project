package com.app.graduationproject.db;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2016/12/23.
 */

public class Focus extends RealmObject{

    //课程号
    private String classCode;
    //学号
    private String sutCode;

    public Focus() {
    }

    public Focus(String classCode, String sutCode) {
        this.classCode = classCode;
        this.sutCode = sutCode;
    }

    public String getClassCode() {
        return classCode;
    }
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
    public String getSutCode() {
        return sutCode;
    }
    public void setSutCode(String sutCode) {
        this.sutCode = sutCode;
    }
}
