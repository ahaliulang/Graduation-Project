package com.app.graduationproject.net;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lenovo on 2016/10/31.
 */
public class CloudAPIService {

    private static volatile CloudAPI sCloudAPI;
    private static final Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .create();

    private static final Retrofit cloudRetrofit = new Retrofit.Builder()
            .baseUrl(CloudAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static CloudAPI getInstance(){
        if(sCloudAPI == null){
            synchronized (CloudAPIService.class){
                if(sCloudAPI == null){
                    sCloudAPI = cloudRetrofit.create(CloudAPI.class);
                }
            }
        }
        return sCloudAPI;
    }
}
