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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.fragment.BaseFragment;
import com.app.graduationproject.services.DeleteLearnService;
import com.app.graduationproject.services.LearnService;
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
 * Created by Administrator on 2016/12/23.
 */

public class LearnActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        SlideView.OnSlideListener{

    private static final String TAG = "LearnActivity";

    private Toolbar mToolbar;
    private ListViewCompat learnListview;
    private Realm mRealm;
   // private Course course;
  //  private List<Course> courseList;

    private List<CourseItem> mCourseItems;

    private SlideView mLastSlideViewWithStatusOn;

    private String code;//获取已登陆的存储的账号

    private LocalBroadcastManager mLocalBroadcastManager;
    private CodeReceiver codeReceiver;
    private LearnSlideAdapter adapter;

    private TextView delete;


    private SharedPreferences mSharedPreferences; //存储登录的账号

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.learn_list_layout);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        codeReceiver = new CodeReceiver();
        mLocalBroadcastManager.registerReceiver(codeReceiver,new IntentFilter(LearnService.ACTION_CODE));
        mSharedPreferences = this.getSharedPreferences("account",MODE_PRIVATE);
        code = mSharedPreferences.getString("accountId","");  //获取已登陆的存储的账号
        mRealm = Realm.getDefaultInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("正在学习");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = new Intent(LearnActivity.this, LearnService.class);
        intent.putExtra("code",code);
        startService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(codeReceiver);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.e(TAG, "onItemClick position=" + position);
        TextView textView = (TextView) view.findViewById(courseCode);
        String courseCode = textView.getText().toString();
        Intent intent = new Intent(LearnActivity.this,VideoDetailActivity.class);
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

    /**
     * 广播接收来自LearnService发送过来的课程号集合
     */
    private class CodeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<CharSequence> codeList = intent.getCharSequenceArrayListExtra(LearnService.EXTRA_CODE);
            //courseList = new ArrayList<Course>();
            mCourseItems = new ArrayList<>();

            for(int i=0,size = codeList.size();i<size;i++){
                CourseItem courseItem = new CourseItem();
                courseItem.course = Course.fromCode(mRealm,codeList.get(i).toString());
                Log.e("LLO", "onReceive: "+codeList.get(i).toString());
               // courseItem.course = course;
               // courseList.add(course);
                mCourseItems.add(courseItem);
            }

            adapter = new LearnSlideAdapter(LearnActivity.this, R.layout.learn_list_item,mCourseItems);
            for(CourseItem courseItem1:mCourseItems){
                Course course = courseItem1.course;
                Log.e(TAG, "onReceive: "+course.getCode() );
            }

            learnListview = (ListViewCompat) findViewById(R.id.learn_list);
            //learnListview.setAdapter(new TestAdapter(LearnActivity.this,R.layout.learn_list_item));
            learnListview.setAdapter(adapter);
            learnListview.setOnItemClickListener(LearnActivity.this);



            /*learnListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView textView = (TextView) view.findViewById(R.id.courseCode);
                    String courseCode = textView.getText().toString();
                    Log.e("Couse", courseCode);
                    Intent intent = new Intent(LearnActivity.this, VideoDetailActivity.class);
                    intent.putExtra(BaseFragment.EXTRA_COURSE_CODE, courseCode);
                    startActivity(intent);
                    Button button = (Button) view.findViewById(R.id.btnTop);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("Couse", "button");
                        }
                    });
                }
            });*/
         /*   learnListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView  textView = (TextView) findViewById(R.id.tv_text);
                    Log.e("sdsds", "onItemClick: "+ 1221);
                }
            });
*/
        }
    }




    private class LearnSlideAdapter extends android.widget.BaseAdapter{

        private int resId;
        private List<CourseItem> courseItems;
        private Context context;


        public LearnSlideAdapter(Context context, int resId, List<CourseItem> courseItems) {
            super();
            this.context = context;
            this.resId = resId;
            this.courseItems = courseItems;
        }

        @Override
        public int getCount() {
            return courseItems == null?0:courseItems.size();
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
            if(slideView == null){
                View itemView = View.inflate(context, resId, null);
                slideView = new SlideView(context);
                slideView.setContentView(itemView);
                slideView.setOnSlideListener(LearnActivity.this);
                holder = new ViewHolder(slideView);
                slideView.setTag(holder);
            }else {
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
            switch (item.course.getScore()){
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
                    Intent intent = new Intent(LearnActivity.this, DeleteLearnService.class);
                    intent.putExtra("code",courseItems.get(position).course.getCode());
                    Log.e(TAG, "onClick: "+ courseItems.get(position).course.getCode());
                    startService(intent);
                    courseItems.remove(position);
                    notifyDataSetChanged();
                }
            });
            return slideView;
        }
    }

    private static class ViewHolder{
        ImageView thumbnail;
        TextView title;
        TextView courseCode;
        TextView video_clip;
        TextView people;
        TextView score;
        ViewGroup deleteHolder;

        ViewHolder(View view){
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
            courseCode = (TextView) view.findViewById(R.id.courseCode);
            video_clip = (TextView) view.findViewById(R.id.video_clip);
            people = (TextView) view.findViewById(R.id.people);
            score = (TextView) view.findViewById(R.id.score);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
        }
    }
}

























































