package com.app.graduationproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.CategoryVideoListActivity;
import com.app.graduationproject.adapter.GridviewAdapter;
import com.app.graduationproject.utils.Constants;
import com.app.graduationproject.view.ImageCycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/22.
 */
public class CategoryFragment extends Fragment{


    public static final String EXTRA_CATEGORY = "extra_category";

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
        list.add(new ImageCycleView.ImageInfo(R.drawable.test,"周易精解|YuntuEdu",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.test2,"精品视频公开课：远离呼吸系统疾病",""));
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
        GridviewAdapter adapter = new GridviewAdapter(getActivity(),R.layout.gridview_item, Constants.CATEGORY_NAME);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view.findViewById(R.id.name);
                String category = text.getText().toString();
                Log.e("cate",category);
                Intent intent = new Intent(getActivity(), CategoryVideoListActivity.class);
                intent.putExtra(EXTRA_CATEGORY,category);

                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
