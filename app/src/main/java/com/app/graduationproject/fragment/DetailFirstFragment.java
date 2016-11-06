package com.app.graduationproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.VideoDetailActivity;
import com.app.graduationproject.adapter.VideoDetailAdapter;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.CourseDetails;

import io.realm.Realm;

/**
 * Created by lenovo on 2016/10/23.
 */
public class DetailFirstFragment extends Fragment{

    private Button more;   //查看更过按钮
    private RecyclerView mRecyclerView; //显示的部分课程列表
    private Button loadingList;
    private TextView content;
    private Course course;
    private CourseDetails details;
    private Realm mRealm;
    private TextView title;
    private TextView score;
    private TextView study_number;
    private TextView visit_number;
    private TextView video_detail_desc;
    private TextView teacher;
    private TextView agency,period,category,type,subject,profession,concrete_prof,target;

    private String course_code;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        course_code = getActivity().getIntent().getStringExtra(BaseFragment.EXTRA_COURSE_CODE);
        mRealm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.video_detail_first_fragment,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        course = Course.fromCode(mRealm,course_code);
        details = CourseDetails.fromCode(mRealm,course_code);
        if(course != null){
            title.setText(course.getName());
            score.setText(course.getScore());
            study_number.setText(course.getStudy());
            visit_number.setText(course.getVisit());
            teacher.setText(course.getTeacher());
            video_detail_desc.setText(course.getIntroduce());
            mRecyclerView.setAdapter(initAdapter());
        }
        if(details != null){
            //agency,period,category,type,subject,profession,concrete_prof,target
            agency.setText(details.getAgency());
            period.setText(details.getPeriod());
            category.setText(details.getCategory());
            type.setText(details.getType());
            subject.setText(details.getSubject());
            profession.setText(details.getProfession());
            concrete_prof.setText(details.getConctrte_prof());
            target.setText(details.getTarget());
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
                activity.setChoice(2);
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

    private RecyclerView.Adapter initAdapter(){
        final VideoDetailAdapter adapter = new VideoDetailAdapter(getContext());
        adapter.setOnItemClickListener(new VideoDetailAdapter.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                Toast.makeText(getContext(),"你长按了item",Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getContext(),"你点击了item",Toast.LENGTH_SHORT).show();
            }
        });
        return adapter;
    }
}
