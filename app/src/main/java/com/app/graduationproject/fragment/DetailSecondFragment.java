package com.app.graduationproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private void initView(View view) {
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDetailActivity activity = (VideoDetailActivity) getActivity();
                activity.setChoice(1);
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
}
