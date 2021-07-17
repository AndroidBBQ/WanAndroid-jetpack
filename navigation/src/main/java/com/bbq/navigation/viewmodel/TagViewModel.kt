package com.bbq.navigation.viewmodel

import androidx.databinding.ObservableField
import com.bbq.base.utils.getRandomColor
import com.bbq.base.utils.getResColor
import com.bbq.base.utils.getResDimen
import com.bbq.base.utils.getResDrawable
import com.bbq.navigation.R

/**
 * @author jhb
 * @date 2020/10/23
 */
class TagViewModel {
    var mBgColor = ObservableField(getRandomColor())
    var mContent = ObservableField("")
    var mTextColor = ObservableField(R.color.colorRed.getResColor())
    var mTextSize = ObservableField(R.dimen.sp_12.getResDimen())
    var mDrawable = ObservableField(R.drawable.nav_rect_red_shape.getResDrawable())
}