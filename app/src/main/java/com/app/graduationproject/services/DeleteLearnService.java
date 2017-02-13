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

public class DeleteLearnService extends IntentService{

    private static final String TAG = "DeleteLearnService";

    private Constants.NETWORK_EXCEPTION mExceptionCode;


    public DeleteLearnService() {
        super(TAG);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mExceptionCode = Constants.NETWORK_EXCEPTION.DEFAULT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String studentCode = intent.getStringExtra("studentCode");
        String courseCode = intent.getStringExtra("courseCode");

        try {
            Boolean body = CloudAPIService.getInstance().deleteLearn(studentCode,courseCode).execute().body();
            /*Intent intent2 = new Intent(this, LearnService.class);
            intent.putExtra("code",accountId);
            startService(intent2);*/
           /* while (!body){
                body = CloudAPIService.getInstance().deleteLearn(code).execute().body();
            }*/
        }catch (SocketTimeoutException e) {
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
