package com.smart.himalaya.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart.himalaya.R;
import com.smart.himalaya.base.BaseFragment;

public class HistoryFragment extends BaseFragment {

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.fragment_history, container,false);
        return rootView;
    }

    @Override
    protected void onRefresh() {

    }
}
