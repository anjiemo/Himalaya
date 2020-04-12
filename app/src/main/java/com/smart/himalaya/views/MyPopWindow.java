package com.smart.himalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smart.himalaya.R;
import com.smart.himalaya.adapters.PlayListAdapter;
import com.smart.himalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class MyPopWindow extends PopupWindow {

    private final View mPopView;
    private TextView mCloseBtn;
    private RecyclerView mTracksList;
    private LinearLayoutManager mLayoutManager;
    private PlayListAdapter mPlayListAdapter;

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
        //先找到控件
        mTracksList = mPopView.findViewById(R.id.play_list_rv);
        //设置布局管理器
        mLayoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        mTracksList.setLayoutManager(mLayoutManager);
        //创建适配器
        mPlayListAdapter = new PlayListAdapter();
        //设置适配器
        mTracksList.setAdapter(mPlayListAdapter);
    }

    private void initEvent() {
        //点击关闭以后，窗口消失
        mCloseBtn.setOnClickListener(v -> dismiss());
    }

    /**
     * 给适配器设置数据
     *
     * @param data
     */
    public void setListData(List<Track> data) {
        if (mPlayListAdapter != null) {
            mPlayListAdapter.setData(data);
        }
    }

    public void setCurrentPlayPosition(int position) {
        if (mPlayListAdapter != null) {
            mPlayListAdapter.setCurrentPlayPosition(position);
        }
    }
}
