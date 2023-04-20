package com.smart.himalaya.ui.activity;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.smart.himalaya.R;
import com.smart.himalaya.adapters.PlayerTrackPagerAdapter;
import com.smart.himalaya.base.BaseActivity;
import com.smart.himalaya.beans.Music;
import com.smart.himalaya.interfaces.IPlayerCallback;
import com.smart.himalaya.presenters.PlayerPresenter;
import com.smart.himalaya.utils.CoverLoader;
import com.smart.himalaya.utils.LogUtil;
import com.smart.himalaya.views.AlbumCoverView;
import com.smart.himalaya.views.MyMarqueeView;
import com.smart.himalaya.views.MyPopWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressLint("SimpleDateFormat")
public class PlayerActivity extends BaseActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {

    private static final String TAG = "PlayerActivity";
    private final SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    private final SimpleDateFormat mHourFormat = new SimpleDateFormat("HH:mm:ss");
    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;
    private TextView mTotalDuration;
    private TextView mCurrentPosition;
    private SeekBar mDurationBar;
    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;
    private ImageView mPlayPreBtn;
    private ImageView mPlayNextBtn;
    private MyMarqueeView mTrackTitleTv;
    private String mTrackTitleText;
    private ImageView mPlayingBgIv;
    private AlbumCoverView mAlbumCoverView;
    private ViewPager mTrackPageView;
    private PlayerTrackPagerAdapter mTrackPagerAdapter;
    private boolean mIsUserSlidePager = false;
    private ImageView mPlayerModeSwitchBtn;

    private XmPlayListControl.PlayMode mCurrentMode = PLAY_MODEL_LIST;
    //
    private static final Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> sPlayModeRule = new HashMap<>();

    //处理播放模式的切换
    //1、默认的是：PLAY_MODEL_LIST
    //2、列表循环：PLAY_MODEL_LIST_LOOP
    //3、随机播放：PLAY_MODEL_RANDOM
    //4、单曲循环：PLAY_MODEL_SINGLE_LOOP
    static {
        sPlayModeRule.put(PLAY_MODEL_LIST, PLAY_MODEL_LIST_LOOP);
        sPlayModeRule.put(PLAY_MODEL_LIST_LOOP, PLAY_MODEL_RANDOM);
        sPlayModeRule.put(PLAY_MODEL_RANDOM, PLAY_MODEL_SINGLE_LOOP);
        sPlayModeRule.put(PLAY_MODEL_SINGLE_LOOP, PLAY_MODEL_LIST);
    }

