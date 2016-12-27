package com.app.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.adapter.CategoryAdapter;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.db.CourseDetails;
import com.app.graduationproject.fragment.BaseFragment;
import com.app.graduationproject.fragment.CategoryFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by lenovo on 2016/10/25.
 */
public class CategoryVideoListActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private ListView categyListview;
    private String category;
    private Realm mRealm;
    private Course course;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_video_list_layout);
        category = getIntent().getStringExtra(CategoryFragment.EXTRA_CATEGORY);
        mRealm = Realm.getDefaultInstance();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("分类>>医学");
        setSupportActionBar(mToolbar);

        initCategory();
       /* if(TextUtils.isEmpty(category)){
            return;
        }*/
        CategoryAdapter adapter = new CategoryAdapter(CategoryVideoListActivity.this,
                R.layout.category_video_list_item,courseList,mRealm,category);
        if(adapter.getCount() == 0){
            Toast.makeText(this,"抱歉，没有找到相关课程",Toast.LENGTH_SHORT).show();
            finish();
        }

        categyListview = (ListView) findViewById(R.id.category_video_list);
        categyListview.setAdapter(adapter);
        categyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(R.id.courseCode);
                String courseCode = textView.getText().toString();
                Log.e("Couse",courseCode);
                Intent intent = new Intent(CategoryVideoListActivity.this,VideoDetailActivity.class);
                intent.putExtra(BaseFragment.EXTRA_COURSE_CODE,courseCode);
                startActivity(intent);
            }
        });
    }

    private void initCategory() {
        RealmResults<CourseDetails> courseDetailses = CourseDetails.fromCate(mRealm, category);
        courseList = new ArrayList<Course>();
        for(int i=0;i<courseDetailses.size();i++){
            course = Course.fromCode(mRealm,courseDetailses.get(i).getCode());
            courseList.add(course);
        }

    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }
}
