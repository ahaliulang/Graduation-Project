package com.app.graduationproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by lenovo on 2016/10/20.
 */
public class HomeFragment extends Fragment{

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycleview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置移除动画
        mRecyclerView.setAdapter(initAdapter());
        return view;
    }

    private RecyclerView.Adapter initAdapter(){
        final HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(getActivity());
        adapter.setOnItemClickListener(new HomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                Toast.makeText(getActivity(),"你长按了item",Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),VideoDetailActivity.class);
                startActivity(intent);
            }
        });
        return adapter;
    }


}































