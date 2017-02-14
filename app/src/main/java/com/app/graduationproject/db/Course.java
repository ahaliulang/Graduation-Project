package com.app.graduationproject.db;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lenovo on 2016/10/31.
 */
public class Course extends RealmObject{

    //课程编号，唯一
    @PrimaryKey
    private String code;

    //课程名字
    private String name;

    //课程评分，5星制
    private String score;

    //学习人数
    private String study;

    //访问人数
    private String visit;

    //讲师
    private String teacher;

    //内容概要
    private String introduce;

    private String imgurl;

    public Course(){
    }
    public Course(String code, String name, String score, String study, String visit,
                  String teacher, String introduce, String imgurl) {
        this.code = code;
        this.name = name;
        this.score = score;
        this.study = study;
        this.visit = visit;
        this.teacher = teacher;
        this.introduce = introduce;
        this.imgurl = imgurl;
    }

    public static RealmResults<Course> all(Realm realm){
        return realm.where(Course.class)
                .findAllSorted("code", Sort.DESCENDING);
    }

    public static Course fromCode(Realm realm,String code){
        return realm.where(Course.class).equalTo("code",code).findFirst();
    }

    public static RealmResults<Course> fromNameKey(Realm realm, String key){
        return realm.where(Course.class).contains("name",key).findAll();
    }

    public static RealmResults<Course> fromTeacherKey(Realm realm,String key){
        return realm.where(Course.class).contains("teacher",key).findAll();
    }

    public static void clearAll(Realm realm){
        final RealmResults<Course> allCourse = realm.where(Course.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allCourse.deleteAllFromRealm();
            }
        });
    }




    public String getImgurl() {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    public String getStudy() {
        return study;
    }
    public void setStudy(String study) {
        this.study = study;
    }
    public String getVisit() {
        return visit;
    }
    public void setVisit(String visit) {
        this.visit = visit;
    }
    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public String getIntroduce() {
        return introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
