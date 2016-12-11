package com.app.graduationproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.services.LoginService;

/**
 * Created by TAN on 2016/11/20.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "user";
    public static final String EXTRA_PASSWROD = "password";

    private Button login;
    private String code; //账号
    private String pwd; //密码
    private EditText user;
    private EditText password;
    private LocalBroadcastManager mLocalBroadcastManager;
    private LoginStatusReceiver loginStatusReceiver;

    private SharedPreferences mSharedPreferences; //存储登录的账号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSharedPreferences = this.getSharedPreferences("account",MODE_PRIVATE);
        loginStatusReceiver = new LoginStatusReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(loginStatusReceiver, new IntentFilter(LoginService.ACTION_STATUS));
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LoginService.class);
                intent.putExtra(EXTRA_USER, code);
                intent.putExtra(EXTRA_PASSWROD, pwd);
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
            final int status = intent.getIntExtra(LoginService.EXTRA_STATUS, 0);
            if (status == 1) {
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();

                //将登录账号存储起来
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.putString("accountId",code);
                editor.commit();
                LoginActivity.this.setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
            }
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
            login.setBackgroundColor(getResources().getColor(R.color.colorDeepGreen));
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
