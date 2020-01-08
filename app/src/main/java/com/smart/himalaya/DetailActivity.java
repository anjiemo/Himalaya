package com.smart.himalaya;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smart.himalaya.base.BaseActivity;
import com.smart.himalaya.interfaces.IAlbumDetailViewCallback;
import com.smart.himalaya.presenters.AlbumDetailPresenter;
import com.smart.himalaya.views.RoundRectImageView;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
    }

    private void initView() {
        mLargeCover = (ImageView) findViewById(R.id.iv_large_cover);
        mSmallCover = (RoundRectImageView) findViewById(R.id.iv_small_cover);
        mAlbumTitle = (TextView) findViewById(R.id.tv_album_title);
        mAlbumAuthor = (TextView) findViewById(R.id.tv_album_author);
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumLoaded(Album album) {
        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (mLargeCover != null) {
            Glide.with(this).load(album.getCoverUrlLarge()).into(mLargeCover);
        }
        if (mSmallCover != null) {
            Glide.with(this).load(album.getCoverUrlLarge()).into(mSmallCover);
        }
    }
}
