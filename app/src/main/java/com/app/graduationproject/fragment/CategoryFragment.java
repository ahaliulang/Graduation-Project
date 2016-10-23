package com.app.graduationproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.app.graduationproject.R;
import com.app.graduationproject.adapter.GridviewAdapter;
import com.app.graduationproject.utils.Constans;
import com.app.graduationproject.view.ImageCycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/22.
 */
public class CategoryFragment extends Fragment{

    private ImageCycleView mImageCycleView;
    private List<ImageCycleView.ImageInfo> list;
    private List<String> data_list;
    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment,container,false);
        list = new ArrayList<ImageCycleView.ImageInfo>();
        //添加图片资源用作轮播
        list.add(new ImageCycleView.ImageInfo(R.drawable.test,"内科学|广州医科大学",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.test2,"基能实验室|广州医科大学",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.test3,"精品视频公开课：医患关系及其伦理难题",""));
        mImageCycleView = (ImageCycleView) view.findViewById(R.id.scrollimage);

        mImageCycleView.loadData(list, new ImageCycleView.LoadImageCallBack() {
            @Override
            public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {
                //本地图片
                ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(Integer.parseInt(imageInfo.image.toString()));
                return imageView;
            }
        });

        mGridView = (GridView) view.findViewById(R.id.gview);
        GridviewAdapter adapter = new GridviewAdapter(getActivity(),R.layout.gridview_item,Constans.CATEGORY_NAME);
        mGridView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
