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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.entity.Student;
import com.app.graduationproject.services.GetProfileService;
import com.app.graduationproject.services.UpdateProfileService;
import com.app.graduationproject.utils.InputMethodUtils;
import com.app.graduationproject.utils.NetWorkUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/19.
 */

public class UpdateProfileActivity extends BaseActivity {

    private static final String TAG = "UpdateProfileActivity";

    public static final String EXTRA_CODE = "account";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_GENDER = "gender";
    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_MAIL = "mail";
    public static final String EXTRA_INSTITUTE = "institute";
    public static final String EXTRA_PROFESSION = "profession";
    public static final String EXTRA_INTRODUCE = "introduce";


    private EditText et_name;
    private EditText et_phone;
    private EditText et_mail;
    private EditText et_institute;
    private EditText et_profession;
    private EditText et_introduce;

   // private Button btn_update;

    private RadioGroup rg_gender;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private RadioButton rb_secret;

    private Toolbar mToolbar;

    private String code,name,gender,phone,mail,institute,profession,introduce;

    private Student student;

    private ProgressBar pb;
    private TextView tv_error;


    private LocalBroadcastManager mLocalBroadcastManager;
    private UpdateProfileReceiver updateProfileReceiver;

    private SharedPreferences mSharedPreferences; //获取已登录的存储账号
    private ProgressBar update_pb;
    private TextView update_tv;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        setContentView(R.layout.activity_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("个人资料");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pb = (ProgressBar) findViewById(R.id.progress_bar);
        tv_error = (TextView) findViewById(R.id.tv_error);

        update_pb = (ProgressBar) findViewById(R.id.pb_update);
        update_tv = (TextView) findViewById(R.id.tv_update);

        mSharedPreferences = this.getSharedPreferences("account",MODE_PRIVATE);
        updateProfileReceiver = new UpdateProfileReceiver();

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(UpdateProfileService.ACTION_STATUS);
        filter.addAction(GetProfileService.ACTION_STUDENT);
        mLocalBroadcastManager.registerReceiver(updateProfileReceiver, filter);
        initView();
        code = mSharedPreferences.getString("accountId","");  //获取已登陆的存储的账号
        gender = rb_male.getText().toString().trim();
        initListener();
        if(!checkNetwork()){
            return;
        }
        getProfileOnEditText();
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* //Log.e("student",student.toString());
        if(student != null){
            et_name.setText(student.getName());
            et_phone.setText(student.getPhone());
            et_mail.setText(student.getMail());
            et_institute.setText(student.getInstitute());
            et_profession.setText(student.getProfession());
            et_introduce.setText(student.getIntroduce());
        }*/

        Log.e("onResume", "onResume: "+"uuu");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(updateProfileReceiver);

    }

    private void getProfileOnEditText() {
        Intent intent = new Intent(UpdateProfileActivity.this, GetProfileService.class);
        intent.putExtra(EXTRA_CODE,code);
        startService(intent);
    }


    private boolean checkNetwork() {
        if (!NetWorkUtils.isNetworkConnected(getApplicationContext())) {
            pb.setVisibility(View.INVISIBLE);
            //error.setVisibility(View.VISIBLE);
            Toast.makeText(UpdateProfileActivity.this, "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
            tv_error.setText("获取资料失败，请点击重试");
            tv_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_error.setText("获取资料中...");
                    pb.setVisibility(View.VISIBLE);
                    Toast.makeText(UpdateProfileActivity.this, "当前网络不可用，请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    checkNetwork();
                }
            });
            return false;
        }
        return true;
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_mail = (EditText) findViewById(R.id.et_email);
        et_institute = (EditText) findViewById(R.id.et_institute);
        et_profession = (EditText) findViewById(R.id.et_profession);
        et_introduce = (EditText) findViewById(R.id.et_introduce);

