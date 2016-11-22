package com.app.graduationproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.db.Course;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import io.realm.Realm;

/**
 * Created by lenovo on 2016/10/25.
 */
public class CategoryAdapter extends ArrayAdapter<Course>{

    private int resourceId;
    private Realm mRealm;
    private Context mContext;


    private String mCategory;

    public CategoryAdapter(Context context, int resource, List<Course> objects,Realm realm, String category) {
        super(context, resource,objects);
        this.mContext = context;
        this.mRealm = realm;
        this.mCategory = category;
       // detailList = CourseDetails.fromCate(mRealm,mCategory); //通过类别获得课程详细类
        resourceId = resource;
    }






    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // final Category category = getItem(position); //获取当前项Category实例
      //  details = detailList.get(position);
       // course = Course.fromCode(mRealm,details.getCode());
        final Course course = getItem(position);
        View view;
        ViewHolder  viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.video_clip = (TextView) view.findViewById(R.id.video_clip);
            viewHolder.people = (TextView) view.findViewById(R.id.people);
            viewHolder.score = (TextView) view.findViewById(R.id.score);
            viewHolder.courseCode = (TextView) view.findViewById(R.id.courseCode);
            view.setTag(viewHolder); //将viewHolder 存储在View中
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
       // viewHolder.thumbnail.setImageResource(category.getImageId());
        Glide.with(mContext)
                .load(Uri.parse(course.getImgurl()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.thumbnail);
        viewHolder.title.setText(course.getName());
        viewHolder.video_clip.setText(course.getStudy());
        viewHolder.people.setText(course.getVisit());
        viewHolder.courseCode.setText(course.getCode());
        switch (course.getScore()){
            case "5":
                viewHolder.score.setText("☆☆☆☆☆");
                break;
            case "4":
                viewHolder.score.setText("☆☆☆☆");
                break;
            case "3":
                viewHolder.score.setText("☆☆☆");
                break;
            case "2":
                viewHolder.score.setText("☆☆");
                break;
            case "1":
                viewHolder.score.setText("☆");
                break;
            default:
                viewHolder.score.setText("☆");
                break;
        }
        return view;
    }
    class ViewHolder{
        ImageView thumbnail;
        TextView title;
        TextView courseCode;
        TextView video_clip;
        TextView people;
        TextView score;
    }
}
















