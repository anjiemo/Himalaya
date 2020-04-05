package com.smart.himalaya;

import android.os.Bundle;

import com.smart.himalaya.base.BaseActivity;
import com.smart.himalaya.presenters.PlayerPresenter;

public class PlayerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.play();
    }
}
