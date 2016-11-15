package com.app.graduationproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.db.Course;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by lenovo on 2016/11/4.
 */
public  class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder>{

    private static final String TAG = "BaseAdapter";
    protected final Context mContext;
    protected final Realm mRealm;
    protected RealmResults<Course> mCourses;
    protected int lastCoursesNum;
    private OnItemClickListener mOnItemClickListener;

    public BaseAdapter(Context context,Realm realm){
        this.mContext = context;
        this.mRealm = realm;
        initCourses(mRealm);
        lastCoursesNum = mCourses.size();
        Log.e("JJ",lastCoursesNum+"");
        setHasStableIds(true);

    }

    public void updateInsertedData(int numImages,boolean isMore){
        if(isMore){
            notifyItemRangeInserted(0,numImages);
        }else {
            notifyItemRangeInserted(0,numImages);
        }
        lastCoursesNum += numImages;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.home_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        for(int i=0;i<lastCoursesNum;i++){
            Course course = mCourses.get(i);
            Log.e("FF",course.getCode());
        }
        final Course course = mCourses.get(position);

        Log.e("TAN",course.getCode()+"---"+course.getImgurl());
        Glide.with(mContext)
                .load(Uri.parse(course.getImgurl()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.title.setText(course.getName());
        holder.where.setText("广州医科大学");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null)
                    return mOnItemClickListener.onItemLongClick(v, position);

                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return mCourses.get(position).getCode().hashCode();
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        boolean onItemLongClick(View v,int position);
        void onItemClick(View v,int position);
    }

    protected  void initCourses(Realm realm){
        mCourses = Course.all(realm);
    }


    public Course getCourseAt(int pos) {
        return mCourses.get(pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView item;
        ImageView imageView;
        TextView title,where;
        public ViewHolder(View itemView) {
            super(itemView);
            item = (CardView) itemView.findViewById(R.id.video_cardview);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.title);
            where = (TextView) itemView.findViewById(R.id.where);
        }
    }
}
