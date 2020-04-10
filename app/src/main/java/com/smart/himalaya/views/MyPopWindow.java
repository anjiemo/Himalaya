package com.smart.himalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smart.himalaya.R;
import com.smart.himalaya.base.BaseApplication;

public class MyPopWindow extends PopupWindow {

    private final View mPopView;
    private TextView mCloseBtn;

    public MyPopWindow() {
        //设置它的宽高
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //这里要注意，设置setOutsideTouchable之前，先要设置：setBackgroundDrawable，
        //否则点击外部无法关闭pop。
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        //载进来View
        mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null);
        //设置内容
        setContentView(mPopView);
        //设置窗口进入和退出的动画
        setAnimationStyle(R.style.pop_animation);
        initView();
        initEvent();
    }

    private void initView() {
        mCloseBtn = mPopView.findViewById(R.id.play_list_close_btn);
    }

    private void initEvent() {
        //点击关闭以后，窗口消失
        mCloseBtn.setOnClickListener(v -> dismiss());
    }
}
