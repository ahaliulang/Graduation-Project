package com.app.graduationproject.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;



/**
 * Created by Administrator on 2017/1/3.
 */

public class SearchActivity extends AppCompatActivity{

    private EditText mEtSearch;
    private ImageView mIvDelete;
    private ImageView mIvBack;
    private TextView mTvResult;
    private InputMethodManager imm;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void initListener() {
        mEtSearch.addTextChangedListener(searchWatch);


        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                   /* mEtSearch.setCursorVisible(false);*/
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



















































