package com.smart.himalaya.presenters;

import com.smart.himalaya.base.BaseApplication;
import com.smart.himalaya.data.ISubDaoCallback;
import com.smart.himalaya.data.SubscriptionDao;
import com.smart.himalaya.interfaces.ISubscriptionCallback;
import com.smart.himalaya.interfaces.ISubscriptionPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SubscriptionPresenter implements ISubscriptionPresenter, ISubDaoCallback {

    private final SubscriptionDao mSubscriptionDao;
    private Map<Long, Album> mData = new HashMap<>();
    private List<ISubscriptionCallback> mCallbacks = new ArrayList<>();

    private SubscriptionPresenter() {
        mSubscriptionDao = SubscriptionDao.getInstance();
        mSubscriptionDao.setCallback(this);
        mSubscriptionDao.listAlbum();
        listSubscriptions();
    }

    private void listSubscriptions() {
        Observable.create(emitter -> {
            //只调用，不处理结果
            if (mSubscriptionDao != null) {
                mSubscriptionDao.listAlbum();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private static SubscriptionPresenter sSubscriptionPresenter = null;

    public static SubscriptionPresenter getInstance() {
        if (sSubscriptionPresenter == null) {
            synchronized (SubscriptionPresenter.class) {
                if (sSubscriptionPresenter == null) {
                    sSubscriptionPresenter = new SubscriptionPresenter();
                }
            }
        }
        return sSubscriptionPresenter;
    }

    @Override
    public void addSubscription(Album album) {
        Observable.create(emitter -> {
            if (mSubscriptionDao != null) {
                mSubscriptionDao.addAlbum(album);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteSubscription(Album album) {
        Observable.create(emitter -> {
            if (mSubscriptionDao != null) {
                mSubscriptionDao.delAlbum(album);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void getSubscriptionList() {

    }

    @Override
    public boolean isSub(Album album) {
        Album result = mData.get(album.getId());
        return result == null;
    }

    @Override
    public void registerViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        if (!mCallbacks.contains(iSubscriptionCallback)) {
            mCallbacks.add(iSubscriptionCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        mCallbacks.remove(iSubscriptionCallback);
    }

    @Override
    public void onAddResult(boolean isSuccess) {
        //添加结果的回调
        BaseApplication.getHandler().post(() -> {
            for (ISubscriptionCallback callback : mCallbacks) {
                callback.onAddResult(isSuccess);
            }
        });
    }

    @Override
    public void onDelResult(boolean isSuccess) {
        //删除订阅的回调
        BaseApplication.getHandler().post(() -> {
            for (ISubscriptionCallback callback : mCallbacks) {
                callback.onDeleteResult(isSuccess);
            }
        });
    }

    @Override
    public void onSubListLoaded(final List<Album> result) {
        //加载数据的回调
        for (Album album : result) {
            mData.put(album.getId(), album);
        }
        //通知UI更新
        BaseApplication.getHandler().post(() -> {
            for (ISubscriptionCallback callback : mCallbacks) {
                callback.onSubscriptionsLoaded(result);
            }
        });
    }
}
