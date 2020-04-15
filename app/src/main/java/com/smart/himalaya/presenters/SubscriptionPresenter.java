package com.smart.himalaya.presenters;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smart.himalaya.base.BaseApplication;
import com.smart.himalaya.beans.MyAlbum;
import com.smart.himalaya.db.DaoSession;
import com.smart.himalaya.db.MyAlbumDao;
import com.smart.himalaya.interfaces.ISubDaoCallback;
import com.smart.himalaya.interfaces.ISubscriptionCallback;
import com.smart.himalaya.interfaces.ISubscriptionPresenter;
import com.smart.himalaya.utils.Constants;
import com.smart.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPresenter implements ISubscriptionPresenter, ISubDaoCallback {

    private static final String TAG = "SubscriptionPresenter";
    private final DaoSession mDaoSession = BaseApplication.getDaoSession();
    private List<ISubscriptionCallback> mCallbacks = new ArrayList<>();
    private List<Album> mSubscriptions = new ArrayList<>();

    private SubscriptionPresenter() {

    }

    @SuppressLint("NewApi")
    private void listSubscriptions() {
        mSubscriptions.clear();
        List<MyAlbum> myAlbums = mDaoSession.loadAll(MyAlbum.class);
        myAlbums.sort((o1, o2) -> (int) (o2.getSubscriptionTime() - o1.getSubscriptionTime()));
        for (MyAlbum myAlbum : myAlbums) {
            Album album = new Album();
            album.setId(myAlbum.getId());
            album.setAlbumTitle(myAlbum.getAlbumTitle());
            album.setAlbumIntro(myAlbum.getAlbumIntro());
            album.setCoverUrlLarge(myAlbum.getCoverUrlLarge());
            album.setPlayCount(myAlbum.getPlayCount());
            album.setIncludeTrackCount(myAlbum.getIncludeTrackCount());
            Announcer announcer = new Announcer();
            announcer.setNickname(myAlbum.getNickName());
            album.setAnnouncer(announcer);
            mSubscriptions.add(album);
        }
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
        //判断当前的订阅数量，不能超过100
        if (mSubscriptions.size() >= Constants.MAX_SUB_COUNT) {
            //给出提示
            for (ISubscriptionCallback callback : mCallbacks) {
                callback.onSubFull();
            }
            return;
        }
        MyAlbum myAlbum = new MyAlbum();
        myAlbum.setId(album.getId());
        myAlbum.setAlbumTitle(album.getAlbumTitle());
        myAlbum.setAlbumIntro(album.getAlbumIntro());
        myAlbum.setCoverUrlLarge(album.getCoverUrlLarge());
        myAlbum.setPlayCount(album.getPlayCount());
        myAlbum.setIncludeTrackCount(album.getIncludeTrackCount());
        myAlbum.setNickName(album.getAnnouncer().getNickname());
        myAlbum.setSubscriptionTime(System.currentTimeMillis());
        mDaoSession.insertOrReplace(myAlbum);
    }

    @Override
    public void deleteSubscription(Album album) {
        mDaoSession.queryBuilder(MyAlbum.class).where(MyAlbumDao.Properties.Id.eq(album.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    @Override
    public void loadSubscriptionList() {
        listSubscriptions();
    }

    public List<Album> getSubscriptions() {
        listSubscriptions();
        return mSubscriptions;
    }

    @Override
    public boolean isSub(Album album) {
        List<MyAlbum> myAlbums = mDaoSession.queryBuilder(MyAlbum.class).where(MyAlbumDao.Properties.Id.eq(album.getId())).list();
        return myAlbums.size() != 0;
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
            LogUtil.d(TAG, "update ui for add result.");
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
    public void onSubListLoaded(List<Album> result) {
        //通知UI更新
        BaseApplication.getHandler().post(() -> {
            for (ISubscriptionCallback callback : mCallbacks) {
                callback.onSubscriptionsLoaded(result);
            }
        });
    }
}
