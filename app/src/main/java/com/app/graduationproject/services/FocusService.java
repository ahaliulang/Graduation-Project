package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.graduationproject.db.Focus;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class FocusService extends IntentService{

    private static final String TAG = "FocusService";

    public static final String ACTION_CODE = "com.app.graduationproject.focusservice.action_code";
    public static final String EXTRA_CODE = "code";

    private LocalBroadcastManager localBroadcastManager;
    private Constants.NETWORK_EXCEPTION mExceptionCode;

    private String code;

    public FocusService() {
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

        Log.d(TAG, "onHandleIntent: " + "get" + intent.getStringExtra("code"));

        code = intent.getStringExtra("code");
        ArrayList<CharSequence> codes = new ArrayList<>();

        try {
            List<Focus> body = CloudAPIService.getInstance().getFocus(code).execute().body();

            for(Focus focus:body){
                codes.add(focus.getClassCode());
            }
            Log.d(TAG, "onHandleIntent: " + codes.size() + " -" + codes.toString());
            Log.e("TAG",codes.size() + " -" + codes.toString());

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
