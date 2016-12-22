package com.app.graduationproject.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.graduationproject.R;

import java.io.File;

import static com.app.graduationproject.MainActivity.PHOTO_REQUEST_CAMERA;
import static com.app.graduationproject.MainActivity.PHOTO_REQUEST_GALLERY;

/**
 * Created by Administrator on 2016/12/22.
 */

public class BottomDialog extends Dialog{

    private Context context;



    public BottomDialog(Context context) {
        super(context, R.style.bottom_dialog);
        this.context = context;
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    protected BottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeavatar_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("dongdong","1");

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

                try {
                    Log.e("dongdong","2");
                    File file = new File(Environment.getExternalStorageDirectory()+"/dongdong","temp.jpg");
                    Log.e("dongdong","3");
                   // if (!file.exists()) file.createNewFile();
                    //通过得到文件的父文件，判断父文件是否存在
                    Log.e("dongdong","4");
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }

                    Log.e("dongdong","5");
                    //从文件中创建uri
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                    Log.e("dongdong","6");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("dongdong","catch");
                }
                //context.startActivity(intent);
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                ((Activity)context).startActivityForResult(intent,PHOTO_REQUEST_CAMERA);
                Log.e("dongdong","7");
                cancelMe();

            }
        });
        findViewById(R.id.tv_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //开启一个带返回值的Activity,请求码为PHOTO_REQUEST_GALLERY
               // context.startActivity(intent);
                ((Activity)context).startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                cancelMe();
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelMe();
            }
        });

    }

    private void cancelMe(){
        dismiss();
    }

    @Override
    public void show() {
        super.show();

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);

        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogWindow.setAttributes(params);
    }
}





































