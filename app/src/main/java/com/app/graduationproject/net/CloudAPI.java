package com.app.graduationproject.net;

import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.CourseDetails;
import com.app.graduationproject.db.Video;
import com.app.graduationproject.entity.ChangePwdStatus;
import com.app.graduationproject.entity.LoginStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by lenovo on 2016/10/31.
 */
public interface CloudAPI {
    //API的url
    String BASE_URL = "http://10.168.122.181:8080/CloudClass_Server/servlet/";

    @GET("CourseServlet")
    Call<List<Course>> Results();

    @GET("CourseDetailServlet")
    Call<List<CourseDetails>> courseDetailsResults();

    @GET("VideoServlet")
    Call<List<Video>> videoResult();

    @FormUrlEncoded
    @POST("LoginServlet")
    Call<LoginStatus> login(@Field("studentCode") String studentCode, @Field("password") String courseCode);

    @FormUrlEncoded
    @POST("UpdatePasswordServlet")
    Call<ChangePwdStatus> changePwd(@Field("account") String studentCode, @Field("newPassword") String newPassword,@Field("oldPassword")String oldPassword);


}














































