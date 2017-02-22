package com.app.graduationproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.ShowVideoActivity;
import com.app.graduationproject.activity.VideoDetailActivity;
import com.app.graduationproject.adapter.ListAllAdapter;
import com.app.graduationproject.db.Video;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by lenovo on 2016/10/23.
 */
public class DetailSecondFragment extends Fragment{

    public static final String ACTION_SECOND_VIDEO = "com.app.graduationproject.fragment.second_fragment";
    public static final String EXTRA_SECOND = "video_second";

    private static final String TAG = "DetailSecondFragment";

    private ImageView back;
    private ListView listAll;
    //private List<Clip> clipList = new ArrayList<Clip>();
    private RealmResults<Video> videos;
    private Realm mRealm;
    private String courseCode;
    private SharedPreferences mSharedPreferences; //存储上次播放位置
    private int index; //选中的index，即上次播放的位置


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        mRealm = Realm.getDefaultInstance();
        courseCode = getActivity().getIntent().getStringExtra(BaseFragment.EXTRA_COURSE_CODE);
        mSharedPreferences = getActivity().getSharedPreferences(courseCode, Context.MODE_PRIVATE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        Log.d(TAG, "onDestroyOptionsMenu: ");
    }
    
    

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + "S");
        View view = inflater.inflate(R.layout.video_detail_second_fragment,container,false);
        initView(view);
        initData();
        ListAllAdapter allAdapter = new ListAllAdapter(getContext(),R.layout.video_list_all_item,videos);
        index = mSharedPreferences.getInt("index",0);



        listAll.setAdapter(allAdapter);
        listAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.putInt("index",position);
                editor.commit();

                Video video = videos.get(position);
                Intent intent = new Intent(getActivity(), ShowVideoActivity.class);
                intent.putExtra(ShowVideoActivity.EXTRA_VIDEO_URL,video.getUrl());
                startActivity(intent);

                for(int i=0;i<adapterView.getCount();i++){
                    View v=adapterView.getChildAt(i);
                    if (position == i) {
                        v.setBackgroundColor(getResources().getColor(R.color.colorGray));
                    } else {
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                }

            }
        });
      /*  listAll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.putInt("index",position);
                editor.commit();

                for(int i=0;i<adapterView.getCount();i++){
                    View v=adapterView.getChildAt(i);
                    if (position == i) {
                        v.setBackgroundColor(getResources().getColor(R.color.colorDeepGreen));
                    } else {
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setListViewHeightBaseOnChildren(listAll);
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
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
        videos = Video.fromCode(mRealm, courseCode);
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


























