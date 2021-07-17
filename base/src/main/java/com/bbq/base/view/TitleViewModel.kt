package com.bbq.base.view

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import com.bbq.base.R
import com.bbq.base.utils.getDrawable
import com.bbq.base.utils.getResColor

/**
 * Created by jhb on 2020-01-15.
 */
class TitleViewModel(
    var leftText: String? = "",
    var leftDrawable: Drawable? = R.drawable.abc_vector_test.getDrawable(),
    var leftAction: (() -> Unit)? = null,
    var title: String = "",
    var rightText: String = "",
    var rightDrawable: Drawable? = null,
    var rightAction: (() -> Unit)? = null,
    var background: Int = R.color.theme.getResColor()
) {
    val mTitle = ObservableField(title)
    val mRightDrawable = ObservableField(rightDrawable)
}