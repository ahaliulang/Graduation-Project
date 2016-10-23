package com.app.graduationproject.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.app.graduationproject.R;
import com.app.graduationproject.fragment.DetailFirstFragment;
import com.app.graduationproject.fragment.DetailSecondFragment;

/**
 * Created by lenovo on 2016/10/22.
 */
public class VideoDetailActivity extends AppCompatActivity {

    private DetailFirstFragment detailFirstFragment;
    private DetailSecondFragment detailSecondFragment;
    private FragmentManager fragmentManager;//碎片管理器
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        fragmentManager = getSupportFragmentManager();
        setChoice(1);
    }

    public void setChoice(int flag){
        transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if(flag == 1){
            if(detailFirstFragment == null){
                detailFirstFragment = new DetailFirstFragment();
                transaction.add(R.id.video_detail_fragment,detailFirstFragment);
            }else {
                transaction.show(detailFirstFragment);
            }
        }else if(flag ==  2){
            if(detailSecondFragment == null){
                detailSecondFragment = new DetailSecondFragment();
                transaction.add(R.id.video_detail_fragment,detailSecondFragment);
            }else {
                transaction.show(detailSecondFragment);
            }
        }
    }

    private void hideFragments(FragmentTransaction transaction){
        if(detailFirstFragment != null){
            transaction.hide(detailFirstFragment);
        }
        if(detailSecondFragment != null){
            transaction.hide(detailSecondFragment);
        }
    }


}


























