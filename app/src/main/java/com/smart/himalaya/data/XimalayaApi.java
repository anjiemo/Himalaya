package com.smart.himalaya.data;

import com.smart.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.HashMap;
import java.util.Map;

public class XimalayaApi {

    private XimalayaApi() {

    }

    private static XimalayaApi sXimalayaApi;

    public static XimalayaApi getInstance() {
        if (sXimalayaApi == null) {
            synchronized (XimalayaApi.class) {
                if (sXimalayaApi == null) {
                    sXimalayaApi = new XimalayaApi();
                }
            }
        }
        return sXimalayaApi;
    }

    /**
     * 获取推荐内容
     *
     * @param callBack 请求结果的回调接口
     */
    public void getRecommendList(IDataCallBack<GussLikeAlbumList> callBack) {
        Map<String, String> map = new HashMap<>();
        //这个参数表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_RECOMMEND + "");
        CommonRequest.getGuessLikeAlbum(map, callBack);
    }

    /**
     * 根据专辑的id获取到专辑内容。
     *
     * @param callBack  获取专辑详情的回调接口
     * @param albumId   专辑的id
     * @param pageIndex 第几页
     */
    public void getAlbumDetail(IDataCallBack<TrackList> callBack, long albumId, int pageIndex) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.ALBUM_ID, String.valueOf(albumId));
        map.put(DTransferConstants.PAGE, String.valueOf(pageIndex));
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Constants.COUNT_DEFAULT));
        CommonRequest.getTracks(map, callBack);
    }

    /**
     * 根据关键字，进行搜索
     *
     * @param keyword
     */
    public void searchByKeyword(String keyword, int page, IDataCallBack<SearchAlbumList> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        map.put(DTransferConstants.PAGE, String.valueOf(page));
        map.put(DTransferConstants.PAGE_SIZE, String.valueOf(Constants.COUNT_DEFAULT));
        CommonRequest.getSearchedAlbums(map, callBack);
    }

    /**
     * 获取推荐的热词
     *
     * @param callBack
     */
    public void getHotWords(IDataCallBack<HotWordList> callBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DTransferConstants.TOP, String.valueOf(Constants.COUNT_HOT_WORD));
        CommonRequest.getHotWords(map, callBack);
    }

    /**
     * 根据关键字获取联想词
     *
     * @param keyword 关键字
     * @param callback 回调
     */
    public void getSuggestWord(String keyword, IDataCallBack<SuggestWords> callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, keyword);
        CommonRequest.getSuggestWord(map, callback);
    }
}
