package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.graduationproject.db.CourseDetails;
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
 * Created by lenovo on 2016/11/5.
 */
public class CourseDetailsService extends IntentService{

    private static final String TAG = "CourseDetailsService";
    private Constants.NETWORK_EXCEPTION mExceptionCode;

    public CourseDetailsService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExceptionCode = Constants.NETWORK_EXCEPTION.DEFAULT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CourseDetails> all = CourseDetails.all(realm);
        try {
            fetchDetails(realm);
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

    private void fetchDetails(final Realm realm) throws IOException {
        List<CourseDetails> result = CloudAPIService.getInstance().courseDetailsResults().execute().body();
        int size = result.size();
        for(int i=0;i<size;i++){
            if(!saveToDb(realm,result.get(i))){
                Log.e("Sum",""+i);
                return;
            }
            Log.e("Sum",""+i);
        }
    }

    private boolean saveToDb(Realm realm, final CourseDetails details) {
        realm.beginTransaction();
        try{
            realm.copyToRealm(details);
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
