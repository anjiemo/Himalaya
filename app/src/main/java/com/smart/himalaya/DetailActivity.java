package com.smart.himalaya;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smart.himalaya.adapters.DetailListAdapter;
import com.smart.himalaya.base.BaseActivity;
import com.smart.himalaya.interfaces.IAlbumDetailViewCallback;
import com.smart.himalaya.presenters.AlbumDetailPresenter;
import com.smart.himalaya.utils.ImageBlur;
import com.smart.himalaya.utils.LogUtil;
import com.smart.himalaya.views.RoundRectImageView;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private static final String TAG = "DetailActivity";
    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentPage = 1;
    private RecyclerView mAlbum_detail_list;
    private DetailListAdapter mDetailLsitAdapter;

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
        mAlbum_detail_list = findViewById(R.id.album_detail_list);
        //RecyclerView的使用步骤
        //第一步：设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAlbum_detail_list.setLayoutManager(layoutManager);
        //第二步：设置适配器
        mDetailLsitAdapter = new DetailListAdapter();
        mAlbum_detail_list.setAdapter(mDetailLsitAdapter);
        //设置item的上下间距
        mAlbum_detail_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //更新/设置UI数据
        mDetailLsitAdapter.setData(tracks);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAlbumLoaded(Album album) {

        long id = album.getId();
        LogUtil.d(TAG, "album -- > " + id);
        //获取专辑的详情内容
        mAlbumDetailPresenter.getAlbumDetail((int) id, mCurrentPage);

        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }

        //做毛玻璃效果
        if (mLargeCover != null) {
            final Handler handler = new Handler();
            final ImageView imageView = Glide.with(this).load(album.getCoverUrlLarge()).into(mLargeCover).getView();
            //到这里才是说明有图片的
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // TODO: 2020/1/8 防止图片未加载成功，直接使用高斯模糊工具类时程序会崩溃
                        Thread.sleep(600);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (imageView.getDrawable() != null) {
                                    ImageBlur.makeBlur(imageView, DetailActivity.this);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        if (mSmallCover != null) {
            Glide.with(this).load(album.getCoverUrlLarge()).into(mSmallCover);
        }
    }
}
