package com.app.graduationproject.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.ChangePwdActivity;
import com.app.graduationproject.activity.LoginActivity;
import com.app.graduationproject.activity.UpdateProfileActivity;

/**
 * Created by lenovo on 2016/10/26.
 */
public class MyFragment extends Fragment {

    private static final int REQUEST_CODE = 96;
    private static final int RESULT_OK = -1;

    private LinearLayout loginLayout;
    private LinearLayout study;
    private LinearLayout add;
    private LinearLayout fellow;
    private LinearLayout info;
    private LinearLayout changePwd;

    private LinearLayout login_item;
    private Button logout;

    private SharedPreferences mSharedPreferences;
    private boolean ishow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getActivity().getSharedPreferences("showLogin", Context.MODE_PRIVATE);
        ishow = mSharedPreferences.getBoolean("show",false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        initViews(view);
        Log.e("show",ishow+"");
        showLoginLayout(ishow);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
    }

    private void initListener() {
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 96);
            }
        });
        changePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePwdActivity.class);
                startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitAlertDialog(); //退出时弹出对话框
            }
        });
    }

    private void showExitAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.putBoolean("show",false);
                editor.commit();
                showLoginLayout(false);


                boolean show = mSharedPreferences.getBoolean("show",false);
                Log.e("heihie",show+"");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 96 && resultCode == RESULT_OK) {
            Log.e("SSSSS", "ssss");

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.clear();
            editor.putBoolean("show",true);
            editor.commit();
            showLoginLayout(true);

            boolean show = mSharedPreferences.getBoolean("show", false);
            Log.e("heihie",show+"");
        }
    }

    private void initViews(View view) {
        loginLayout = (LinearLayout) view.findViewById(R.id.loginlayout);
        study = (LinearLayout) view.findViewById(R.id.study);
        add = (LinearLayout) view.findViewById(R.id.add);
        fellow = (LinearLayout) view.findViewById(R.id.fellow);
        info = (LinearLayout) view.findViewById(R.id.info);
        changePwd = (LinearLayout) view.findViewById(R.id.changePwd);

        login_item = (LinearLayout) view.findViewById(R.id.login_item);
        logout = (Button) view.findViewById(R.id.logout);
    }

    private void showLoginLayout(boolean show){
        if(show){
            login_item.setVisibility(View.VISIBLE);
            loginLayout.setEnabled(false);
            logout.setVisibility(View.VISIBLE);
        }else {
            login_item.setVisibility(View.INVISIBLE);
            loginLayout.setEnabled(true);
            logout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}








































