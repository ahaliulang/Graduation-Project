package com.app.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.entity.Clip;

import java.util.List;

/**
 * Created by lenovo on 2016/10/23.
 * 全部列表的item
 */
public class ListAllAdapter extends ArrayAdapter<Clip>{

    private int resourceId;

    public ListAllAdapter(Context context, int resource, List<Clip> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Clip clip = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView number = (TextView) view.findViewById(R.id.number);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView title = (TextView) view.findViewById(R.id.title);
        number.setText(clip.getNumber());
        time.setText(clip.getTime());
        title.setText(clip.getTitle());
        return view;
    }
}
