package com.app.graduationproject.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.graduationproject.R;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.Video;
import com.app.graduationproject.fragment.BaseFragment;
import com.app.graduationproject.fragment.DetailFirstFragment;
import com.app.graduationproject.fragment.DetailSecondFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;

/**
 * Created by lenovo on 2016/10/22.
 */
public class VideoDetailActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ImageView play,bg;
    private DetailFirstFragment detailFirstFragment;
    private DetailSecondFragment detailSecondFragment;
    private FragmentManager fragmentManager;//碎片管理器
    private FragmentTransaction transaction;
    private Realm mRealm;
    private Video video;
    private Course course;

    private String course_code;

    private static final String TAG = "VideoDetailActivity";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.activity_video_detail);

        course_code = getIntent().getStringExtra(BaseFragment.EXTRA_COURSE_CODE);
        mRealm = Realm.getDefaultInstance();

        video = Video.fromCode(mRealm, course_code).first();
        course = Course.fromCode(mRealm,course_code);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("视频详情");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fragmentManager = getSupportFragmentManager();
        play = (ImageView) findViewById(R.id.video_paly);
        bg = (ImageView) findViewById(R.id.video_detail_iv);

        Glide.with(this).load(Uri.parse(course.getImgurl()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bg);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoDetailActivity.this,ShowVideoActivity.class);
                intent.putExtra(ShowVideoActivity.EXTRA_VIDEO_URL,
                        video.getUrl());
                startActivity(intent);
            }
        });
        setMyChoice(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public void setMyChoice(int flag){
        transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if(flag == 1){
           // if(detailFirstFragment == null){
                detailFirstFragment = new DetailFirstFragment();

          // }else {
                transaction.replace(R.id.video_detail_fragment,detailFirstFragment);
                //transaction.show(detailFirstFragment);
          //  }
        }else if(flag ==  2){
            //if(detailSecondFragment == null){
                detailSecondFragment = new DetailSecondFragment();//}
           // }else {
                //transaction.show(detailSecondFragment);
                // }
                transaction.replace(R.id.video_detail_fragment,detailSecondFragment);
                transaction.addToBackStack(null);


        }

        transaction.commit();//提交事物
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


























