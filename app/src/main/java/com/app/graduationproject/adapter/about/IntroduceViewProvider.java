package com.app.graduationproject.adapter.about;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.graduationproject.R;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by ahaliulang on 2017/2/18.
 */

public class IntroduceViewProvider extends ItemViewProvider<Introduce,IntroduceViewProvider.ViewHolder>{


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.about_page_item_introduce, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Introduce introduce) {
        holder.introduce.setText(introduce.value);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView introduce;

        ViewHolder(View itemView){
            super(itemView);
            introduce = (TextView) itemView.findViewById(R.id.introduce);
        }
    }
}



































