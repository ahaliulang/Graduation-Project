package com.app.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.entity.Category;

import java.util.List;

/**
 * Created by lenovo on 2016/10/25.
 */
public class CategoryAdapter extends ArrayAdapter<Category>{

    private int resourceId;

    public CategoryAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position); //获取当前项Category实例
        View view;
        ViewHolder  viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.video_clip = (TextView) view.findViewById(R.id.video_clip);
            viewHolder.people = (TextView) view.findViewById(R.id.people);
            view.setTag(viewHolder); //将viewHolder 存储在View中
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.thumbnail.setImageResource(category.getImageId());
        viewHolder.title.setText(category.getTitle());
        viewHolder.video_clip.setText(category.getVideo_clip());
        viewHolder.people.setText(category.getPeople());
        return view;
    }
    class ViewHolder{
        ImageView thumbnail;
        TextView title;
        TextView video_clip;
        TextView people;
    }
}
















