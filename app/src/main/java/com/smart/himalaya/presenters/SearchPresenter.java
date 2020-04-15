package com.smart.himalaya.presenters;

import com.smart.himalaya.data.XimalayaApi;
import com.smart.himalaya.interfaces.ISearchCallback;
import com.smart.himalaya.interfaces.ISearchPresenter;
import com.smart.himalaya.utils.Constants;
import com.smart.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements ISearchPresenter {

    private static final String TAG = "SearchPresenter";

    private List<Album> mSearchResult = new ArrayList<>();

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
        mCurrentPage = DEFAULT_PAGE;
        mSearchResult.clear();
        //用于重新搜索
        //当网络不好的时候，用户会点击重新搜索
        mCurrentKeyword = keyword;
        search(keyword);
    }

    private void search(String keyword) {
        mXimalayaApi.searchByKeyword(keyword, mCurrentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                List<Album> albums = searchAlbumList.getAlbums();
                mSearchResult.addAll(albums);
                if (albums != null) {
                    LogUtil.d(TAG, "albums size --- > " + albums.size());
                    if (mIsLoaderMore) {
                        for (ISearchCallback iSearchCallback : mCallbacks) {
                            iSearchCallback.onLoadMoreResult(mSearchResult, albums.size() != 0);
                        }
                        mIsLoaderMore = false;
                    } else {
                        for (ISearchCallback iSearchCallback : mCallbacks) {
                            iSearchCallback.onSearchResultLoaded(mSearchResult);
                        }
                    }
                } else {
                    LogUtil.d(TAG, "albums is null..");
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "errorCode --- > " + errorCode);
                LogUtil.d(TAG, "errorMsg --- > " + errorMsg);
                for (ISearchCallback iSearchCallback : mCallbacks) {
                    if (mIsLoaderMore) {
                        iSearchCallback.onLoadMoreResult(mSearchResult, false);
                        mCurrentPage--;
                        mIsLoaderMore = false;
                    } else {
                        iSearchCallback.onError(errorCode, errorMsg);
                    }
                }
            }
        });
    }

    @Override
    public void reSearch() {
        search(mCurrentKeyword);
    }

    private boolean mIsLoaderMore = false;

    @Override
    public void loadMore() {
        //判断有没有必要进行加载更多
        if (mSearchResult.size() < Constants.COUNT_DEFAULT) {
            for (ISearchCallback iSearchCallback : mCallbacks) {
                iSearchCallback.onLoadMoreResult(mSearchResult, false);
            }
        } else {
            mIsLoaderMore = true;
            mCurrentPage++;
            search(mCurrentKeyword);
        }
    }

    @Override
    public void getHotWord() {
        //做一个热词缓存
        mXimalayaApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(HotWordList hotWordList) {
                if (hotWordList != null) {
                    List<HotWord> hotWords = hotWordList.getHotWordList();
                    LogUtil.d(TAG, "hotWords size --- > " + hotWords.size());
                    for (ISearchCallback iSearchCallback : mCallbacks) {
                        iSearchCallback.onHotWordLoaded(hotWords);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getHotWord errorCode --- > " + errorCode);
                LogUtil.d(TAG, "getHotWord errorMsg --- > " + errorMsg);
            }
        });
    }

    @Override
    public void getRecommendWord(String keyword) {
        mXimalayaApi.getSuggestWord(keyword, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(SuggestWords suggestWords) {
                if (suggestWords != null) {
                    List<QueryResult> keyWordList = suggestWords.getKeyWordList();
                    LogUtil.d(TAG, "keyWordList size --- > " + keyWordList.size());
                    for (ISearchCallback iSearchCallback : mCallbacks) {
                        iSearchCallback.onRecommendWordLoaded(keyWordList);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG, "getRecommendWord errorCode --- > " + errorCode);
                LogUtil.d(TAG, "getRecommendWord errorMsg --- > " + errorMsg);
            }
        });
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
