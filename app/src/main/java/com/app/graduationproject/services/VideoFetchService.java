package com.app.graduationproject.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.graduationproject.db.Video;
import com.app.graduationproject.fragment.BaseFragment;
import com.app.graduationproject.net.CloudAPIService;
import com.app.graduationproject.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

/**
 * Created by lenovo on 2016/11/7.
 */
public class VideoFetchService extends IntentService{

    private static final String TAG = "VideoFetchService";
    private Constants.NETWORK_EXCEPTION mExceptionCode;
    private String courseCode;

    public VideoFetchService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExceptionCode = Constants.NETWORK_EXCEPTION.DEFAULT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        courseCode = intent.getStringExtra(BaseFragment.EXTRA_COURSE_CODE);
        Realm realm = Realm.getDefaultInstance();
        try{
            fetchVideo(realm);
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

    private void fetchVideo(final Realm realm) throws IOException {
        List<Video> result = CloudAPIService.getInstance().videoResult().execute().body();
        int size = result.size();
        for(int i=0;i<size;i++){
            Video video = result.get(i);
            String name = video.getName();
            Log.e("TEST",name);
            if(!saveToDb(realm,result.get(i))){
                return;
            }
        }

    }

    private boolean saveToDb(Realm realm,final Video video){
        realm.beginTransaction();
        try{
            realm.copyToRealm(video);
            Log.e("WHAT",video.getName());
        }catch (RealmPrimaryKeyConstraintException e){
            realm.commitTransaction();
            return true;
        }catch (Exception e){
            Log.e("ERROR",""+video.getId());
            realm.cancelTransaction();
            return false;
        }
        realm.commitTransaction();
        return true;
    }

}
