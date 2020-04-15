package com.smart.himalaya.presenters;

import android.util.Log;

import com.smart.himalaya.data.XimalayaApi;
import com.smart.himalaya.interfaces.IRecommendPresenter;
import com.smart.himalaya.interfaces.IRecommendViewCallback;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.List;

public class RecommendPresenter implements IRecommendPresenter {

    private static final String TAG = "RecommendPresenter";
    private List<IRecommendViewCallback> mCallbacks = new ArrayList<>();
    private List<Album> mCurrentRecommend = null;

    private RecommendPresenter() {

    }

    private static RecommendPresenter sInstance = null;

    /**
     * 获取单例对象
     *
     * @return
     */
    public static RecommendPresenter getInstance() {
        if (sInstance == null) {
            synchronized (RecommendPresenter.class) {
                if (sInstance == null) {
                    sInstance = new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取当前的推荐专辑列表
     *
     * @return 推荐专辑列表，使用之前要判空
     */
    public List<Album> getCurrentRecommend() {
        return mCurrentRecommend;
    }

    /**
     * 获取推荐内容，其实就是猜你喜欢
     * 这个接口：3.10.6 获取猜你喜欢专辑
     */
    @Override
    public void getRecommendList() {
        //获取推荐内容
        //封装参数
        upDateLoading();
        XimalayaApi ximalayaApi = XimalayaApi.getInstance();
        ximalayaApi.getRecommendList(new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                //数据获取成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    //数据回来以后，我们要求更新UI
                    //upRecommendUI(albumList);
                    handlerRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //获取数据出错
                Log.d(TAG, "onError: ========error code====>" + i);
                Log.d(TAG, "onError: ========error msg====>" + s);
                handlerError();
            }
        });
    }

    private void handlerError() {
        if (mCallbacks != null) {
            for (IRecommendViewCallback callback : mCallbacks) {
                callback.onNetworkError();
            }
        }
    }

    private void handlerRecommendResult(List<Album> albumList) {
        //通知UI更新
        if (albumList != null) {
            if (albumList.size() == 0) {
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onEmpty();
                }
            } else {
                for (IRecommendViewCallback callback : mCallbacks) {
                    callback.onRecommendListLoad(albumList);
                }
                mCurrentRecommend = albumList;
            }
        }
    }

    private void upDateLoading() {
        for (IRecommendViewCallback callback : mCallbacks) {
            callback.onLoading();
        }
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }
}
