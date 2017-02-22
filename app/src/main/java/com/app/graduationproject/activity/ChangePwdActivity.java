package com.app.graduationproject.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.services.ChangePwdService;
import com.app.graduationproject.utils.InputMethodUtils;

/**
 * Created by TAN on 2016/12/11.
 */
public class ChangePwdActivity extends BaseActivity {

    public static final String EXTRA_USER = "acctount";
    public static final String EXTRA_OLD_PASSWORD = "old_password";
    public static final String EXTRA_NEW_PASSWORD = "new_password";

    private EditText old_pwd;
    private EditText new_pwd;
    private EditText confirm;
    //private Button change;

    private String old_pwd_text;
    private String new_pwd_text;
    private String confirm_text;
    private String account;

    private Toolbar mToolbar;

    private LocalBroadcastManager mLocalBroadcastManager;
    private ChangeStatusReceiver changeStatusReceiver;

    private ProgressBar update_pb;
    private TextView update_tv;

    private SharedPreferences mSharedPreferences; //获取已登录的存储账号



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.activity_change_pwd);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("修改密码");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSharedPreferences = this.getSharedPreferences("account",MODE_PRIVATE);

        changeStatusReceiver = new ChangeStatusReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(changeStatusReceiver, new IntentFilter(ChangePwdService.ACTION_STATUS));

        account = mSharedPreferences.getString("accountId","");

        update_pb = (ProgressBar) findViewById(R.id.pb_update);
        update_tv = (TextView) findViewById(R.id.tv_update);

        Log.e("account",account);

        initView();
       /* change.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save){
            InputMethodUtils.closeKeyboard(ChangePwdActivity.this);
            if(old_pwd.getText().length() < 6 ){
                //Toast.makeText(ChangePwdActivity.this,"原密码不能小于6位",Toast.LENGTH_SHORT).show();
                old_pwd.setError("密码不能小于6位");
            }else if(new_pwd.getText().length() <6){
                //Toast.makeText(ChangePwdActivity.this,"密码不能小于6位",Toast.LENGTH_SHORT).show();
                new_pwd.setError("新密码不能小于6位");
            } else if(!new_pwd_text.equals(confirm_text)){
                //Toast.makeText(ChangePwdActivity.this,"两次密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                confirm.setError("两次输入的密码不一致");
            }else {
                update_pb.setVisibility(View.VISIBLE);
                update_tv.setVisibility(View.VISIBLE);
                InputMethodUtils.closeKeyboard(ChangePwdActivity.this);
                Intent intent = new Intent(ChangePwdActivity.this, ChangePwdService.class);
                intent.putExtra(EXTRA_USER,account);
                intent.putExtra(EXTRA_OLD_PASSWORD,old_pwd_text);
                intent.putExtra(EXTRA_NEW_PASSWORD,new_pwd_text);
                Log.e("TAG",account+" " + old_pwd_text + " " + new_pwd_text);

                startService(intent);
            }
        }
        return super.onOptionsItemSelected(item);
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
        //change = (Button) findViewById(change);
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
            //changeState();
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
            //changeState();
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
            //changeState();
        }
    };

   /* private void changeState() {
        if (!(TextUtils.isEmpty(old_pwd_text)) && !(TextUtils.isEmpty(new_pwd_text)) && !TextUtils.isEmpty(confirm_text)) {
            change.setEnabled(true);
            change.setBackgroundColor(getResources().getColor(R.color.colorButton));
        } else {
            change.setEnabled(false);
            change.setBackgroundColor(getResources().getColor(R.color.colorGray));
        }
    }*/

    private class ChangeStatusReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            final int status = intent.getIntExtra(ChangePwdService.EXTRA_STATUS,0);
            if(status == 1){
                //Toast.makeText(context,"修改密码成功",Toast.LENGTH_SHORT).show();
                finish();
            }else if(status == 0){
                //Toast.makeText(context,"原密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
                old_pwd.setError("原密码不正确");
            }else {
                update_pb.setVisibility(View.GONE);
                update_tv.setVisibility(View.GONE);
                Toast.makeText(context, "无法连接服务器，请检查你的网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }



}





































