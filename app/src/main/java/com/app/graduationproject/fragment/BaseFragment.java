package com.app.graduationproject.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.VideoDetailActivity;
import com.app.graduationproject.adapter.BaseAdapter;
import com.app.graduationproject.db.Course;
import com.app.graduationproject.services.CourseDetailsService;
import com.app.graduationproject.services.CourseFetchService;
import com.app.graduationproject.services.VideoFetchService;
import com.app.graduationproject.utils.Constants;

import io.realm.Realm;


public class BaseFragment extends Fragment{

    private static final String TAG = "BaseFragment";

    public static final String EXTRA_COURSE_CODE = "course_code";
    protected RecyclerView mRecyclerView;
    public SwipeRefreshLayout mRefreshLayout;
    protected LocalBroadcastManager mLocalBroadcastManager;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.Adapter mAdapter;
    protected boolean mIsLoadingMore;
    protected boolean mIsRefreshing;
    protected Realm mRealm;
    protected boolean mIsNoMore;

    private UpdateResultReceiver updateResultReceiver;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        fetchDetails();
    }

    protected void initData(){
        mRealm = Realm.getDefaultInstance();
        updateResultReceiver = new UpdateResultReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocalBroadcastManager.registerReceiver(updateResultReceiver,new IntentFilter(CourseFetchService.ACTION_UPDATE_RESULT));
    }

    @Override
    public void onPause() {
        if (mRefreshLayout!=null) {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.destroyDrawingCache();
            mRefreshLayout.clearAnimation();
        }
        mLocalBroadcastManager.unregisterReceiver(updateResultReceiver);
        super.onPause();


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(),container,false);
        mRefreshLayout = $(view,getRefreshLayoutId());
        mRecyclerView = $(view,getRecyclerViewId());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置Item增加移除动画
        mLayoutManager = getLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = initAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mIsLoadingMore && dy >0){
                    int lastVisiblePos = getLastVisiblePos();
                    if(lastVisiblePos + 1 == mAdapter.getItemCount()){
                        loadingMore();
                    }
                }
            }
        });
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        return view;
    }

    private void loadingMore(){
        if(mIsLoadingMore)
            return;
        Intent intent = new Intent(getActivity(),CourseFetchService.class);
        intent.setAction(CourseFetchService.ACTION_FETCH_MORE);
        getActivity().startService(intent);
        mIsLoadingMore = true;
        setRefreshLayout(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               fetLatest();
            }
        };
        mRefreshLayout.setOnRefreshListener(listener);
        if(savedInstanceState == null){
            listener.onRefresh();
        }

    }

    private void fetLatest(){
        if(mIsRefreshing)
            return;
        Intent intent = new Intent(getActivity(),CourseFetchService.class);
        intent.setAction(CourseFetchService.ACTION_FETCH_REFRESH);
        getActivity().startService(intent);
        Intent intentVideo = new Intent(getActivity(), VideoFetchService.class);
        getActivity().startService(intentVideo);
        mIsRefreshing = true;
        setRefreshLayout(true);
    }


    private void fetchDetails(){
        Intent intent = new Intent(getActivity(), CourseDetailsService.class);
        getActivity().startService(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.removeAllChangeListeners();
        mRealm.close();
    }

    public void setRefreshLayout(final boolean state){
        if(mRefreshLayout == null){
            return;
        }
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(state);
            }
        });
    }

    public void setFetchingFlagsFalse(){
        if(mIsRefreshing)
            mIsRefreshing = false;
        if(mIsLoadingMore)
            mIsLoadingMore = false;
    }

    public void updateData(){
        if(null == mAdapter){
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean isFetching(){
        return mIsLoadingMore || mIsRefreshing;
    }

    protected <T extends View> T $(View view,int resId){
        return (T) view.findViewById(resId);
    }

    //protected abstract void loadingMore();

    protected  int getLastVisiblePos(){
        return ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
    }

    protected  RecyclerView.Adapter initAdapter(){
        final BaseAdapter adapter = new BaseAdapter(getActivity(),mRealm);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                return false;
            }

            @Override
            public void onItemClick(View v, int position) {
                if(mIsLoadingMore || mIsRefreshing)
                    return;
                final Course course = adapter.getCourseAt(position);
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class); //转到视频详情活动
                intent.putExtra(EXTRA_COURSE_CODE,course.getCode()); //携带课程号
                View transitionView = v.findViewById(R.id.iv);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        transitionView,getString(R.string.transition_video_img));
                ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
                //getActivity().startActivity(intent);
            }
        });
        return adapter;
    }

    protected RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    }

    protected int getRecyclerViewId(){
        return R.id.home_recycleview;
    }

    protected int getRefreshLayoutId(){
        return R.id.home_refresh_layout;
    }

    protected  int getLayoutResId(){
        return R.layout.home_fragment;
    }

    private class UpdateResultReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            final int fetched = intent.getIntExtra(CourseFetchService.EXTRA_FETCHED,0);
            String trigger = intent.getStringExtra(CourseFetchService.EXTRA_TRIGGER);
            final Constants.NETWORK_EXCEPTION networkException = (Constants.NETWORK_EXCEPTION)
                    intent.getSerializableExtra(CourseFetchService.EXTRA_EXCEPTION_CODE);
            if(fetched == 0 && trigger.equals(CourseFetchService.ACTION_FETCH_MORE)){
                mIsNoMore = true;
                Snackbar.make(mRefreshLayout,"没有更多内容了",Snackbar.LENGTH_SHORT).show();
            }

            setRefreshLayout(false);

            if(!TextUtils.isEmpty(networkException.getTipsResId())){
                setFetchingFlagsFalse();
                return;
            }

            if(mIsRefreshing && BaseFragment.this.isVisible()){
                updateData();
                Snackbar.make(mRefreshLayout,"已更新",Snackbar.LENGTH_SHORT).show();
                mRecyclerView.smoothScrollToPosition(0);
            }
            setFetchingFlagsFalse();

            if(null == mAdapter || fetched == 0)
                return;
           // ((BaseAdapter) mAdapter).updateInsertedData(fetched,trigger.equals(CourseFetchService.ACTION_FETCH_MORE));
            setRefreshLayout(false);
        }
    }

}



















































