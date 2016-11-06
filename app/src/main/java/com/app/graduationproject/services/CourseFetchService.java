package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.graduationproject.db.Course;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by lenovo on 2016/10/31.
 */
public class CourseFetchService extends IntentService {

    public static final String ACTION_UPDATE_RESULT = "com.app.graduationproject.services.update_result";
    public static final String EXTRA_FETCHED = "fetched";
    public static final String EXTRA_TRIGGER = "trigger";
    public static final String EXTRA_EXCEPTION_CODE = "exception_code";
    public static final String ACTION_FETCH_REFRESH = "com.app.graduationproject.services.fetch_refresh";
    public static final String ACTION_FETCH_MORE = "com.app.graduationproject.services.fetch_more";


    private static final String TAG = "CourseFetchService";

    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private LocalBroadcastManager localBroadcastManager;

    public CourseFetchService() {
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
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Course> latest = Course.all(realm);

        int fetched = 0;

        try{
            if(latest.isEmpty()){
                fetched = fetchLatest(realm);
            }else if(ACTION_FETCH_REFRESH.equals(intent.getAction())){
                fetched = fetchRefresh(realm);
            }else if(ACTION_FETCH_MORE.equals(intent.getAction())){
                fetched = fetchMore(realm);
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
        sendResult(intent,realm,fetched);
    }

    private void sendResult(Intent intent,Realm realm,int fetched){
        realm.close();
        Intent broadcast = new Intent(ACTION_UPDATE_RESULT);
        broadcast.putExtra(EXTRA_FETCHED,fetched)
                .putExtra(EXTRA_TRIGGER,intent.getAction())
                .putExtra(EXTRA_EXCEPTION_CODE,mExceptionCode);
        localBroadcastManager.sendBroadcast(broadcast);
    }

    private int fetchLatest(final Realm realm) throws IOException {
        List<Course> result = CloudAPIService.getInstance().Results().execute().body();
        int courseSize = result.size();
        for(int i=0;i<courseSize;i++){
            if(!saveToDb(realm,result.get(i))){
                Log.d("TAN1",""+i );
                return i;
            }
            Log.d("TAN2",""+i );
        }


        return courseSize;
    }



    private int fetchRefresh(final Realm realm) throws IOException {
        return fetchLatest(realm);
    }

    private int fetchMore(final Realm realm) throws IOException {
        return 0;
    }





    private boolean saveToDb(Realm realm, final Course course) {
        realm.beginTransaction();
        try{
            realm.copyToRealm(course);
        }catch (RealmPrimaryKeyConstraintException e){
            realm.commitTransaction();
            return true;
        } catch (Exception e){
            realm.cancelTransaction();
            return false;
        }
        realm.commitTransaction();
        return true;
    }



}
















































