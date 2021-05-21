package com.smart.himalaya.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onSubViewLoaded(inflater, container);
    }

    protected abstract View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container);

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    protected void onRefresh() {

    }
}
