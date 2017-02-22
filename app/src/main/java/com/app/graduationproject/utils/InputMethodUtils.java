package com.app.graduationproject.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ahaliulang on 2017/2/21.
 * 软键盘管理类
 */

public class InputMethodUtils {

    /**
     * 切换软键盘
     * @param context
     */
    public static void toggleKeyboard(Context context){
        //得到InputMethodManager的实例
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            //如果开启,关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void closeKeyboard(Activity activity){
        View view = activity.getWindow().peekDecorView();
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
