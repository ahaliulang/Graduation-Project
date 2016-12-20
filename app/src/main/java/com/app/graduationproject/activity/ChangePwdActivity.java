package com.app.graduationproject.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.services.ChangePwdService;

/**
 * Created by TAN on 2016/12/11.
 */
public class ChangePwdActivity extends Activity{

    public static final String EXTRA_USER = "acctount";
    public static final String EXTRA_OLD_PASSWORD = "old_password";
    public static final String EXTRA_NEW_PASSWORD = "new_password";

    private EditText old_pwd;
    private EditText new_pwd;
    private EditText confirm;
    private Button change;

    private String old_pwd_text;
    private String new_pwd_text;
    private String confirm_text;
    private String account;

    private LocalBroadcastManager mLocalBroadcastManager;
    private ChangeStatusReceiver changeStatusReceiver;

    private SharedPreferences mSharedPreferences; //获取已登录的存储账号



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_pwd);
        super.onCreate(savedInstanceState);

        mSharedPreferences = this.getSharedPreferences("account",MODE_PRIVATE);

        changeStatusReceiver = new ChangeStatusReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(changeStatusReceiver, new IntentFilter(ChangePwdService.ACTION_STATUS));

        account = mSharedPreferences.getString("accountId","");

        Log.e("account",account);

        initView();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(old_pwd.getText().length() < 6 ){
                    Toast.makeText(ChangePwdActivity.this,"原密码不能小于6位",Toast.LENGTH_SHORT).show();
                }else if(new_pwd.getText().length() <6){
                    Toast.makeText(ChangePwdActivity.this,"新密码不能小于6位",Toast.LENGTH_SHORT).show();
                } else if(!new_pwd_text.equals(confirm_text)){
                    Toast.makeText(ChangePwdActivity.this,"两次密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ChangePwdActivity.this, ChangePwdService.class);
                    intent.putExtra(EXTRA_USER,account);
                    intent.putExtra(EXTRA_OLD_PASSWORD,old_pwd_text);
                    intent.putExtra(EXTRA_NEW_PASSWORD,new_pwd_text);
                    Log.e("TAG",account+" " + old_pwd_text + " " + new_pwd_text);

                    startService(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(changeStatusReceiver);

    }

    private void initView() {
        old_pwd = (EditText) findViewById(R.id.old_pwd);
        old_pwd.addTextChangedListener(old_pwd_textwatch);
        new_pwd = (EditText) findViewById(R.id.new_pwd);
        new_pwd.addTextChangedListener(new_pwd_textwatch);
        confirm = (EditText) findViewById(R.id.confirm_pwd);
        confirm.addTextChangedListener(confirm_textwatch);
        change = (Button) findViewById(R.id.change);
    }

    private TextWatcher old_pwd_textwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            old_pwd_text = old_pwd.getText().toString();
            changeState();
        }
    };

    private TextWatcher new_pwd_textwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            new_pwd_text = new_pwd.getText().toString();
            changeState();
        }
    };

    private TextWatcher confirm_textwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            confirm_text = confirm.getText().toString();
            changeState();
        }
    };

    private void changeState() {
        if (!(TextUtils.isEmpty(old_pwd_text)) && !(TextUtils.isEmpty(new_pwd_text)) && !TextUtils.isEmpty(confirm_text)) {
            change.setEnabled(true);
            change.setBackgroundColor(getResources().getColor(R.color.colorDeepGreen));
        } else {
            change.setEnabled(false);
            change.setBackgroundColor(getResources().getColor(R.color.colorGray));
        }
    }

    private class ChangeStatusReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            final int status = intent.getIntExtra(ChangePwdService.EXTRA_STATUS,0);
            if(status == 1){
                Toast.makeText(context,"修改密码成功",Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(context,"原密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }
    }



}





































