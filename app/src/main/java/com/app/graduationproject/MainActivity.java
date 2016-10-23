package com.app.graduationproject;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.graduationproject.fragment.CategoryFragment;
import com.app.graduationproject.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private TextView mHome;
    private TextView mCategory;
    private TextView mMy;
    private long mExitTime = 0;
    private boolean mIsSearching;//搜索框是否出现，true为出现，false为隐藏
    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;
    private FragmentManager fragmentManager;//碎片管理器
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        initView();
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
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //菜单选择


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about:
                //TODO
                break;
            case R.id.action_clear_cache:
                //TODO
                break;
            case R.id.action_search:
                //TODO searchview
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setChoice(int currentItem){
        transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        clearChoice();
        switch (currentItem){
            case 1:
                mHome.setTextColor(getResources().getColor(R.color.colorBlack));
                if(mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.main_ll_fragment,mHomeFragment);
                }else {
                    transaction.show(mHomeFragment);
                }
                break;
            case 2:
                mCategory.setTextColor(getResources().getColor(R.color.colorBlack));
                if(mCategoryFragment == null){
                    mCategoryFragment = new CategoryFragment();
                    transaction.add(R.id.main_ll_fragment,mCategoryFragment);
                }else {
                    transaction.show(mCategoryFragment);
                }
                break;
            case 3:
                mMy.setTextColor(getResources().getColor(R.color.colorBlack));
                Toast.makeText(MainActivity.this,"你点击了3",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        transaction.commit();//提交事物
    }


    private void hideFragments(FragmentTransaction transaction){
        if(mHomeFragment != null){
            transaction.hide(mHomeFragment);
        }
        if(mCategoryFragment != null){
            transaction.hide(mCategoryFragment);
        }
    }

    //按两次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis() - mExitTime) > 2000){
                Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }else {
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
        switch (view.getId()){
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
    private void clearChoice(){
        mHome.setTextColor(getResources().getColor(R.color.colorGray));
        mCategory.setTextColor(getResources().getColor(R.color.colorGray));
        mMy.setTextColor(getResources().getColor(R.color.colorGray));
    }
}
