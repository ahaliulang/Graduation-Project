package com.app.graduationproject.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.ChangePwdActivity;
import com.app.graduationproject.activity.LoginActivity;
import com.app.graduationproject.activity.UpdateProfileActivity;
import com.app.graduationproject.view.BottomDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 2016/10/26.
 */
public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";

    private static final int REQUEST_CODE = 96;
    private static final int RESULT_OK = -1;



    private static final int NAME_REQUESR_PROFILE = 4; //修改个人资料返回新的昵称

    private static final String PHOTO_FILE_NAME = "avatar.jpg";


    private LinearLayout loginLayout;
    private LinearLayout study;
    private LinearLayout add;
    private LinearLayout fellow;
    private LinearLayout info;
    private LinearLayout changePwd;
    private LinearLayout login_item;
    private Button logout;
    public CircleImageView avatar; //头像图片
    private TextView tv_name; //名称
    private SharedPreferences mSharedPreferences;
    private boolean ishow;

    private String name;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        ishow = mSharedPreferences.getBoolean("show", false);
        name = mSharedPreferences.getString("name","点击登陆");
        Log.e(TAG, "onCreate: " );

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach: " );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        initViews(view);

        try {
            File file = new File(getActivity().getFilesDir(), PHOTO_FILE_NAME);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            avatar.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("show", ishow + "");
        showLoginLayout(ishow);
        Log.e(TAG, "onCreateView: " );
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        Log.e(TAG, "onActivityCreated: " );
    }



    private void initListener() {
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ishow) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    //startActivity(intent);
                    startActivityForResult(intent, REQUEST_CODE);
                    Log.e(TAG, "onClick: "+ishow);
                    return;
                }
                //激活系统图库，选择一张图片
                /*Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image");
                //开启一个带返回值的Activity,请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                Log.e(TAG, "onClick: " + ishow);*/
                BottomDialog dialog = new BottomDialog(getActivity());
                dialog.show();

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
                startActivityForResult(intent,NAME_REQUESR_PROFILE);
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
                //editor.clear();
                editor.putBoolean("show", false);
                editor.putString("name","点击登陆");
                editor.commit();
                showLoginLayout(false);
                ishow = false;
                tv_name.setText("点击登陆");


                boolean show = mSharedPreferences.getBoolean("show", false);
                Log.e("heihie", show + "");
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

        Log.e(TAG, "onActivityResult: " );
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.e("SSSSS", "ssss");
            if(data != null){
                String name = data.getStringExtra("name");
                tv_name.setText(name);
            }

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("show", true);
            ishow = true;
            editor.commit();
            showLoginLayout(true);

            boolean show = mSharedPreferences.getBoolean("show", false);
            Log.e("heihie", show + "");
        } else if(requestCode == NAME_REQUESR_PROFILE && resultCode == RESULT_OK){
            if(data != null){
                String name = data.getStringExtra("name");
                tv_name.setText(name);
            }
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

        avatar = (CircleImageView) view.findViewById(R.id.avatar);
        tv_name = (TextView) view.findViewById(R.id.login);
        tv_name.setText(name);
    }

    private void showLoginLayout(boolean show) {
        if (show) {
            login_item.setVisibility(View.VISIBLE);
            //loginLayout.setEnabled(false);
            logout.setVisibility(View.VISIBLE);
            try {
                File file = new File(getActivity().getFilesDir(), PHOTO_FILE_NAME);
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                avatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            login_item.setVisibility(View.INVISIBLE);
            // loginLayout.setEnabled(true);
            logout.setVisibility(View.INVISIBLE);
            avatar.setImageResource(R.drawable.ic_account_circle_white_48dp);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




}








































