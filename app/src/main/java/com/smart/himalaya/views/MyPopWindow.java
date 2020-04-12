package com.smart.himalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smart.himalaya.R;
import com.smart.himalaya.adapters.PlayListAdapter;
import com.smart.himalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class MyPopWindow extends PopupWindow {

    private final View mPopView;
    private TextView mCloseBtn;
    private RecyclerView mTracksList;
    private LinearLayoutManager mLayoutManager;
    private PlayListAdapter mPlayListAdapter;
    private ImageView mPlayModeIv;
    private TextView mPlayModeTv;
    private LinearLayout mPlayModeContainer;
    private PlayListModeClickListener mPlayModeClickListener = null;

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
        //播放模式相关
        mPlayModeTv = mPopView.findViewById(R.id.play_list_mode_tv);
        mPlayModeIv = mPopView.findViewById(R.id.player_list_mode_iv);
        mPlayModeContainer = mPopView.findViewById(R.id.play_list_mode_container);
    }

    private void initEvent() {
        //点击关闭以后，窗口消失
        mCloseBtn.setOnClickListener(v -> dismiss());
        mPlayModeContainer.setOnClickListener(v -> {
            //切换播放模式
            if (mPlayModeClickListener != null) {
                mPlayModeClickListener.onPlayModeClick();
            }
        });
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
            mTracksList.scrollToPosition(position);
        }
    }

    public void setPlayListItemClickListener(PlayListItemClickListener listener) {
        mPlayListAdapter.setOnItemClickListener(listener);
    }

    /**
     * 更新播放列表播放模式
     *
     * @param currentMode
     */
    public void updatePlayMode(XmPlayListControl.PlayMode currentMode) {
        updatePlayModeBtnImg(currentMode);
    }

    /**
     * 根据当前的状态，更新播放模式图标
     * PLAY_MODEL_LIST
     * PLAY_MODEL_LIST_LOOP
     * PLAY_MODEL_RANDOM
     * PLAY_MODEL_SINGLE_LOOP
     */
    private void updatePlayModeBtnImg(XmPlayListControl.PlayMode playMode) {
        int resId = R.drawable.selector_player_mode_list_order;
        int textId = R.string.play_mode_list_play_text;
        switch (playMode) {
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_player_mode_list_order;
                textId = R.string.play_mode_order_text;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_player_mode_random;
                textId = R.string.play_mode_random_text;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_order_looper;
                textId = R.string.play_mode_list_play_text;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_player_mode_single_loop;
                textId = R.string.play_mode_single_play_text;
                break;
        }
        mPlayModeIv.setImageResource(resId);
        mPlayModeTv.setText(textId);
    }

    public interface PlayListItemClickListener {
        void onItemClick(int position);
    }

    public void setPlayListModeClickListener(PlayListModeClickListener playModeListener) {
        mPlayModeClickListener = playModeListener;
    }

    public interface PlayListModeClickListener {
        void onPlayModeClick();
    }
}
