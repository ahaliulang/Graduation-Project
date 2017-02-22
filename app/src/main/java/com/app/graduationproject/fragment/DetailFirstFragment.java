package com.app.graduationproject.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.LoginActivity;
import com.app.graduationproject.activity.ShowVideoActivity;
import com.app.graduationproject.activity.VideoDetailActivity;
import com.app.graduationproject.adapter.VideoDetailAdapter;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.CourseDetails;
import com.app.graduationproject.db.Video;
import com.app.graduationproject.services.AddFocusService;
import com.app.graduationproject.services.AddLearnService;
import com.app.graduationproject.services.DeleteFocusService;
import com.app.graduationproject.services.DeleteLearnService;
import com.app.graduationproject.services.FindIfFocusedService;
import com.app.graduationproject.services.FindIfLearnedService;
import com.app.graduationproject.utils.NetWorkUtils;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by lenovo on 2016/10/23.
 */
public class DetailFirstFragment extends Fragment {


    private Button more;   //查看更多按钮
    private RecyclerView mRecyclerView; //显示的部分课程列表
    private Button loadingList;
    private static Button follow;
    private static Button start;
    private TextView content;
    private Course course;
    private CourseDetails details;
    private RealmResults<Video> videoList;
    private Realm mRealm;
    private TextView title;
    private TextView score;
    private TextView study_number;
    private TextView visit_number;
    private TextView video_detail_desc;
    private TextView teacher;
    private TextView agency, period, category, type, subject, profession, concrete_prof, target;
    private static Boolean focus;
    private static Boolean learn;
    private LocalBroadcastManager mLocalBroadcastManager;
    private LearnAndFocusReceiver receiver;
    private SharedPreferences mSharedPreferences;
    private boolean ishow;

    private static final String TAG = "DetailFirstFragment";


    private String course_code;
    private String accountId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        mSharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        course_code = getActivity().getIntent().getStringExtra(BaseFragment.EXTRA_COURSE_CODE);
        ishow = mSharedPreferences.getBoolean("show", false); //是否登陆
        accountId = mSharedPreferences.getString("accountId", ""); //获取登陆Id
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        receiver = new LearnAndFocusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(FindIfFocusedService.ACTION_CODE);
        filter.addAction(FindIfLearnedService.ACTION_CODE);
        mLocalBroadcastManager.registerReceiver(receiver, filter);


