package com.app.graduationproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.graduationproject.R;

import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */

public class AddCourseMainAdapter extends android.widget.BaseAdapter{

    private Context context;
    private List<String> list;
    private int position = 0;

    Holder holder;

    public AddCourseMainAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = View.inflate(context,R.layout.item_addcourse_mainlist,null);
            holder = new Holder(view);
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }
        holder.txt.setText(list.get(i).toString());
        holder.layout.setBackgroundColor(0xFFEBEBEB);
        if(i == position){
            holder.layout.setBackgroundColor(0xFFFFFFFF);
        }
        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
    }

    public int getSelectItem() {
        return position;
    }

   private static class Holder{
        TextView txt;
       LinearLayout layout;

       public Holder(View view) {
           txt = (TextView) view.findViewById(R.id.mainitem_txt);
           layout = (LinearLayout) view.findViewById(R.id.mainitem_layout);
       }
   }
}








































