package com.app.graduationproject.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lenovo on 2016/10/31.
 */
public class CourseDetails extends RealmObject{

    @PrimaryKey

    private int id;

    //开课单位
    private String agency;
    //课程学时
    private String period;
    //课程类别
    private String category;
    //课程类型
    private String type;
    //学科门类
    private String subject;
    //专业类
    private String profession;
    //专业
    private String conctrte_prof;
    //适用专业
    private String target;
    //课程号
    private String code;

    //无参构造函数
    public CourseDetails() {
    }

    //有参构造函数


    public CourseDetails(int id, String agency, String period, String category,
                         String type, String subject, String profession, String conctrte_prof,
                         String target, String code) {
        this.id = id;
        this.agency = agency;
        this.period = period;
        this.category = category;
        this.type = type;
        this.subject = subject;
        this.profession = profession;
        this.conctrte_prof = conctrte_prof;
        this.target = target;
        this.code = code;
    }

    public static CourseDetails fromCode(Realm realm,String code){
        return realm.where(CourseDetails.class).equalTo("code",code).findFirst();
    }

    public static RealmResults<CourseDetails> all(Realm realm){
        return realm.where(CourseDetails.class).findAll();
    }


    //清除数据库中的数据
    public static void clearCourseDetail(final Context context, Realm realm){

        //查询出数据库中的所有课程细节的信息
        final RealmResults<CourseDetails> courseDetailses = realm.where(CourseDetails.class).findAll();
        //使用交易
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //删除数据库中的所有课程细节信息
                courseDetailses.deleteAllFromRealm();
            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getConctrte_prof() {
        return conctrte_prof;
    }

    public void setConctrte_prof(String conctrte_prof) {
        this.conctrte_prof = conctrte_prof;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


























