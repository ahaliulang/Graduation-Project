package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.graduationproject.db.Learn;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.codes;

/**
 * Created by Administrator on 2016/12/23.
 */

public class LearnService extends IntentService{

    private static final String TAG = "LearnService";

    public static final String ACTION_CODE = "com.app.graduationproject.services.action_code";
    public static final String EXTRA_CODE = "code";

    private LocalBroadcastManager localBroadcastManager;
    private Constants.NETWORK_EXCEPTION mExceptionCode;

    private String code;

    public LearnService() {
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

        code = intent.getStringExtra("code");
        ArrayList<CharSequence> codes = new ArrayList<>();
        try {
            List<Learn> body = CloudAPIService.getInstance().getLearn(code).execute().body();


            for(Learn learn:body){
                codes.add(learn.getClassCode());
            }
            Log.e("TTT",codes.size() + " -" + codes.toString());


        } catch (SocketTimeoutException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.TIMEOUT;
        } catch (UnknownHostException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.UNKNOWN_HOST;
        } catch (IOException e) {
            mExceptionCode = Constants.NETWORK_EXCEPTION.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendCode(codes);
    }

    private void sendCode(ArrayList<CharSequence> codeList){
        Intent broadcast = new Intent(ACTION_CODE);
        broadcast.putCharSequenceArrayListExtra(EXTRA_CODE,codeList);
        localBroadcastManager.sendBroadcast(broadcast);
    }
}
