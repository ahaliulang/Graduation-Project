package com.app.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.app.graduationproject.R;
import com.app.graduationproject.fragment.DetailFirstFragment;
import com.app.graduationproject.fragment.DetailSecondFragment;

/**
 * Created by lenovo on 2016/10/22.
 */
public class VideoDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView play;
    private DetailFirstFragment detailFirstFragment;
    private DetailSecondFragment detailSecondFragment;
    private FragmentManager fragmentManager;//碎片管理器
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("视频详情");
        setSupportActionBar(mToolbar);


        fragmentManager = getSupportFragmentManager();
        play = (ImageView) findViewById(R.id.video_paly);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoDetailActivity.this,ShowVideoActivity.class);
                startActivity(intent);
            }
        });
        setChoice(1);
    }

    public void setChoice(int flag){
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


























