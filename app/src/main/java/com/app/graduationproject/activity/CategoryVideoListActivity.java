package com.app.graduationproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.graduationproject.R;
import com.app.graduationproject.adapter.CategoryAdapter;
import com.app.graduationproject.entity.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/25.
 */
public class CategoryVideoListActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private ListView categyListview;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_video_list_layout);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("分类>>医学");
        setSupportActionBar(mToolbar);

        initCategory();
        CategoryAdapter adapter = new CategoryAdapter(CategoryVideoListActivity.this,
                R.layout.category_video_list_item,categoryList);
        categyListview = (ListView) findViewById(R.id.category_video_list);
        categyListview.setAdapter(adapter);
        categyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CategoryVideoListActivity.this,VideoDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initCategory() {
        categoryList = new ArrayList<Category>();
        for(int i =0 ;i<10;i++){
            Category category = new Category(R.drawable.test2,"药事管理","共28集","共124学习");
            categoryList.add(category);
        }
    }
}
