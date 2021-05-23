package com.smart.himalaya.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View

fun View.setRoundRectBg(color: Int = Color.WHITE, cornerRadius: Int = ScreenUtils.dp2px(15f)) {
    background = GradientDrawable().apply {
        setColor(color)
        setCornerRadius(cornerRadius.toFloat())
    }
}