package com.app.graduationproject.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.fragment.BaseFragment;
import com.app.graduationproject.services.DeleteFocusService;
import com.app.graduationproject.services.FocusService;
import com.app.graduationproject.utils.NetWorkUtils;
import com.app.graduationproject.view.CourseItem;
import com.app.graduationproject.view.ListViewCompat;
import com.app.graduationproject.view.SlideView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static com.app.graduationproject.R.id.courseCode;

/**
 * Created by Administrator on 2016/12/27.
 */

public class FocusActivity extends BaseActivity implements
        AdapterView.OnItemClickListener, SlideView.OnSlideListener {

    private static final String TAG = "FocusActivity";

    private Toolbar mToolbar;
    private Realm mRealm;

    private List<CourseItem> mCourseItems;

    private SlideView mLastSlideViewWithStatusOn;

    private String accountId;//获取已登陆的存储的账号

    private LocalBroadcastManager mLocalBroadcastManager;
    private CodeReceiver codeReceiver;
    private FocusSlideAdapter adapter;

    private SharedPreferences mSharedPreferences; //存储登录的账号
    private ProgressBar pb;
    private LinearLayout linearLayout;
    private TextView toAdd;
    private ListViewCompat focusListview;
    private TextView learn_or_focus;
    private TextView tv_error;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.learn_list_layout);

        pb = (ProgressBar) findViewById(R.id.progress_bar);
        linearLayout = (LinearLayout) findViewById(R.id.showTips);
        toAdd = (TextView) findViewById(R.id.toAdd);
        focusListview = (ListViewCompat) findViewById(R.id.learn_list);
        learn_or_focus = (TextView) findViewById(R.id.tv_learn_or_focus);
        tv_error = (TextView) findViewById(R.id.tv_error);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("关注的课程");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAG","1122");
                finish();
            }
        });

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        codeReceiver = new CodeReceiver();
        mLocalBroadcastManager.registerReceiver(codeReceiver,new IntentFilter(FocusService.ACTION_CODE));
        mSharedPreferences = this.getSharedPreferences("account",MODE_PRIVATE);
        accountId = mSharedPreferences.getString("accountId","");  //获取已登陆的存储的账号
        mRealm = Realm.getDefaultInstance();

        /*Intent intent = new Intent(FocusActivity.this, FocusService.class);
        intent.putExtra("code",accountId);
        startService(intent);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkNetwork()) {
            return;
        }
        Intent intent = new Intent(FocusActivity.this, FocusService.class);
        intent.putExtra("code",accountId);
        startService(intent);
        Log.d(TAG, "onResume: ");
    }

    private boolean checkNetwork() {
        if (!NetWorkUtils.isNetworkConnected(getApplicationContext())) {
            pb.setVisibility(View.INVISIBLE);
            //error.setVisibility(View.VISIBLE);
            Toast.makeText(FocusActivity.this, "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
            tv_error.setText("加载失败，请点击重试");
            tv_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_error.setText("正在加载中...");
                    pb.setVisibility(View.VISIBLE);
                    Toast.makeText(FocusActivity.this, "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    checkNetwork();
                }
            });
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(codeReceiver);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView textView = (TextView) view.findViewById(courseCode);
        String courseCode = textView.getText().toString();
        Intent intent = new Intent(FocusActivity.this,VideoDetailActivity.class);
        intent.putExtra(BaseFragment.EXTRA_COURSE_CODE,courseCode);
        startActivity(intent);
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    private static class ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView courseCode;
        TextView video_clip;
        TextView people;
        TextView score;
        ViewGroup deleteHolder;

        ViewHolder(View view) {
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
            courseCode = (TextView) view.findViewById(R.id.courseCode);
            video_clip = (TextView) view.findViewById(R.id.video_clip);
            people = (TextView) view.findViewById(R.id.people);
            score = (TextView) view.findViewById(R.id.score);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
        }
    }



    private class CodeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<CharSequence> codeList = intent.getCharSequenceArrayListExtra(FocusService.EXTRA_CODE);
            Log.d(TAG, "onReceive: " + "test");

            if(codeList.size() == 1 && codeList.get(0).equals("666666")){
                Log.d(TAG, "onReceive: " + codeList.size() + " -- " + codeList.get(0));
                pb.setVisibility(View.GONE);
                tv_error.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                focusListview.setVisibility(View.GONE);
                learn_or_focus.setText("HI,您没有关注的课程");
                toAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toAdd = new Intent(FocusActivity.this,AddCourse.class);
                        startActivity(toAdd);
                    }
                });
                return;
            }else if(codeList.size() > 0){
                Log.d(TAG, "onReceive: " + codeList.size() + " -- " + codeList.get(0));
                pb.setVisibility(View.GONE);
                tv_error.setVisibility(View.GONE);
                mCourseItems = new ArrayList<>();

                for(int i=0,size = codeList.size();i<size;i++){
                    CourseItem courseItem = new CourseItem();
                    courseItem.course = Course.fromCode(mRealm,codeList.get(i).toString());
                    Log.e("LLO", "onReceive: "+codeList.get(i).toString());
                    mCourseItems.add(courseItem);
                }

                adapter = new FocusSlideAdapter(FocusActivity.this, R.layout.learn_list_item,mCourseItems);
                for(CourseItem courseItem1:mCourseItems){
                    Course course = courseItem1.course;
                    Log.e(TAG, "onReceive: "+course.getCode() );
                }
                linearLayout.setVisibility(View.GONE);
                focusListview.setVisibility(View.VISIBLE);
                focusListview.setAdapter(adapter);
                focusListview.setOnItemClickListener(FocusActivity.this);
            }else {
                Log.d(TAG, "onReceive: " + "else");
                Toast.makeText(context, "无法连接服务器，请检查你的网络连接", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.INVISIBLE);
                tv_error.setText("加载失败，请点击重试");
                tv_error.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_error.setText("正在加载中...");
                        pb.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(FocusActivity.this, FocusService.class);
                        intent.putExtra("code",accountId);
                        startService(intent);
                    }
                });

            }

            /*mCourseItems = new ArrayList<>();


            for(int i=0,size = codeList.size();i<size;i++){
                CourseItem courseItem = new CourseItem();
                courseItem.course = Course.fromCode(mRealm,codeList.get(i).toString());
                Log.e("LLO", "onReceive: "+codeList.get(i).toString());
                mCourseItems.add(courseItem);
            }
            pb.setVisibility(View.GONE);
            tv_error.setVisibility(View.GONE);
            if(mCourseItems.size() <= 0){
                linearLayout.setVisibility(View.VISIBLE);
                focusListview.setVisibility(View.GONE);
                learn_or_focus.setText("HI,您没有关注的课程");
                toAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toAdd = new Intent(FocusActivity.this,AddCourse.class);
                        startActivity(toAdd);
                    }
                });
                return;
            }
            adapter = new FocusSlideAdapter(FocusActivity.this, R.layout.learn_list_item,mCourseItems);
            for(CourseItem courseItem1:mCourseItems){
                Course course = courseItem1.course;
                Log.e(TAG, "onReceive: "+course.getCode() );
            }
            linearLayout.setVisibility(View.GONE);
            focusListview.setVisibility(View.VISIBLE);
            focusListview.setAdapter(adapter);
            focusListview.setOnItemClickListener(FocusActivity.this);*/
        }
    }

    private class FocusSlideAdapter extends BaseAdapter {

        private int resId;
        private List<CourseItem> courseItems;
        private Context context;

        public FocusSlideAdapter(Context context, int resId, List<CourseItem> courseItems) {
            super();
            this.context = context;
            this.resId = resId;
            this.courseItems = courseItems;
        }

        @Override
        public int getCount() {
            return courseItems == null ? 0 : courseItems.size();

        }

        @Override
        public Object getItem(int i) {
            return courseItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            SlideView slideView = (SlideView) convertView;
            if (slideView == null) {
                View itemView = View.inflate(context, resId, null);
                slideView = new SlideView(context);
                slideView.setContentView(itemView);
                slideView.setOnSlideListener(FocusActivity.this);
                holder = new ViewHolder(slideView);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }
            CourseItem item = courseItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

            Glide.with(context)
                    .load(Uri.parse(item.course.getImgurl()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.thumbnail);
            holder.title.setText(item.course.getName());
            holder.video_clip.setText(item.course.getStudy());
            holder.people.setText(item.course.getVisit());
            holder.courseCode.setText(item.course.getCode());
            switch (item.course.getScore()) {
                case "5":
                    holder.score.setText("☆☆☆☆☆");
                    break;
                case "4":
                    holder.score.setText("☆☆☆☆");
                    break;
                case "3":
                    holder.score.setText("☆☆☆");
                    break;
                case "2":
                    holder.score.setText("☆☆");
                    break;
                case "1":
                    holder.score.setText("☆");
                    break;
                default:
                    holder.score.setText("☆");
                    break;
            }
            /*holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("SSSSS", "onClick: "+12321);
                }
            });*/
            holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FocusActivity.this, DeleteFocusService.class);
                    intent.putExtra("courseCode", courseItems.get(position).course.getCode());
                    intent.putExtra("studentCode",accountId);
                    startService(intent);
                    courseItems.remove(position);
                    notifyDataSetChanged();
                    if(courseItems.size()<=0){
                        linearLayout.setVisibility(View.VISIBLE);
                        focusListview.setVisibility(View.GONE);
                        toAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent toAdd = new Intent(FocusActivity.this,AddCourse.class);
                                startActivity(toAdd);
                            }
                        });
                    }
                }
            });
            return slideView;
        }
    }
}