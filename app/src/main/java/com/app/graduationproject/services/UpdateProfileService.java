package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.graduationproject.activity.UpdateProfileActivity;
import com.app.graduationproject.entity.UpdateProfileStatus;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/19.
 */

public class UpdateProfileService extends IntentService{

    private static final String TAG = "UpdateProfileService";


    public static final String ACTION_STATUS = "com.app.graduationproject.services.updataprofile.action_status";
    public static final String EXTRA_STATUS = "status";

    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private LocalBroadcastManager localBroadcastManager;

    private String code,name,gender,phone,mail,institute,profession,introduce;

    public UpdateProfileService() {
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
        name = intent.getStringExtra(UpdateProfileActivity.EXTRA_NAME);
        gender = intent.getStringExtra(UpdateProfileActivity.EXTRA_GENDER);
        phone = intent.getStringExtra(UpdateProfileActivity.EXTRA_PHONE);
        mail = intent.getStringExtra(UpdateProfileActivity.EXTRA_MAIL);
        institute = intent.getStringExtra(UpdateProfileActivity.EXTRA_INSTITUTE);
        profession = intent.getStringExtra(UpdateProfileActivity.EXTRA_PROFESSION);
        introduce = intent.getStringExtra(UpdateProfileActivity.EXTRA_INTRODUCE);

        Log.e(TAG,code+name+gender+phone+mail+institute+profession+introduce);

        int status = -1;

        try {
            Response<UpdateProfileStatus> execute = CloudAPIService.getInstance().updateProfile(code, name, gender, phone, mail, institute, profession, introduce).execute();
            status = execute.body().getStatus();
            Log.e("status",status+"");
        } catch (SocketTimeoutException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.TIMEOUT;
        } catch (UnknownHostException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.UNKNOWN_HOST;
        } catch (IOException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendStatus(status);
    }
    private void sendStatus(int status) {
        Intent broadcast = new Intent(ACTION_STATUS);
        broadcast.putExtra(EXTRA_STATUS,status);
        localBroadcastManager.sendBroadcast(broadcast);
    }
}
























