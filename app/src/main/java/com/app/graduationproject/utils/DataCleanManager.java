package com.app.graduationproject.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/2/14.
 * 本应用数据清楚管理器
 */

public class DataCleanManager {

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     * @param context
     */
    public static void cleanInternalCache(Context context){
        deleteFiles(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库
     * @param context
     */
    public static void cleanDatabases(Context context){
        deleteFiles(new File("/data/data" + context.getPackageName() + "/databases"));
    }

    /**
     * 清楚本应用SharedPreferences(/data/data/com.xxx.xxx.shared_prefs)
     * @param context
     */
    public static void cleanSharedPreference(Context context){
        deleteFiles(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清楚本应用数据库
     * @param context
     * @param dbName
     */
    public static void cleanDatabaseByName(Context context,String dbName){
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     * @param context
     */
    public static void cleanFiles(Context context){
        deleteFiles(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(mnt/sdcard/android/data/com.xxx.xxx/cache)
     * @param context
     */
    public static void cleanExternalCache(Context context){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            deleteFiles(context.getExternalCacheDir());
        }
    }

    /**
     * 清楚自定义路径下的文件，使用需小心，不要误删。而且只支持目录下的文件删除
     * @param filePath
     */
    public static void cleanCustomCache(String filePath){
        deleteFiles(new File(filePath));
    }

    public static void cleanApplicationData(Context context){
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }





    /**
     * 删除方法
     * @param file
     */
    private static void deleteFiles(File file){
        if(file != null && file.exists()){
            if(file.isFile()){
                file.delete();
            }
            if(file.isDirectory()){
                for(File item : file.listFiles()){
                    item.delete();
                }
            }
        }
    }
}

































