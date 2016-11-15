package com.app.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.db.Video;

import io.realm.RealmResults;

/**
 * Created by lenovo on 2016/10/23.
 * 全部列表的item
 */
public class ListAllAdapter extends ArrayAdapter<Video> {

    private int resourceId;

    public ListAllAdapter(Context context, int resource, RealmResults<Video> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
       // Clip clip = getItem(position);
        Video video = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView number = (TextView) view.findViewById(R.id.number);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView title = (TextView) view.findViewById(R.id.title);
        number.setText((++position)+"");
        time.setText(video.getTime());
        title.setText(video.getName());
        return view;
    }


}
