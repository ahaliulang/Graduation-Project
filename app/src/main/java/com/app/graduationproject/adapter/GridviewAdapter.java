package com.app.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.graduationproject.R;
import com.app.graduationproject.utils.Constants;

/**
 * Created by lenovo on 2016/10/22.
 */
public class GridviewAdapter extends ArrayAdapter<String>{

    private int resourceId;

    public GridviewAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(Constants.CATEGORY_NAME[position]);
        return view;
    }
}
