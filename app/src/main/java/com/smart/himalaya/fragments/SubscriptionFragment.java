package com.smart.himalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.smart.himalaya.DetailActivity;
import com.smart.himalaya.R;
import com.smart.himalaya.adapters.AlbumListAdapter;
import com.smart.himalaya.base.BaseApplication;
import com.smart.himalaya.base.BaseFragment;
import com.smart.himalaya.interfaces.ISubscriptionCallback;
import com.smart.himalaya.presenters.AlbumDetailPresenter;
import com.smart.himalaya.presenters.SubscriptionPresenter;
import com.smart.himalaya.views.ConfirmDialog;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class SubscriptionFragment extends BaseFragment implements ISubscriptionCallback, AlbumListAdapter.onAlbumItemClickListener, AlbumListAdapter.onAlbumItemLongClickListener, ConfirmDialog.OnDialogActionClickListener {

    private static final String TAG = "SubscriptionFragment";
    private SubscriptionPresenter mSubscriptionPresenter;
    private RecyclerView mSubListView;
    private AlbumListAdapter mAlbumListAdapter;
    private TwinklingRefreshLayout mRefreshLayout;
    private Album mCurrentClickAlbum = null;

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
        mAlbumListAdapter.setAlbumItemClickListener(this);
        mAlbumListAdapter.setOnAlbumItemLongClickListener(this);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAlbumListAdapter.setAlbumItemClickListener(null);
    }

    @Override
    public void onItemClick(int position, Album album) {
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        //Item被点击了，跳转到详情界面
        startActivity(new Intent(getContext(), DetailActivity.class));
    }

    @Override
    public void onItemLongClick(Album album) {
        mCurrentClickAlbum = album;
        //订阅的item被长按了
        ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
        confirmDialog.setOnDialogActionClickListener(this);
        confirmDialog.show();
    }

    @Override
    public void onCancelSubClick() {
        //取消订阅内容
        if (mCurrentClickAlbum != null && mSubscriptionPresenter != null) {
            mSubscriptionPresenter.deleteSubscription(mCurrentClickAlbum);
            onRefresh();
            //给出取消订阅的提示
            Toast.makeText(BaseApplication.getAppContext(), R.string.cancel_sub_success, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGiveUpClick() {
        //放弃取消订阅
    }
}