    private ImageView mPlayerListBtn;
    private MyPopWindow mMyPopWindow;
    private ValueAnimator mEnterBgAnimator;
    private ValueAnimator mOutBgAnimator;
    public final int BG_ANIMATION_DURATION = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        fullWindow();
        initView();
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        initCoverLrc();
        initEvent();
        initBgAnimation();
    }

    private void initCoverLrc() {
        mAlbumCoverView.initNeedle(mPlayerPresenter.isPlaying());
    }

    private void initBgAnimation() {
        mEnterBgAnimator = ValueAnimator.ofFloat(1.0f, 0.7f);
        mEnterBgAnimator.setDuration(BG_ANIMATION_DURATION);
        mEnterBgAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            //处理一下背景，有点透明度
            updateBgAlpha(value);
        });
        //退出的
        mOutBgAnimator = ValueAnimator.ofFloat(0.7f, 1.0f);
        mOutBgAnimator.setDuration(BG_ANIMATION_DURATION);
        mOutBgAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            updateBgAlpha(value);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }

    /**
     * 给控件设置相关的事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        mControlBtn.setOnClickListener(v -> {
            //如果现在的状态是正在播放的，那么就暂停
            if (mPlayerPresenter.isPlaying()) {
                mPlayerPresenter.pause();
            } else {
                //如果现在的状态是非播放的，那么我们就让播放器播放节目
                mPlayerPresenter.play();
            }
        });
        mDurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = false;
                //手离开拖动进度条的时候更新进度
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });
        mPlayPreBtn.setOnClickListener(v -> {
            //播放上一个节目
            if (mPlayerPresenter != null) {
                mPlayerPresenter.playPre();
            }
        });
        mPlayNextBtn.setOnClickListener(v -> {
            //播放下一个节目
            if (mPlayerPresenter != null) {
                mPlayerPresenter.playNext();
            }
        });
        mTrackPageView.addOnPageChangeListener(this);
        mTrackPageView.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mIsUserSlidePager = true;
                    break;
            }
            return false;
        });
        mPlayerModeSwitchBtn.setOnClickListener(v -> switchPlayMode());
        mPlayerListBtn.setOnClickListener(v -> {
            //展示播放列表
            mMyPopWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            //修改背景的透明度，有一个渐变的过程
            mEnterBgAnimator.start();
        });
        mMyPopWindow.setOnDismissListener(() -> {
            //pop窗体消失以后，恢复透明度
            mOutBgAnimator.start();
        });
        mMyPopWindow.setPlayListItemClickListener(position -> {
            //说明播放列表里的Item被点击了
            if (mPlayerPresenter != null) {
                mPlayerPresenter.playByIndex(position);
            }
        });
        mMyPopWindow.setPlayListActionListener(new MyPopWindow.PlayListActionClickListener() {
            @Override
            public void onPlayModeClick() {
                //切换播放模式
                switchPlayMode();
            }

            @Override
            public void onOrderClick() {
                //点击了切换顺序和逆序
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.reversePlayList();
                }
            }
        });
        mAlbumCoverView.setOnClickListener(v -> {
            boolean isPlaying = mPlayerPresenter.isPlaying();
            if (isPlaying) {
                mPlayerPresenter.pause();
                mAlbumCoverView.pause();
            } else {
                mPlayerPresenter.play();
                mAlbumCoverView.start();
            }
        });
    }

    private void switchPlayMode() {
        //根据当前的mode获取到下一个mode
        XmPlayListControl.PlayMode playMode = sPlayModeRule.get(mCurrentMode);
        //修改播放模式
        if (mPlayerPresenter != null) {
            mPlayerPresenter.switchPlayMode(playMode);
        }
    }

    public void updateBgAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = alpha;
        window.setAttributes(attributes);
    }

    /**
     * 根据当前的状态，更新播放模式图标
     * PLAY_MODEL_LIST
     * PLAY_MODEL_LIST_LOOP
     * PLAY_MODEL_RANDOM
     * PLAY_MODEL_SINGLE_LOOP
     */
    private void updatePlayModeBtnImg() {
        int resId = R.drawable.selector_player_mode_list_order;
        switch (mCurrentMode) {
            case PLAY_MODEL_LIST:
                resId = R.drawable.selector_player_mode_list_order;
                break;
            case PLAY_MODEL_RANDOM:
                resId = R.drawable.selector_player_mode_random;
                break;
            case PLAY_MODEL_LIST_LOOP:
                resId = R.drawable.selector_player_mode_list_order_looper;
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                resId = R.drawable.selector_player_mode_single_loop;
                break;
        }
        mPlayerModeSwitchBtn.setImageResource(resId);
    }

    /**
     * 找到各个控件
     */
    private void initView() {
        mControlBtn = findViewById(R.id.play_or_pause_btn);
        mTotalDuration = findViewById(R.id.track_duration);
        mCurrentPosition = findViewById(R.id.current_position);
        mDurationBar = findViewById(R.id.track_seek_bar);
        mPlayPreBtn = findViewById(R.id.play_pre);
        mPlayNextBtn = findViewById(R.id.player_next);
        mTrackTitleTv = findViewById(R.id.track_title);
        if (!TextUtils.isEmpty(mTrackTitleText)) {
            mTrackTitleTv.setText(mTrackTitleText);
        }
        mPlayingBgIv = findViewById(R.id.play_page_bg_iv);
        mAlbumCoverView = findViewById(R.id.album_cover_view);

        mTrackPageView = findViewById(R.id.track_pager_view);
        //创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        //设置适配器
        mTrackPageView.setAdapter(mTrackPagerAdapter);
        //切换播放模式的按钮
        mPlayerModeSwitchBtn = findViewById(R.id.player_mode_switch_btn);
        //播放器列表
        mPlayerListBtn = findViewById(R.id.player_list);
        mMyPopWindow = new MyPopWindow();
    }

    @Override
    public void onPlayStart() {
        //开始播放，修改UI层暂停的按钮
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_player_pause);
        }
        if (mAlbumCoverView != null) {
            mAlbumCoverView.start();
        }
    }

    @Override
    public void onPlayPause() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_player_play);
        }
        if (mAlbumCoverView != null) {
            mAlbumCoverView.pause();
        }
    }

    @Override
    public void onPlayStop() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.drawable.selector_player_pause);
        }
        if (mAlbumCoverView != null) {
            mAlbumCoverView.pause();
        }
    }

    @Override
    public void onPlayError(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoad(List<Track> list) {
        LogUtil.d(TAG, "list --- > " + list);
        //把数据设置到适配器里
        if (mTrackPagerAdapter != null) {
            mTrackPagerAdapter.setData(list);
        }
        //数据回来以后，也要给节目列表一份
        if (mMyPopWindow != null) {
            mMyPopWindow.setListData(list);
        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {
        //更新播放模式，并且修改UI。
        mCurrentMode = playMode;
        //更新pop里的播放模式
        mMyPopWindow.updatePlayMode(mCurrentMode);
        updatePlayModeBtnImg();
    }

    @Override
    public void onProgressChange(int currentDuration, int total) {
        mDurationBar.setMax(total);
        //更新播放进度，更新进度条
        String totalDuration;
        String currentPosition;
        if (total > 1000 * 60 * 60) {
            totalDuration = mHourFormat.format(total);
            currentPosition = mHourFormat.format(currentDuration);
        } else {
            totalDuration = mMinFormat.format(total);
            currentPosition = mMinFormat.format(currentDuration);
        }
        if (mTotalDuration != null) {
            mTotalDuration.setText(totalDuration);
        }
        //更新当前时间
        if (mCurrentPosition != null) {
            mCurrentPosition.setText(currentPosition);
        }
        //更新进度
        //计算当前的进度
        if (!mIsUserTouchProgressBar) {
            mDurationBar.setProgress(currentDuration);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        if (track == null) {
            LogUtil.d(TAG, "onTrackUpdate --- > track null.");
            return;
        }
        mTrackTitleText = track.getTrackTitle();
        if (mTrackTitleTv != null) {
            //设置当前节目的标题
            mTrackTitleTv.setText(mTrackTitleText);
        }
        //当节目改变的时候，我们就获取到当前播放中播放位置
        //当前的节目改变以后，要修改页面的图片
        if (mTrackPageView != null) {
            mTrackPageView.setCurrentItem(playIndex);
        }
        //修改播放列表里的播放位置
        if (mMyPopWindow != null) {
            mMyPopWindow.setCurrentPlayPosition(playIndex);
        }
        String coverUrlLarge = track.getCoverUrlLarge();
        Observable.just(coverUrlLarge)
                .map(coverUrl -> Glide.with(this).asFile().load(coverUrl).submit().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        LogUtil.d(TAG, "=====> onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull File file) {
                        Music music = new Music();
                        music.setType(Music.Type.ONLINE);
                        music.setCoverPath(file.getPath());
                        mAlbumCoverView.setCoverBitmap(CoverLoader.get().loadRound(music));
                        mPlayingBgIv.setImageBitmap(CoverLoader.get().loadBlur(music));
                        LogUtil.d(TAG, "=====> onNext");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        LogUtil.d(TAG, "=====> onError");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.d(TAG, "=====> onComplete");
                    }
                });
    }

    @Override
    public void updateListOrder(boolean isReverse) {
        mMyPopWindow.updateOrderIcon(!isReverse);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //当页面选中的时候，就去切换播放器的内容
        if (mPlayerPresenter != null && mIsUserSlidePager) {
            mPlayerPresenter.playByIndex(position);
        }
        mIsUserSlidePager = false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
