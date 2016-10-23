package com.app.graduationproject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.graduationproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/10/23.
 */
public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailAdapter.MyViewHolder> {

    private final Context mContext;
    private OnItemClickListener mOnItemClickListener; //提供回调接口click 和 longclick
    private List<Boolean> isClicks; //控件是否被点击，默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public VideoDetailAdapter(Context context) {
        this.mContext = context;
        isClicks = new ArrayList<>();
        for(int i=0;i<4;i++){
            isClicks.add(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.video_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.number.setText("1");
        holder.time.setText("1:45:32");
        holder.title.setText("呼吸疾病总论");
        if(isClicks.get(position)){
            holder.item.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorDeepGreen));
        }else {
            holder.item.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                    for(int i = 0;i<4;i++){
                        isClicks.set(i,false);
                    }
                    isClicks.set(position,true);
                    notifyDataSetChanged();
                }
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemClickListener != null) {
                    return mOnItemClickListener.onItemLongClick(view, position);
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