        Log.d(TAG, "onCreate: " + "创建");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + " F");
        View view = inflater.inflate(R.layout.video_detail_first_fragment, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onDestroy() {
        mLocalBroadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (ishow) {
            Intent focus = new Intent(getActivity(), FindIfFocusedService.class);
            focus.putExtra("studentCode", accountId);
            focus.putExtra("courseCode", course_code);
            getActivity().startService(focus);
            Intent learn = new Intent(getActivity(), FindIfLearnedService.class);
            learn.putExtra("studentCode", accountId);
            learn.putExtra("courseCode", course_code);
            getActivity().startService(learn);
        }
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        course = Course.fromCode(mRealm, course_code);
        details = CourseDetails.fromCode(mRealm, course_code);
        //videoList = Video.fromCode(mRealm,course_code);
        videoList = Video.fromCode(mRealm, course_code);
        for (int i = 0; i < videoList.size(); i++) {
            Video video = videoList.get(i);
            Log.e("MYMY", video.getName());
        }
        if (course != null) {
            title.setText(course.getName());
            score.setText(course.getScore());
            //从Java7开始，switch开始支持String
            switch (course.getScore()) {
                case "5":
                    score.setText("☆☆☆☆☆");
                    break;
                case "4":
                    score.setText("☆☆☆☆");
                    break;
                case "3":
                    score.setText("☆☆☆");
                    break;
                case "2":
                    score.setText("☆☆");
                    break;
                case "1":
                    score.setText("☆");
                    break;
                default:
                    score.setText("☆");
                    break;
            }
            study_number.setText(course.getStudy());
            visit_number.setText(course.getVisit());
            teacher.setText("讲师：" + course.getTeacher());
            video_detail_desc.setText(course.getIntroduce());
            mRecyclerView.setAdapter(initAdapter());
        }
        if (details != null) {
            //agency,period,category,type,subject,profession,concrete_prof,target
            agency.setText("开课单位：" + details.getAgency());
            period.setText("课程学时：" + details.getPeriod());
            category.setText("课程类别：" + details.getCategory());
            type.setText("课程类型：" + details.getType());
            subject.setText("学科门类：" + details.getSubject());
            profession.setText("专业类：" + details.getProfession());
            concrete_prof.setText("专业：" + details.getConctrte_prof());
            target.setText("适用专业：" + details.getTarget());
        }


    }

    private void initView(View view) {
        content = (TextView) view.findViewById(R.id.video_detail_desc);
        more = (Button) view.findViewById(R.id.click);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (more.getText().equals("more>>")) {
                    //more.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    more.setText("收起");
                    content.setSingleLine(false);
                    //  more.setTag(2);
                } else {
                    // more.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    more.setText("more>>");
                    content.setSingleLine(true);
                    //more.setTag(1);
                }
            }
        });
        loadingList = (Button) view.findViewById(R.id.more);
        loadingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDetailActivity activity = (VideoDetailActivity) getActivity();
                activity.setMyChoice(2);
            }
        });
        follow = (Button) view.findViewById(R.id.follow); //关注按钮
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ishow) {
                    //follow.setText("关注");
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else if (!NetWorkUtils.isNetworkConnected(getContext())) {
                    Toast.makeText(getContext(), "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
                } else if (follow.getText().equals("关注")) {
                    Intent intent = new Intent(getActivity(), AddFocusService.class);
                    intent.putExtra("studentCode", accountId);
                    intent.putExtra("courseCode", course_code);
                    getActivity().startService(intent);
                    follow.setText("取消关注");
                } else {
                    Intent intent = new Intent(getActivity(), DeleteFocusService.class);
                    intent.putExtra("studentCode", accountId);
                    intent.putExtra("courseCode", course_code);
                    getActivity().startService(intent);
                    follow.setText("关注");
                }
            }
        });
        start = (Button) view.findViewById(R.id.start); //开始学习
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ishow) {
                    //  start.setText("添加课程");
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else if(!NetWorkUtils.isNetworkConnected(getContext())){
                    Toast.makeText(getContext(), "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
                }else if (start.getText().equals("添加课程")) {
                    Intent intent = new Intent(getActivity(), AddLearnService.class);
                    intent.putExtra("studentCode", accountId);
                    intent.putExtra("courseCode", course_code);
                    getActivity().startService(intent);
                    start.setText("删除课程");
                } else {
                    Intent intent = new Intent(getActivity(), DeleteLearnService.class);
                    intent.putExtra("studentCode", accountId);
                    intent.putExtra("courseCode", course_code);
                    getActivity().startService(intent);
                    start.setText("添加课程");
                }
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置移除动画


        title = (TextView) view.findViewById(R.id.video_detail_title);
        score = (TextView) view.findViewById(R.id.score);
        study_number = (TextView) view.findViewById(R.id.study_number);
        visit_number = (TextView) view.findViewById(R.id.visit_number);
        video_detail_desc = (TextView) view.findViewById(R.id.video_detail_desc);
        teacher = (TextView) view.findViewById(R.id.teacher);
        agency = (TextView) view.findViewById(R.id.agency);
        period = (TextView) view.findViewById(R.id.period);
        category = (TextView) view.findViewById(R.id.category);
        type = (TextView) view.findViewById(R.id.type);
        subject = (TextView) view.findViewById(R.id.subject);
        profession = (TextView) view.findViewById(R.id.profession);
        concrete_prof = (TextView) view.findViewById(R.id.concrete_prof);
        target = (TextView) view.findViewById(R.id.target);
    }

    private RecyclerView.Adapter initAdapter() {
        final VideoDetailAdapter adapter = new VideoDetailAdapter(getContext(), mRealm, course_code);
        adapter.setOnItemClickListener(new VideoDetailAdapter.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                Toast.makeText(getContext(), "你长按了item", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onItemClick(View v, int position) {
                Video video = videoList.get(position);
                Intent intent = new Intent(getActivity(), ShowVideoActivity.class);
                intent.putExtra(ShowVideoActivity.EXTRA_VIDEO_URL, video.getUrl());
                startActivity(intent);
            }
        });
        return adapter;
    }

    private static class LearnAndFocusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FindIfFocusedService.ACTION_CODE)) {
                focus = intent.getBooleanExtra(FindIfFocusedService.EXTRA_CODE, false);
                Log.d(TAG, "onReceive: " + focus);
                if (focus) {
                    follow.setText("取消关注");
                } else {
                    follow.setText("关注");
                }
            } else if (intent.getAction().equals(FindIfLearnedService.ACTION_CODE)) {
                learn = intent.getBooleanExtra(FindIfLearnedService.EXTRA_CODE, false);
                if (learn) {
                    start.setText("删除课程");
                } else {
                    start.setText("添加课程");
                }
            }
        }
    }
}