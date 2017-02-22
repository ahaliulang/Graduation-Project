package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.graduationproject.activity.LoginActivity;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;


/**
 * Created by TAN on 2016/11/21.
 */
public class LoginService extends IntentService {

    private static final String TAG = "LoginService";

    public static final String ACTION_STATUS = "com.app.graduationproject.services.action_status";
    public static final String EXTRA_STATUS = "status";

    private String code;
    private String password;

    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private LocalBroadcastManager localBroadcastManager;

    public LoginService() {
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
        code = intent.getStringExtra(LoginActivity.EXTRA_USER);
        password = intent.getStringExtra(LoginActivity.EXTRA_PASSWROD);
        Log.d(TAG, "onHandleIntent: " + code + "--" + password);
        String name="";
        try {
            Response<String> execute = CloudAPIService.getInstance().login(code, password).execute();
            name = execute.body();
        } catch (SocketTimeoutException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.TIMEOUT;
        } catch (UnknownHostException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.UNKNOWN_HOST;
        } catch (IOException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendStatus(name);
    }

    private void sendStatus(String name){
        Intent broadcast = new Intent(ACTION_STATUS);
        broadcast.putExtra(EXTRA_STATUS,name);
        localBroadcastManager.sendBroadcast(broadcast);
    }
}
