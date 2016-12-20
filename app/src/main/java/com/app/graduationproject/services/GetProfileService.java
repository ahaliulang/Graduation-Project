package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.app.graduationproject.activity.UpdateProfileActivity;
import com.app.graduationproject.entity.Student;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/20.
 */

public class GetProfileService extends IntentService{

    private static final String TAG = "GetProfileService";


    public static final String ACTION_STUDENT = "com.app.graduationproject.services.getprofile.action_status";
    public static final String KEY = "student";

    private String code; //学号

    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private LocalBroadcastManager localBroadcastManager;


    public GetProfileService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExceptionCode = Constants.NETWORK_EXCEPTION.DEFAULT;
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        code = intent.getStringExtra(UpdateProfileActivity.EXTRA_CODE);
        Student student;
        try {
            Response<Student> execute = CloudAPIService.getInstance().getProfile(code).execute();
            student = execute.body();
          //  Log.e("oooh",student.toString());
            sendStudent(student);
        } catch (SocketTimeoutException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.TIMEOUT;
        } catch (UnknownHostException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.UNKNOWN_HOST;
        } catch (IOException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void sendStudent(Student student) {
        Intent broadcast = new Intent(ACTION_STUDENT);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY,student);
        broadcast.putExtras(bundle);
        localBroadcastManager.sendBroadcast(broadcast);
    }
}
