package com.app.graduationproject.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/20.
 */

public class Student implements Parcelable{
    //学号，即登陆账号
    private String code;
    //姓名
    private String name;
    //性别
    private String gender;
    //手机号码
    private String phone;
    //邮箱
    private String mail;
    //学院
    private String institute;
    //专业
    private String profession;
    //简介
    private String introduce;
    //密码
    private String password;

    protected Student(Parcel in) {
        code = in.readString();
        name = in.readString();
        gender = in.readString();
        phone = in.readString();
        mail = in.readString();
        institute = in.readString();
        profession = in.readString();
        introduce = in.readString();
        password = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

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
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getInstitute() {
        return institute;
    }
    public void setInstitute(String institute) {
        this.institute = institute;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getIntroduce() {
        return introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Student(){

    }

    public Student(String code, String name, String gender, String phone,
                   String mail, String institute, String profession, String introduce,
                   String password) {
        this.code = code;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.mail = mail;
        this.institute = institute;
        this.profession = profession;
        this.introduce = introduce;
        this.password = password;
    }

    public Student(String code, String name, String gender, String phone,
                   String mail, String institute, String profession, String introduce) {
        super();
        this.code = code;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.mail = mail;
        this.institute = institute;
        this.profession = profession;
        this.introduce = introduce;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeString(phone);
        parcel.writeString(mail);
        parcel.writeString(institute);
        parcel.writeString(profession);
        parcel.writeString(introduce);
        parcel.writeString(password);
    }

    @Override
    public String toString() {
        return "Student{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", institute='" + institute + '\'' +
                ", profession='" + profession + '\'' +
                ", introduce='" + introduce + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}