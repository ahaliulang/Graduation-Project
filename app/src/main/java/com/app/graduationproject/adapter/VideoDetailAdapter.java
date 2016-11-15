package com.app.graduationproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.db.Video;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by lenovo on 2016/10/23.
 */
public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailAdapter.MyViewHolder> {

    private final Context mContext;
    private final Realm mRealm;
    private OnItemClickListener mOnItemClickListener; //提供回调接口click 和 longclick
    private List<Boolean> isClicks; //控件是否被点击，默认为false，如果被点击，改变值，控件根据值改变自身颜色
    private RealmResults<Video> mVideo;
    private int selectIndex=0;
    private SharedPreferences sp;
    private int id;

    boolean flag = true; //首次选中

    public VideoDetailAdapter(Context context, Realm realm,String courseCode) {

        this.mContext = context;
        this.mRealm = realm;
        setHasStableIds(true);
        isClicks = new ArrayList<>();
        mVideo = Video.fromCode(mRealm,courseCode);
        for (int i = 0; i < mVideo.size(); i++) {
            isClicks.add(false);
        }
        sp = context.getSharedPreferences("videoindex", Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.video_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        selectIndex = sp.getInt("index",0);
        final int index ;
        if(selectIndex == 0){
            index = position;
        }else if(selectIndex == mVideo.size()-1){
            index = selectIndex - 3+position;

        }else if(selectIndex == mVideo.size() - 2){
            index = selectIndex - 2+position;

        }else if(selectIndex == mVideo.size()-3){
            index = selectIndex - 1+position;

        }else{
            index = selectIndex+position-1;
        }
        Log.e("TAG",mVideo.size()+"");


        if(flag){
            isClicks.set(selectIndex,true);
            flag = false;
        }
        id=position;
        final Video video = mVideo.get(index);
        holder.number.setText((++id)+"");
        holder.time.setText(video.getTime());
        holder.title.setText(video.getName());
        if (isClicks.get(index) ) {
            holder.item.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorDeepGreen));
        } else {
            holder.item.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        }


        holder.item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, index);
                    for (int i = 0; i < mVideo.size(); i++) {
                        isClicks.set(i, false);

                    }
                    isClicks.set(selectIndex,false);

                    isClicks.set(index,true);
                    //将新的选中index存入SharePreference中
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.putInt("index",index);
                    editor.commit();
                    notifyDataSetChanged();


                }
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemClickListener != null) {
                    return mOnItemClickListener.onItemLongClick(view, index);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return 4;
    }

    public interface OnItemClickListener {
        boolean onItemLongClick(View v, int position);

        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView item;
        TextView number, title, time;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = (CardView) itemView.findViewById(R.id.video_list_cardview);
            number = (TextView) itemView.findViewById(R.id.number);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
