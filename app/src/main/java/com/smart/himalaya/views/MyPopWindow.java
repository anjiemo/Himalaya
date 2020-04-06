package com.smart.himalaya.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.smart.himalaya.R;
import com.smart.himalaya.base.BaseApplication;

public class MyPopWindow  extends PopupWindow {
    public MyPopWindow() {
        //设置它的宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //载进来
        View popView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
        //设置内容
        setContentView(popView);
    }
}
