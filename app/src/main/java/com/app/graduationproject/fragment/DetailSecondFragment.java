package com.app.graduationproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.VideoDetailActivity;
import com.app.graduationproject.adapter.ListAllAdapter;
import com.app.graduationproject.entity.Clip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/23.
 */
public class DetailSecondFragment extends Fragment{

    private ImageView back;
    private ListView listAll;
    private List<Clip> clipList = new ArrayList<Clip>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_detail_second_fragment,container,false);
        initView(view);
        initData();
        ListAllAdapter allAdapter = new ListAllAdapter(getContext(),R.layout.video_list_all_item,clipList);
        listAll.setAdapter(allAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setListViewHeightBaseOnChildren(listAll);
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDetailActivity activity = (VideoDetailActivity) getActivity();
                activity.onBackPressed();
            }
        });
        listAll = (ListView) view.findViewById(R.id.video_list_all);
    }

    private void initData(){
        for(int i = 0 ;i<17;i++){
            Clip clip = new Clip("第"+i+"集","内分泌","53:10");
            clipList.add(clip);
        }
    }

    /**
     * 调整ListView的显示高度，scrollView嵌套ListView只显示一行
     * @param listView
     */
    public void setListViewHeightBaseOnChildren(ListView listView){
        //获取ListView对应的Adapter
        ListAdapter adapter = listView.getAdapter();
        if(adapter == null){
            return;
        }
        int totalHeight = 0;
        for(int i=0,len=adapter.getCount();i<len;i++){
            //listAdapter.getCount()返回数据的条目
            View listItem = adapter.getView(i,null,listView);
            //计算子项的总高度
            listItem.measure(0,0);
            //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight()*(adapter.getCount()-1));
        //ListView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}


