        //btn_update = (Button) findViewById(R.id.update);

        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);

        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        rb_secret = (RadioButton) findViewById(R.id.rb_secret);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save){
            if(validateText()){ //格式输入正确
                update_pb.setVisibility(View.VISIBLE);
                update_tv.setVisibility(View.VISIBLE);
                InputMethodUtils.closeKeyboard(UpdateProfileActivity.this);
                Intent intent = new Intent(UpdateProfileActivity.this, UpdateProfileService.class);
                intent.putExtra(EXTRA_CODE,code);
                intent.putExtra(EXTRA_NAME,name);
                intent.putExtra(EXTRA_GENDER,gender);
                intent.putExtra(EXTRA_PHONE,phone);
                intent.putExtra(EXTRA_MAIL,mail);
                intent.putExtra(EXTRA_INSTITUTE,institute);
                intent.putExtra(EXTRA_PROFESSION,profession);
                intent.putExtra(EXTRA_INTRODUCE,introduce);
                //name = et_name.getText().toString().trim();
                startService(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListener() {
        /*btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(validateText()){ //格式输入正确
                    Intent intent = new Intent(UpdateProfileActivity.this, UpdateProfileService.class);
                    intent.putExtra(EXTRA_CODE,code);
                    intent.putExtra(EXTRA_NAME,name);
                    intent.putExtra(EXTRA_GENDER,gender);
                    intent.putExtra(EXTRA_PHONE,phone);
                    intent.putExtra(EXTRA_MAIL,mail);
                    intent.putExtra(EXTRA_INSTITUTE,institute);
                    intent.putExtra(EXTRA_PROFESSION,profession);
                    intent.putExtra(EXTRA_INTRODUCE,introduce);
                    //name = et_name.getText().toString().trim();
                    startService(intent);
                }
            }
        });*/

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if(checkId == rb_male.getId()){
                    gender = rb_male.getText().toString().trim();
                }else if(checkId == rb_female.getId()){
                    gender = rb_female.getText().toString().trim();
                }else {
                    gender = rb_secret.getText().toString().trim();
                }
            }
        });
    }

    /**
     * 验证输入的字符串格式是否正确，必填项是否填写
     */
    private boolean validateText() {
        name = et_name.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        mail = et_mail.getText().toString().trim();
        institute = et_institute.getText().toString().trim();
        profession = et_profession.getText().toString().trim();
        introduce = et_introduce.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            et_name.setError("请填写姓名！");
            return false;
        }else if(name.length()>10){
            et_name.setError("名字长度不能超过10位！");
            return false;
        }

        if(TextUtils.isEmpty(phone)){
            et_phone.setError("请填写手机号码！");
            return false;
        }else if(!checkPhoneNumber(phone)) {
            et_phone.setError("手机格式不正确");
            return false;
        }

        if(TextUtils.isEmpty(mail)){
            et_mail.setError("请填写邮箱！");
            return false;
        }else if(!checkEmail(mail)){
            et_mail.setError("邮箱格式不正确");
            return false;
        }
        Log.d(TAG, "validateText: " + introduce.length());
        if(introduce.length()>80){

            et_introduce.setError("不能超过80个字符");
        }
        return true;
    }

    /**
     * 正则表达式检查手机号码格式
     * @param phoneNumber
     * @return
     */
    public boolean checkPhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


    /**
     * 正则表达式检查邮箱格式
     * @param email
     * @return
     */
    public boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /*@Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        if(checkId == rb_male.getId()){
            gender = rb_male.getText().toString();
        }else if(checkId == rb_female.getId()){
            gender = rb_female.getText().toString();
        }else {
            gender = rb_secret.getText().toString();
        }
    }*/


    private class UpdateProfileReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(UpdateProfileService.ACTION_STATUS)){
                final int status = intent.getIntExtra(UpdateProfileService.EXTRA_STATUS,0);
                if(status == 1){
                    //Toast.makeText(context,"修改成功！",Toast.LENGTH_SHORT).show();
                    SharedPreferences  .Editor editor = mSharedPreferences.edit();
                    editor.putString("name",name); //保存昵称
                    editor.commit();

                    Intent intentName = new Intent();
                    intentName.putExtra("name",name);
                    UpdateProfileActivity.this.setResult(RESULT_OK,intentName);
                    finish();
                }else if(status == 0){
                    Toast.makeText(context,"修改失败！",Toast.LENGTH_SHORT).show();
                }else{
                    update_pb.setVisibility(View.GONE);
                    update_tv.setVisibility(View.GONE);
                    Toast.makeText(context, "无法连接服务器，请检查你的网络连接", Toast.LENGTH_SHORT).show();
                }
            }else if(intent.getAction().equals(GetProfileService.ACTION_STUDENT)){
                Student student = intent.getParcelableExtra(GetProfileService.KEY);
                if(student == null){
                    pb.setVisibility(View.INVISIBLE);
                    tv_error.setText("获取资料失败，请点击重试");
                    tv_error.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tv_error.setText("获取资料中...");
                            pb.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(UpdateProfileActivity.this, GetProfileService.class);
                            intent.putExtra(EXTRA_CODE,code);
                            startService(intent);
                        }
                    });
                    return;
                }
                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_content);
                if(rb_male.getText().equals(student.getGender())){
                    rg_gender.check(rb_male.getId());
                }else if(rb_female.getText().equals(student.getGender())){
                    rg_gender.check(rb_female.getId());
                }else {
                    rg_gender.check(rb_secret.getId());
                }
                et_name.setText(student.getName());
                et_phone.setText(student.getPhone());
                et_mail.setText(student.getMail());
                et_institute.setText(student.getInstitute());
                et_profession.setText(student.getProfession());
                et_introduce.setText(student.getIntroduce());
                pb.setVisibility(View.GONE);
                tv_error.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }
}




















































