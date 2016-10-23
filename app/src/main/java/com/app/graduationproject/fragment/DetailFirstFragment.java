package com.app.graduationproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.VideoDetailActivity;
import com.app.graduationproject.adapter.VideoDetailAdapter;

/**
 * Created by lenovo on 2016/10/23.
 */
public class DetailFirstFragment extends Fragment{

    private ImageView more;   //查看更过按钮
    private RecyclerView mRecyclerView; //显示的部分课程列表
    private Button loadingList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_detail_first_fragment,container,false);
        initView(view);
        mRecyclerView.setAdapter(initAdapter());
        return view;
    }

    private void initView(View view) {
        more = (ImageView)view.findViewById(R.id.click);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (more.getTag().equals(1)) {
                    more.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    more.setTag(2);
                } else {
                    more.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    more.setTag(1);
                }
            }
        });
        loadingList = (Button) view.findViewById(R.id.more);
        loadingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDetailActivity activity = (VideoDetailActivity) getActivity();
                activity.setChoice(2);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置移除动画
    }

    private RecyclerView.Adapter initAdapter(){
        final VideoDetailAdapter adapter = new VideoDetailAdapter(getContext());
        adapter.setOnItemClickListener(new VideoDetailAdapter.OnItemClickListener() {
            @Override
            public boolean onItemLongClick(View v, int position) {
                Toast.makeText(getContext(),"你长按了item",Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getContext(),"你点击了item",Toast.LENGTH_SHORT).show();
            }
        });
        return adapter;
    }
}
