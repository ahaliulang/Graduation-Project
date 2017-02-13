package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/2/13.
 */

public class FindIfFocusedService extends IntentService{

    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private LocalBroadcastManager localBroadcastManager;
    private String courseCode;
    private String studentCode;

    private static final String TAG = "FindIfFocusedService";

    public static final String ACTION_CODE = "com.app.graduationproject.findiffocusService.action_code";
    public static final String EXTRA_CODE = "focus";

    public FindIfFocusedService() {
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
        courseCode = intent.getStringExtra("courseCode");
        studentCode = intent.getStringExtra("studentCode");

        try {
            Boolean body = CloudAPIService.getInstance().findIfFocused(studentCode, courseCode).execute().body();
            sendResult(body);
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
    private void sendResult(Boolean focused){
        Intent intent = new Intent(ACTION_CODE);
        intent.putExtra(EXTRA_CODE,focused);
        localBroadcastManager.sendBroadcast(intent);
    }
}
