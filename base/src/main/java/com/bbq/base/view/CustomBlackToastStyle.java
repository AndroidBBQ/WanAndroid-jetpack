package com.bbq.base.view;

import android.content.Context;
import android.view.Gravity;

import com.hjq.toast.style.BlackToastStyle;
import com.win.lib_base.utils.DensityUtil;

/**
 * 自定义的黑色的toast样式
 */
public class CustomBlackToastStyle extends BlackToastStyle {
    @Override
    public int getYOffset() {
        return DensityUtil.dip2px(100);
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getHorizontalPadding(Context context) {
        return DensityUtil.dip2px(17);
    }

    @Override
    protected int getVerticalPadding(Context context) {
        return DensityUtil.dip2px(6);
    }
}
