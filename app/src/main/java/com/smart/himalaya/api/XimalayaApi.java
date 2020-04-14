package com.smart.himalaya.api;

import com.smart.himalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

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
}
