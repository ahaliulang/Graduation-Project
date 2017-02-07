package com.app.graduationproject.net;

import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.CourseDetails;
import com.app.graduationproject.db.Focus;
import com.app.graduationproject.db.Learn;
import com.app.graduationproject.db.Video;
import com.app.graduationproject.entity.ChangePwdStatus;
import com.app.graduationproject.entity.Student;
import com.app.graduationproject.entity.UpdateProfileStatus;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by lenovo on 2016/10/31.
 */
public interface CloudAPI {
    //API的url 123.207.246.137 -- 192.168.0.35:8080
    String BASE_URL = "http://123.207.246.137/CloudClass_Server/servlet/";

    @GET("CourseServlet")
    Call<List<Course>> Results();

    @GET("CourseDetailServlet")
    Call<List<CourseDetails>> courseDetailsResults();

    @GET("VideoServlet")
    Call<List<Video>> videoResult();


    @FormUrlEncoded
    @POST("LoginServlet")
    Call<String> login(@Field("studentCode") String studentCode, @Field("password") String courseCode);

    @FormUrlEncoded
    @POST("UpdatePasswordServlet")
    Call<ChangePwdStatus> changePwd(@Field("account") String studentCode, @Field("newPassword") String newPassword,@Field("oldPassword")String oldPassword);

    @FormUrlEncoded
    @POST("UpdateProfileServlet")
    Call<UpdateProfileStatus> updateProfile(@Field("account") String studentCode, @Field("name") String name, @Field("gender")String gender,
                                            @Field("phone") String phone,@Field("mail") String mail,@Field("institute") String institute,
                                            @Field("profession") String profession,@Field("introduce") String introduce);

    @FormUrlEncoded
    @POST("GetProfileServlet")
    Call<Student> getProfile(@Field("account")String code);

    @FormUrlEncoded
    @POST("FocusServlet")
    Call<List<Focus>> getFocus(@Field("studentCode") String studentCode);

    @FormUrlEncoded
    @POST("LearnServlet")
    Call<List<Learn>> getLearn(@Field("studentCode") String studentCode);

    @FormUrlEncoded
    @POST("DeleteLearnServlet")
    Call<Boolean> deleteLearn(@Field("courseCode") String courseCode);

    @FormUrlEncoded
    @POST("DeleteFocusServlet")
    Call<Boolean> deleteFocus(@Field("courseCode") String courseCode);

    //上传图像
    @Multipart
    @POST("UploadShipServlet")
    Call<String> uploadImage(@Part MultipartBody.Part file);

}














































