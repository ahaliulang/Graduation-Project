package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.graduationproject.activity.ChangePwdActivity;
import com.app.graduationproject.entity.ChangePwdStatus;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

/**
 * Created by TAN on 2016/12/11.
 */
public class ChangePwdService extends IntentService {

    private static final String TAG = "ChangePwdService";

    public static final String ACTION_STATUS = "com.app.graduationproject.services.changePwdservice.action_status";
    public static final String EXTRA_STATUS = "status";

    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private LocalBroadcastManager localBroadcastManager;

    private String code; //账号
    private String old_passwordd; //旧密码
    private String new_password; //新密码

    public ChangePwdService() {
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
        code = intent.getStringExtra(ChangePwdActivity.EXTRA_USER);
        old_passwordd = intent.getStringExtra(ChangePwdActivity.EXTRA_OLD_PASSWORD);
        new_password = intent.getStringExtra(ChangePwdActivity.EXTRA_NEW_PASSWORD);
        Log.e("code+password",code+  "=== " +new_password);
        int status = 0;
        Response<ChangePwdStatus> execute = null;
        try {
            execute = CloudAPIService.getInstance().changePwd(code, new_password,old_passwordd).execute();
            status = execute.body().getStatus();
        } catch (SocketTimeoutException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.TIMEOUT;
        } catch (UnknownHostException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.UNKNOWN_HOST;
        } catch (IOException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("status",status+"");
        sendStatus(status);

    }

    private void sendStatus(int status) {
        Intent broadcast = new Intent(ACTION_STATUS);
        broadcast.putExtra(EXTRA_STATUS,status);
        localBroadcastManager.sendBroadcast(broadcast);
    }
}
