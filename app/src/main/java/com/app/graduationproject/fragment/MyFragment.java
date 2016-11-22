package com.app.graduationproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.graduationproject.R;
import com.app.graduationproject.activity.LoginActivity;

/**
 * Created by lenovo on 2016/10/26.
 */
public class MyFragment extends Fragment{

    private LinearLayout loginLayout;
    private LinearLayout study;
    private LinearLayout add;
    private LinearLayout fellow;
    private LinearLayout info;
    private LinearLayout updatePwd;
    private LinearLayout logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment,container,false);
        loginLayout = (LinearLayout) view.findViewById(R.id.loginlayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
