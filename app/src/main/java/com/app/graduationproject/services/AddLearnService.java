package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;

import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/2/13.
 */

public class AddLearnService extends IntentService{

    private static final String TAG = "AddLearnService";
    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private String courseCode;
    private String studentCode;

    public AddLearnService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExceptionCode = Constants.NETWORK_EXCEPTION.DEFAULT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        courseCode = intent.getStringExtra("courseCode");
        studentCode = intent.getStringExtra("studentCode");

        try {
            Boolean body = CloudAPIService.getInstance().addLearn(studentCode, courseCode).execute().body();
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
}




























