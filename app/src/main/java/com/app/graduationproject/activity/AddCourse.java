package com.app.graduationproject.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.adapter.AddCourseMainAdapter;
import com.app.graduationproject.adapter.CategoryAdapter;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.CourseDetails;
import com.app.graduationproject.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Administrator on 2017/2/13.
 */

public class AddCourse extends BaseActivity{

    private static final String TAG = "AddCourse";

    private Realm mRealm;
    private ListView mainlist;
    private ListView morelist;
    private Toolbar mToolbar;

    AddCourseMainAdapter mainAdapter;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.activity_addcourse);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("添加课程");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRealm = Realm.getDefaultInstance();
        initView();

    }

    private void initView(){
        List<String> courses = getCourses(mRealm);
        mainlist = (ListView) findViewById(R.id.classify_mainlist);
        morelist = (ListView) findViewById(R.id.category_video_list);
        mainAdapter = new AddCourseMainAdapter(this,courses);
        mainAdapter.setSelectItem(0);
        mainlist.setAdapter(mainAdapter);
        CategoryAdapter categoryAdapter = new CategoryAdapter(AddCourse.this,R.layout.category_video_list_item,
                initCategory(courses.get(0).toString())/*,courses.get(0).toString()*/);
        morelist.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
        mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tv_category = (TextView) view.findViewById(R.id.mainitem_txt);
                String category = tv_category.getText().toString();

                CategoryAdapter adapter = new CategoryAdapter(AddCourse.this,
                        R.layout.category_video_list_item,initCategory(category)/*,category*/);
                morelist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mainAdapter.setSelectItem(i);
                mainAdapter.notifyDataSetChanged();
            }
        });
        mainlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        morelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.courseCode);
                String courseCode = textView.getText().toString();
                Log.e("Couse",courseCode);
                Intent intent = new Intent(AddCourse.this,VideoDetailActivity.class);
                intent.putExtra(BaseFragment.EXTRA_COURSE_CODE,courseCode);
                View transitionView = view.findViewById(R.id.thumbnail);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(AddCourse.this,
                                transitionView, getString(R.string.transition_video_img));
                ActivityCompat.startActivity(AddCourse.this, intent, options.toBundle());
                //startActivity(intent);
            }
        });
    }



    private List<String> getCourses(Realm realm){
        RealmResults<CourseDetails> courseDetailses = CourseDetails.courseCate(realm);
        List<String> category = new ArrayList<>();
        for (int i = 0; i < courseDetailses.size(); i++) {
            Log.d(TAG, "getCourses: " + courseDetailses.size());
            category.add(courseDetailses.get(i).getCategory().toString());
            Log.d(TAG, "getCourses: + courseDetailses.get(i).toString()");
        }
        return category;
    }

    private List<Course> initCategory(String category) {
        RealmResults<CourseDetails> courseDetailses = CourseDetails.fromCate(mRealm, category);
        List<Course> courseList = new ArrayList<>();
        for(int i=0;i<courseDetailses.size();i++){
            Course course = Course.fromCode(mRealm,courseDetailses.get(i).getCode());
            courseList.add(course);
        }
        return courseList;
    }


}

























































