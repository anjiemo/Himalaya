package com.smart.himalaya.interfaces;

public interface IAlbumDetailPresenter {

    /**
     * 下拉刷新更多内容
     */
    void pull2RefreshMore();

    /**
     * 上拉加载更多
     */
    void loadMore();

    /**
     * 获取专辑详情
     *
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId, int page);

    /**
     * 注册UI通知的接口
     *
     * @param detailViewCallback
     */
    void registerViewCallback(IAlbumDetailViewCallback detailViewCallback);

    /**
     * 取消UI通知的接口
     *
     * @param detailViewCallback
     */
    void unRegisterViewCallback(IAlbumDetailViewCallback detailViewCallback);
}
