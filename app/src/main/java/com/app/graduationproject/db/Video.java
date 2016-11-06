package com.app.graduationproject.db;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lenovo on 2016/10/31.
 */
public class Video extends RealmObject{


    //视频ID
    @PrimaryKey
    @SerializedName("_id")
    private int id;

    //视频名称（每一集的）
    private String name;
    //视频的url
    private String url;
    //视频对应的课程编号
    private String code;



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
