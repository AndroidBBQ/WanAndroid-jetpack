package com.bbq.base.view

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import com.bbq.base.R
import com.bbq.base.utils.getDrawable
import com.bbq.base.utils.getResColor
import com.bbq.base.utils.getResDimen

class FabViewModel(
    var size: Int = R.dimen.dp_50.getResDimen().toInt(),
    var drawable: Drawable? = R.drawable.up_arrow_white.getDrawable(),
    var onClick: (() -> Unit)? = null,
    var background: Int = R.color.colorAccent.getResColor()
) {
    val mDrawable = ObservableField(drawable)
}