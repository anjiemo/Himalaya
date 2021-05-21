package com.smart.himalaya.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

    protected final String TAG = getClass().getSimpleName();

    protected void fullWindow() {
        fullWindow(false);
    }

    protected void fullWindow(boolean isBlack) {
        Window window = getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        int uiVisibility = decorView.getSystemUiVisibility();
        // 设置状态栏中字体的颜色为黑色
        if (isBlack) {
            uiVisibility = uiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            // 设置状态栏中字体的颜色为白色
            uiVisibility = uiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        decorView.setSystemUiVisibility(uiVisibility);
    }
}

