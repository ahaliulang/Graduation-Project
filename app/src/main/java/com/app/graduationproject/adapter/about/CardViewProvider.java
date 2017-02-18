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

public class CardViewProvider extends ItemViewProvider<Card,CardViewProvider.ViewHolder>{

    private final View.OnClickListener onActionClickListener;

    public CardViewProvider(View.OnClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.about_page_item_card,parent,false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Card card) {
        holder.content.setText(card.content);
        holder.action.setText(card.action);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView content;
        TextView action;

        ViewHolder(View itemView){
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            action = (TextView) itemView.findViewById(R.id.action);
            action.setOnClickListener(onActionClickListener);
        }
    }
}






























