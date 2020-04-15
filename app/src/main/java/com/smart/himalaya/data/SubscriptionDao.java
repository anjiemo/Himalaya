package com.smart.himalaya.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smart.himalaya.base.BaseApplication;
import com.smart.himalaya.utils.Constants;
import com.smart.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.Announcer;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionDao implements ISubDao {

    private static final String TAG = "SubscriptionDao";
    private static final SubscriptionDao ourInstance = new SubscriptionDao();
    private final XimalayaDBHelper mXimalayaDBHelper;

    public static SubscriptionDao getInstance() {
        return ourInstance;
    }

    private SubscriptionDao() {
        mXimalayaDBHelper = new XimalayaDBHelper(BaseApplication.getAppContext());
    }

    @Override
    public void addAlbum(Album album) {
        SQLiteDatabase db = null;
        try {
            db = mXimalayaDBHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            //封装数据
            contentValues.put(Constants.SUB_COVER_URL, album.getCoverUrlLarge());
            contentValues.put(Constants.SUB_TITLE, album.getAlbumTitle());
            contentValues.put(Constants.SUB_DESCRIPTION, album.getAlbumIntro());
            contentValues.put(Constants.SUB_TRACKS_COUNT, album.getIncludeTrackCount());
            contentValues.put(Constants.SUB_AUTHOR_NAME, album.getPlayCount());
            contentValues.put(Constants.SUB_ALBUM_ID, album.getAnnouncer().getNickname());
            //插入数据
            db.insert(Constants.SUB_TB_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public void delAlbum(Album album) {
        SQLiteDatabase db = null;
        try {
            db = mXimalayaDBHelper.getWritableDatabase();
            db.beginTransaction();
            int delete = db.delete(Constants.SUB_TB_NAME, Constants.SUB_ALBUM_ID + "=?", new String[]{String.valueOf(album.getId())});
            LogUtil.d(TAG, "delAlbum  delete --- > " + delete);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public void listAlbum() {
        SQLiteDatabase db = null;
        List<Album> result = new ArrayList<>();
        try {
            db = mXimalayaDBHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor query = db.query(Constants.SUB_TB_NAME, null, null, null, null, null, null);
            //封装数据
            while (query.moveToNext()) {
                Album album = new Album();
                //封面图片
                String coverUrl = query.getString(query.getColumnIndex(Constants.SUB_COVER_URL));
                album.setCoverUrlLarge(coverUrl);
                //title
                String title = query.getString(query.getColumnIndex(Constants.SUB_TITLE));
                album.setAlbumTitle(title);
                //描述
                String description = query.getString(query.getColumnIndex(Constants.SUB_DESCRIPTION));
                album.setAlbumIntro(description);
                //节目数量
                int tracksCount = query.getInt(query.getColumnIndex(Constants.SUB_TRACKS_COUNT));
                album.setIncludeTrackCount(tracksCount);
                //播放量
                int playCount = query.getInt(query.getColumnIndex(Constants.SUB_PLAY_COUNT));
                album.setPlayCount(playCount);
                //专辑id
                int albumId = query.getInt(query.getColumnIndex(Constants.SUB_ALBUM_ID));
                album.setId(albumId);
                //作者名称
                String authorName = query.getString(query.getColumnIndex(Constants.SUB_AUTHOR_NAME));
                Announcer announcer = new Announcer();
                announcer.setNickname(authorName);
                album.setAnnouncer(announcer);
                result.add(album);
            }
            //把数据通知出去
            query.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }
}
