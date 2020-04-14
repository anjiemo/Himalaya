package com.smart.himalaya.presenters;

import com.smart.himalaya.api.XimalayaApi;
import com.smart.himalaya.interfaces.ISearchCallback;
import com.smart.himalaya.interfaces.ISearchPresenter;
import com.smart.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {

    private static final String TAG = "SearchPresenter";
    //当前的搜索关键字
    private String mCurrentKeyword = null;
    private final XimalayaApi mXimalayaApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;

    private SearchPresenter() {
        mXimalayaApi = XimalayaApi.getInstance();
    }

    private static SearchPresenter sSearchPresenter = null;

    public static SearchPresenter getSearchPresenter() {
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class) {
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }
        return sSearchPresenter;
    }

    private List<ISearchCallback> mCallbacks = new ArrayList<>();

    @Override
    public void doSearch(String keyword) {
        //用于重新搜索
        //当网络不好的时候，用户会点击重新搜索
        mCurrentKeyword = keyword;
        mXimalayaApi.searchByKeyword(keyword, mCurrentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                List<Album> albums = searchAlbumList.getAlbums();
                if (albums != null) {
                    LogUtil.d(TAG, "albums size --- > " + albums.size());
                } else {
                    LogUtil.d(TAG, "albums is null..");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "errorCode --- > " + errorCode);
                LogUtil.d(TAG, "errorMsg --- > " + errorMsg);
            }
        });
    }

    @Override
    public void reSearch() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getHotWord() {

    }

    @Override
    public void getRecommend(String keyword) {

    }

    @Override
    public void registerViewCallback(ISearchCallback iSearchCallback) {
        if (!mCallbacks.contains(iSearchCallback)) {
            mCallbacks.add(iSearchCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(ISearchCallback iSearchCallback) {
        mCallbacks.remove(iSearchCallback);
    }
}
