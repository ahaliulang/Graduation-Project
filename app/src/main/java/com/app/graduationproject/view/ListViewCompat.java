package com.app.graduationproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/12/24.
 */

public class ListViewCompat extends ListView{

    private static final String TAG = "ListViewCompat";

    private SlideView mFocusedItemView;

    public ListViewCompat(Context context) {
        super(context);
    }

    public ListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void shrinkListItem(int position){
        View item = getChildAt(position);
        if(item != null){
            try {
                ((SlideView)item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int position = pointToPosition(x,y);
                Log.e(TAG, "positon=" + position);
                if(position != INVALID_POSITION){
                    CourseItem data = (CourseItem) getItemAtPosition(position);
                    mFocusedItemView = data.slideView;
                }
            }
            default:
                break;
            }
        if(mFocusedItemView != null){
            //mFocusedItemView.onRequireTouchEvent(ev);
            mFocusedItemView.onRequireTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }



    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }
}






























