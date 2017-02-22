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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.services.LoginService;
import com.app.graduationproject.utils.InputMethodUtils;
import com.app.graduationproject.utils.NetWorkUtils;

/**
 * Created by TAN on 2016/11/20.
 */
public class LoginActivity extends BaseActivity {

    public static final String EXTRA_USER = "user";
    public static final String EXTRA_PASSWROD = "password";

    private static final String TAG = "LoginActivity";

    private Button login;
    private String code; //账号
    private String pwd; //密码
    private EditText user;
    private EditText password;
    private LocalBroadcastManager mLocalBroadcastManager;
    private LoginStatusReceiver loginStatusReceiver;

    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences; //存储登录的账号
    private ProgressBar pb;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("登陆");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pb = (ProgressBar) findViewById(R.id.progress_bar);

        mSharedPreferences = this.getSharedPreferences("account",MODE_PRIVATE);
        loginStatusReceiver = new LoginStatusReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(loginStatusReceiver, new IntentFilter(LoginService.ACTION_STATUS));
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodUtils.closeKeyboard(LoginActivity.this);
                pb.setVisibility(View.VISIBLE);
                Intent intent = new Intent(LoginActivity.this, LoginService.class);
                intent.putExtra(EXTRA_USER, code);
                intent.putExtra(EXTRA_PASSWROD, pwd);
                login.setText("登陆中...");
                if(!NetWorkUtils.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(LoginActivity.this,"当前网络不可用，请检查你的网络设置",Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                    login.setText("登陆");
                    return;
                }
                startService(intent);
            }
        });
        user = (EditText) findViewById(R.id.user);
        user.addTextChangedListener(userwatch);
        password = (EditText) findViewById(R.id.password);
        password.addTextChangedListener(passwordwatch);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(loginStatusReceiver);
    }

    private class LoginStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String name = intent.getStringExtra(LoginService.EXTRA_STATUS);
            Log.d(TAG, "onReceive: "+name + " " + intent);
            if(name.equals("false")){
                Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                login.setText("登陆");
                login.setBackgroundColor(getResources().getColor(R.color.colorGray));
                password.setText("");
                pb.setVisibility(View.GONE);
            }else if(!TextUtils.isEmpty(name)){
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.putString("accountId",code); //保存账号
                editor.putString("name",name); //保存昵称
                editor.commit();
                //LoginActivity.this.setResult(RESULT_OK);
                Intent intentName = new Intent();
                intentName.putExtra("name",name);
                LoginActivity.this.setResult(RESULT_OK,intentName);
                pb.setVisibility(View.GONE);
                finish();
            }else {
                Toast.makeText(context, "无法连接服务器，请检查你的网络连接", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                login.setText("登陆");
            }
            /*if (!TextUtils.isEmpty(name)) {
               // Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();

                //将登录账号存储起来
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.putString("accountId",code); //保存账号
                editor.putString("name",name); //保存昵称
                editor.commit();
                //LoginActivity.this.setResult(RESULT_OK);
                Intent intentName = new Intent();
                intentName.putExtra("name",name);
                LoginActivity.this.setResult(RESULT_OK,intentName);
                finish();
            } else {
                Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                login.setText("登陆");
                login.setBackgroundColor(getResources().getColor(R.color.colorGray));
                password.setText("");
                flag = true;

            }
            Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();*/

        }
    }




    private TextWatcher userwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            code = user.getText().toString();
            loginState();
        }
    };

    private TextWatcher passwordwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            pwd = password.getText().toString();
            loginState();
        }
    };

    private void loginState() {
        if (!(TextUtils.isEmpty(code)) && !(TextUtils.isEmpty(pwd))) {
            login.setEnabled(true);
            login.setBackgroundColor(getResources().getColor(R.color.colorButton));
        } else {
            login.setEnabled(false);
            login.setBackgroundColor(getResources().getColor(R.color.colorGray));
        }
    }

     /*class GsonUtil{
        public <T> T parseJsonWithGson(String jsonData, Class<T> type){
            Gson gson = new Gson();
            T result = gson.fromJson(jsonData,type);
            return result;
        }
    }*/
}
