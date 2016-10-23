package com.app.graduationproject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;

/**
 * Created by lenovo on 2016/10/20.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder>{

    private final Context mContext;
    private OnItemClickListener mOnItemClickListener; //提供回调接口click 和 longclick

    public HomeRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.home_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.imageView.setImageResource(R.drawable.test);
        holder.title.setText("内科学");
        holder.where.setText("广州医科大学");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(view,position);
                }
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemClickListener != null){
                    return mOnItemClickListener.onItemLongClick(view,position);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        //TODO-先返回10条数据以作测试
        return 10;
    }

    public interface OnItemClickListener{
        boolean onItemLongClick(View v,int position);
        void onItemClick(View v,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CardView item;
        ImageView imageView;
        TextView title,where;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = (CardView) itemView.findViewById(R.id.video_cardview);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.title);
            where = (TextView) itemView.findViewById(R.id.where);
        }
    }

}
