package com.app.graduationproject.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.entity.Student;
import com.app.graduationproject.services.GetProfileService;
import com.app.graduationproject.services.UpdateProfileService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/19.
 */

public class UpdateProfileActivity extends Activity {



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

    private Button btn_update;

    private RadioGroup rg_gender;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private RadioButton rb_secret;

    private String code,name,gender,phone,mail,institute,profession,introduce;

    private Student student;


    private LocalBroadcastManager mLocalBroadcastManager;
    private UpdateProfileReceiver updateProfileReceiver;

    private SharedPreferences mSharedPreferences; //获取已登录的存储账号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        getProfileOnEditText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("student",student.toString());
        if(student != null){
            et_name.setText(student.getName());
            et_phone.setText(student.getPhone());
            et_mail.setText(student.getMail());
            et_institute.setText(student.getInstitute());
            et_profession.setText(student.getProfession());
            et_introduce.setText(student.getIntroduce());
        }

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

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_mail = (EditText) findViewById(R.id.et_email);
        et_institute = (EditText) findViewById(R.id.et_institute);
        et_profession = (EditText) findViewById(R.id.et_profession);
        et_introduce = (EditText) findViewById(R.id.et_introduce);

        btn_update = (Button) findViewById(R.id.update);

        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);

        rb_male = (RadioButton) findViewById(R.id.rb_male);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        rb_secret = (RadioButton) findViewById(R.id.rb_secret);

    }

    private void initListener() {
        btn_update.setOnClickListener(new View.OnClickListener() {

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
        });

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
                    Toast.makeText(context,"修改成功！",Toast.LENGTH_SHORT).show();
                    SharedPreferences  .Editor editor = mSharedPreferences.edit();
                    editor.putString("name",name); //保存昵称
                    editor.commit();

                    Intent intentName = new Intent();
                    intentName.putExtra("name",name);
                    UpdateProfileActivity.this.setResult(RESULT_OK,intentName);
                    finish();
                }else {
                    Toast.makeText(context,"修改失败！",Toast.LENGTH_SHORT).show();
                }
            }else if(intent.getAction().equals(GetProfileService.ACTION_STUDENT)){
                Student student = intent.getParcelableExtra(GetProfileService.KEY);
                Toast.makeText(context,"u see"+student.getName(),Toast.LENGTH_SHORT).show();
                Log.e("receiver", "onReceive: "+"resds" );
                et_name.setText(student.getName());
                et_phone.setText(student.getPhone());
                et_mail.setText(student.getMail());
                et_institute.setText(student.getInstitute());
                et_profession.setText(student.getProfession());
                et_introduce.setText(student.getIntroduce());
                if(rb_male.getText().equals(student.getGender())){
                    rg_gender.check(rb_male.getId());
                }else if(rb_female.getText().equals(student.getGender())){
                    rg_gender.check(rb_female.getId());
                }else {
                    rg_gender.check(rb_secret.getId());
                }
            }

        }
    }
}




















































