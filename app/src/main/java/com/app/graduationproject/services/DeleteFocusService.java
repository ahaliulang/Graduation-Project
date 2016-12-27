package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;

import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/12/27.
 */

public class DeleteFocusService extends IntentService{

    private static final String TAG = "DeleteFocusService";

    private Constants.NETWORK_EXCEPTION mExceptionCode;

    private String code;

    public DeleteFocusService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExceptionCode = Constants.NETWORK_EXCEPTION.DEFAULT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        code = intent.getStringExtra("code");

        try {
            Boolean body = CloudAPIService.getInstance().deleteFocus(code).execute().body();
            while (!body){
                body = CloudAPIService.getInstance().deleteFocus(code).execute().body();
            }
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
