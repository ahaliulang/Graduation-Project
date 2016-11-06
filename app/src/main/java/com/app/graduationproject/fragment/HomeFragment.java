package com.app.graduationproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.VideoDetailActivity;
import com.app.graduationproject.adapter.HomeRecyclerAdapter;
import com.app.graduationproject.services.CourseFetchService;

import io.realm.Realm;

/**
 * Created by lenovo on 2016/10/20.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private Realm mRealm;

    private boolean isRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycleview);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_refresh_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置移除动画
        mRecyclerView.setAdapter(initAdapter());

        return view;
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
                Intent intent = new Intent(getContext(), CourseFetchService.class);
                getContext().startService(intent);
            }
        };
        mRefreshLayout.setOnRefreshListener(listener);
        if(savedInstanceState == null){
            mRefreshLayout.setRefreshing(true);
            listener.onRefresh();
        }



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

    private RecyclerView.Adapter initAdapter() {
        final HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(getActivity(),mRealm);
        adapter.setOnItemClickListener(new HomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                Toast.makeText(getActivity(), "你长按了item", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                startActivity(intent);
            }
        });
        return adapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}































