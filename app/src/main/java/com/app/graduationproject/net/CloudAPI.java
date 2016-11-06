package com.app.graduationproject.net;

import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.CourseDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lenovo on 2016/10/31.
 */
public interface CloudAPI {
    //API的url
    String BASE_URL = "http://123.207.246.137/CloudClass_Server/servlet/";

    @GET("CourseServlet")
    Call<List<Course>> Results();

    @GET("CourseDetailServlet")
    Call<List<CourseDetails>> courseDetailsResults();



}














































