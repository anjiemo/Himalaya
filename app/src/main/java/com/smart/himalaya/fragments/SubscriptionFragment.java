package com.smart.himalaya.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.smart.himalaya.R;
import com.smart.himalaya.adapters.AlbumListAdapter;
import com.smart.himalaya.base.BaseApplication;
import com.smart.himalaya.base.BaseFragment;
import com.smart.himalaya.db.DaoSession;
import com.smart.himalaya.interfaces.ISubscriptionCallback;
import com.smart.himalaya.presenters.SubscriptionPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class SubscriptionFragment extends BaseFragment implements ISubscriptionCallback {

    private static final String TAG = "SubscriptionFragment";
    private SubscriptionPresenter mSubscriptionPresenter;
    private RecyclerView mSubListView;
    private AlbumListAdapter mAlbumListAdapter;
    private TwinklingRefreshLayout mRefreshLayout;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.fragment_subscription, container, false);
        mRefreshLayout = rootView.findViewById(R.id.over_scroll_view);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableRefresh(false);
        mSubListView = rootView.findViewById(R.id.subscription_list);
        mSubListView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mSubListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
        mAlbumListAdapter = new AlbumListAdapter();
        mSubListView.setAdapter(mAlbumListAdapter);
        mSubscriptionPresenter = SubscriptionPresenter.getInstance();
        mSubscriptionPresenter.loadSubscriptionList();
        mSubscriptionPresenter.registerViewCallback(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isShowing = true;
        onRefresh();
    }

    @Override
    protected void onRefresh() {
        //更新UI
        if (mAlbumListAdapter != null) {
            mAlbumListAdapter.setData(mSubscriptionPresenter.getSubscriptions());
        }
    }

    @Override
    public void onAddResult(boolean isSuccess) {

    }

    @Override
    public void onDeleteResult(boolean isSuccess) {

    }

    @Override
    public void onSubscriptionsLoaded(List<Album> albums) {

    }

    @Override
    public void onSubFull() {

    }
}
