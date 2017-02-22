package com.app.graduationproject;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.activity.AboutActivity;
import com.app.graduationproject.activity.SearchActivity;
import com.app.graduationproject.fragment.BaseFragment;
import com.app.graduationproject.fragment.CategoryFragment;
import com.app.graduationproject.fragment.HomeFragment;
import com.app.graduationproject.fragment.MyFragment;
import com.app.graduationproject.net.CloudAPIService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Toolbar mToolbar;
    private TextView mHome;
    private TextView mCategory;
    private TextView mMy;
    private long mExitTime = 0;
    private boolean mIsSearching;//搜索框是否出现，true为出现，false为隐藏
    private HomeFragment mHomeFragment;
    private BaseFragment mBaseFragment;
    private MyFragment mMyFragment;
    private CategoryFragment mCategoryFragment;
    private FragmentManager fragmentManager;//碎片管理器
    private FragmentTransaction transaction;

    public static final int PHOTO_REQUEST_CAMERA = 1;//拍照
    public static final int PHOTO_REQUEST_GALLERY = 2; //从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;//结果

    private static final String PHOTO_FILE_NAME = "avatar.jpg";
    private SharedPreferences mSharedPreferences;



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_color));
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        //Permissions.verifyStoragePermissions(this);
        mSharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        initView();

        Log.d(TAG, "onCreate: ");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // mToolbar.setNavigationIcon(R.drawable.gzhmu);
        mHome = (TextView) findViewById(R.id.home);
        mHome.setOnClickListener(this);
        mCategory = (TextView) findViewById(R.id.category);
        mCategory.setOnClickListener(this);
        mMy = (TextView) findViewById(R.id.my);
        mMy.setOnClickListener(this);
        //设置默认第一个菜单按钮为选中状态
        mHome.setTextColor(getResources().getColor(R.color.colorBlack));
        setChoice(1);

        /*final ImageCycleView imageCycleView = (ImageCycleView) findViewById(R.id.icv_topView);
        List<ImageCycleView.ImageInfo> list = new ArrayList<ImageCycleView.ImageInfo>();
        list.add(new ImageCycleView.ImageInfo(R.drawable.gzhmu,"11",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.ll20163401,"222",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.ll20163410,"33",""));

        imageCycleView.loadData(list, new ImageCycleView.LoadImageCallBack() {
            @Override
            public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageResource(Integer.parseInt(imageInfo.image.toString()));
                return imageView;
            }
        });*/

    }

    //创建菜单


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //菜单选择
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.action_newversion:
                Toast.makeText(this,"当前已是最新版本",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                //TODO searchview
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setChoice(int currentItem) {
        transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        clearChoice();
        switch (currentItem) {
            case 1:
                mHome.setTextColor(getResources().getColor(R.color.colorBlack));
                if (mBaseFragment == null) {
                    mBaseFragment = new BaseFragment();
                    Log.d(TAG, "setChoice: "+1+"null");
                    transaction.add(R.id.main_ll_fragment, mBaseFragment);
                } else {
                    Log.d(TAG, "setChoice: "+1+"not null");
                    transaction.show(mBaseFragment);
                }
               // transaction.replace(R.id.main_ll_fragment, mBaseFragment);
                break;
            case 2:
                mCategory.setTextColor(getResources().getColor(R.color.colorBlack));
               // mBaseFragment.mRefreshLayout.setRefreshing(false);
                if (mCategoryFragment == null) {
                    mCategoryFragment = new CategoryFragment();
                    Log.d(TAG, "setChoice: "+2+"null");
                    transaction.add(R.id.main_ll_fragment, mCategoryFragment);
                } else {
                    transaction.show(mCategoryFragment);
                    Log.d(TAG, "setChoice: "+2+"not null");
                }
                //transaction.replace(R.id.main_ll_fragment, mCategoryFragment);
                break;
            case 3:
                mMy.setTextColor(getResources().getColor(R.color.colorBlack));
               // mBaseFragment.mRefreshLayout.setRefreshing(false);
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                    Log.d(TAG, "setChoice: "+3+"null");
                    transaction.add(R.id.main_ll_fragment, mMyFragment);
                } else {
                    transaction.show(mMyFragment);
                    Log.d(TAG, "setChoice: "+3+"not null");
                }
                //transaction.replace(R.id.main_ll_fragment, mMyFragment);
                break;
            default:
                break;
        }
        transaction.commit();//提交事物
    }


    private void hideFragments(FragmentTransaction transaction) {
        if (mBaseFragment != null) {
            transaction.hide(mBaseFragment);
        }
        if (mCategoryFragment != null) {
            transaction.hide(mCategoryFragment);
        }
        if (mMyFragment != null) {
            transaction.hide(mMyFragment);
        }
    }

    //按两次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish(); //在两秒之内按两次返回键退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    //点击底部菜单栏
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                setChoice(1);
                break;
            case R.id.category:
                setChoice(2);
                break;
            case R.id.my:
                setChoice(3);
                break;
            default:
                break;
        }
    }

    /**
     * 重置所有选项
     */
    private void clearChoice() {
        mHome.setTextColor(getResources().getColor(R.color.colorGray));
        mCategory.setTextColor(getResources().getColor(R.color.colorGray));
        mMy.setTextColor(getResources().getColor(R.color.colorGray));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mMyFragment.isVisible()) {
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                if (data != null) {
                    //得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri);
                    /*Glide.with(this).load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mMyFragment.avatar);*/
                }
            } else if (requestCode == PHOTO_REQUEST_CAMERA && resultCode == RESULT_OK) {
                //从相机返回的数据
                Log.e("1223", "onActivityResult: 2" );
                Log.d("MYURI", "onActivityResult: ");
                File carmeraFile = new File(Environment.getExternalStorageDirectory()+"/dongdong","temp.jpg");
                if(carmeraFile.exists()){
                    Log.e("1223", "onActivityResult: 3" );
                    crop(Uri.fromFile(carmeraFile));
                    Log.d("MYURI", "onActivityResult: " + Uri.fromFile(carmeraFile).toString());
                }

            } else if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK) {
                if (data != null) {
                    Log.e("dongdong","9");
                    Bitmap bitmap = data.getParcelableExtra("data");
                    mMyFragment.avatar.setImageBitmap(bitmap);
                    try {
                        final File file = new File(getFilesDir(), MyFragment.accountId+"_"+PHOTO_FILE_NAME);
                        if (!file.exists()) file.createNewFile();
                        //通过得到文件的父文件，判断父文件是否存在
                        File parentFile = file.getParentFile();
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                        //把图片保存至本地
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                        uploadAvatar(file);
                        //TODO-上传至服务器
                    } catch (FileNotFoundException e) {
                        Log.e("TAG", "nonononono");
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {

        Log.e("1223", "crop: 1" );
        //裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("corp", "true");
        //裁剪框比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");//图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        intent.putExtra("return-data", true);
        //开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    //获取手机状态栏高度
    public int getStatusBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0,statusBarHeight = 0;
        try{
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return  statusBarHeight;
    }

    public void uploadAvatar(File file){
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar",file.getName(),requestFile);
        Call<String> call = CloudAPIService.getInstance().uploadImage(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: "+ "success");
                Log.d(TAG, "onResponse: "+ response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }
    
}





























