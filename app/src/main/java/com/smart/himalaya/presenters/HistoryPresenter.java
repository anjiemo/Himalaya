package com.smart.himalaya.presenters;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.smart.himalaya.base.BaseApplication;
import com.smart.himalaya.beans.MyTrack;
import com.smart.himalaya.db.DaoSession;
import com.smart.himalaya.db.MyTrackDao;
import com.smart.himalaya.utils.Constants;
import com.smart.himalaya.utils.MyEntityClassCombiner;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class HistoryPresenter {

    private static final String TAG = "HistoryPresenter";
    private DaoSession mDaoSession = BaseApplication.getDaoSession();
    private List<Track> mHistories = new ArrayList<>();
    private final Object mLock = new Object();

    private HistoryPresenter() {
        loadHistories();
    }

    private static HistoryPresenter mHistoryPresenter = null;

    public static HistoryPresenter getHistoryPresenter() {
        if (mHistoryPresenter == null) {
            synchronized (HistoryPresenter.class) {
                if (mHistoryPresenter == null) {
                    mHistoryPresenter = new HistoryPresenter();
                }
            }
        }
        return mHistoryPresenter;
    }

    public List<Track> getHistories() {
        return mHistories;
    }

    @SuppressLint("NewApi")
    public void loadHistories() {
        synchronized (mLock) {
            //从数据表中查出所有的历史记录
            mHistories.clear();
            List<MyTrack> myTracks = mDaoSession.loadAll(MyTrack.class);
            for (MyTrack myTrack : myTracks) {
                mHistories.add(myTrack.getTrack());
            }
        }
    }

    /**
     * 添加历史记录，如果重复则替换
     *
     * @param track
     */
    public void addHistory(Track track) {
        synchronized (mLock) {
            if (mHistories.size() >= Constants.MAX_HISTORY_COUNT) {
                //删除最早添加的记录
                mHistories.remove(mHistories.size() - 1);
            }
            mHistories.add(0, track);
            mDaoSession.deleteAll(MyTrack.class);
            for (Track history : mHistories) {
                MyTrack myTrack = new MyTrack();
                myTrack.setDataId(history.getDataId());
                myTrack.setTrack(history);
                mDaoSession.insertOrReplace(myTrack);
            }
            loadHistories();
        }
    }

    /**
     * 删除历史记录
     *
     * @param track
     */
    public void delHistory(Track track) {
        synchronized (mLock) {
            mDaoSession.queryBuilder(MyTrack.class).where(MyTrackDao.Properties.DataId.eq(track.getDataId())).buildDelete().executeDeleteWithoutDetachingEntities();
            loadHistories();
        }
    }

    /**
     * 清空历史记录
     */
    public void clearHistory() {
        synchronized (mLock) {
            mDaoSession.queryBuilder(MyTrack.class).buildDelete().executeDeleteWithoutDetachingEntities();
            loadHistories();
        }
    }
}
