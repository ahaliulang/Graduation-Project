package com.app.graduationproject.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.adapter.CategoryAdapter;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by Administrator on 2017/1/3.
 */

public class SearchActivity extends BaseActivity{

    private static final String TAG = "SearchActivity";
    private EditText mEtSearch;
    private ImageView mIvDelete;
    private ImageView mIvBack;
    private TextView mTvResult;
    private InputMethodManager imm;
    private ListView mLvResult;
    private Realm mRealm;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.activity_search);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initListener();
    }

    private void initView() {
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mIvDelete = (ImageView) findViewById(R.id.iv_delete);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mIvBack = (ImageView) findViewById(R.id.action_back);
        mLvResult = (ListView) findViewById(R.id.main_lv_search_results);
    }

    private void initListener() {
        mEtSearch.addTextChangedListener(searchWatch);

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                   /* mEtSearch.setCursorVisible(false);*/
                    String key = textView.getText().toString().trim();
                    Log.d(TAG, "onEditorAction: " + key);
                    RealmResults<Course> results1 = Course.fromNameKey(mRealm, key);
                    RealmResults<Course> results2 = Course.fromTeacherKey(mRealm,key);
                    Set<Course> set = new HashSet<>();
                    if(results1.size() > 0 ){
                        for(Course c : results1){
                           set.add(c);
                        }
                    }
                    if(results2.size() > 0) {
                        for(Course c : results2){
                            set.add(c);
                        }
                    }
                    if(set.size()> 0){
                        List<Course> courseList = new ArrayList<>(set);
                        CategoryAdapter adapter = new CategoryAdapter(SearchActivity.this,
                                R.layout.category_video_list_item,courseList);
                        mLvResult.setAdapter(adapter);
                        mLvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                TextView textView = (TextView) view.findViewById(R.id.courseCode);
                                String courseCode = textView.getText().toString();
                                Log.e("Couse",courseCode);
                                Intent intent = new Intent(SearchActivity.this,VideoDetailActivity.class);
                                intent.putExtra(BaseFragment.EXTRA_COURSE_CODE,courseCode);
                                View transitionView = view.findViewById(R.id.thumbnail);
                                ActivityOptionsCompat options =
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this,
                                                transitionView, getString(R.string.transition_video_img));
                                ActivityCompat.startActivity(SearchActivity.this, intent, options.toBundle());
                                //startActivity(intent);
                            }
                        });
                        mLvResult.setVisibility(View.VISIBLE);
                        mTvResult.setVisibility(View.GONE);
                    }else {
                        mTvResult.setVisibility(View.VISIBLE);
                        mLvResult.setVisibility(View.GONE);
                    }
                    imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
                }
                return false;
            }
        });


        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtSearch.setText(null);
                mIvDelete.setVisibility(View.GONE);
            }
        });
    }

    private TextWatcher searchWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(!TextUtils.isEmpty(mEtSearch.getText().toString())){
                mIvDelete.setVisibility(View.VISIBLE);
            }
        }
    };
}



















































