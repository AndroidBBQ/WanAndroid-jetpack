package com.bbq.base.utils

import android.text.Html
import android.text.Spanned
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bbq.base.R
import com.bbq.base.base.BaseApp
import com.hjq.toast.ToastUtils
import kotlin.random.Random

fun String.showToast() {
    ToastUtils.show(this)
}

fun Int.getDrawable() = ActivityCompat.getDrawable(BaseApp.getInstance(), this)

fun Int.getResString() = BaseApp.getInstance().getString(this)


fun Int.getResDimen() = BaseApp.getInstance().resources.getDimension(this)

fun Int.getResDrawable() = ContextCompat.getDrawable(BaseApp.getInstance(), this)

fun Int.getResColor() = ContextCompat.getColor(BaseApp.getInstance(), this)


fun Any?.isNull() = this == null

fun Any?.isNotNull() = !isNull()


fun Boolean?.truely() = this != null && this

fun Boolean?.falsely() = !truely()


fun String.getHtmlStr(): Spanned {
    return Html.fromHtml(this)
}

private val mColors = arrayOf(
    R.color.colorBlue.getResColor(),
    R.color.colorBlueLight.getResColor(),
    R.color.colorBlueLighter.getResColor(),
    R.color.colorBlueDark.getResColor(),
    R.color.colorCyan.getResColor(),
    R.color.colorRed.getResColor(),
    R.color.colorRedDark.getResColor(),
    R.color.colorPink.getResColor(),
    R.color.colorPurple.getResColor(),
    R.color.colorPurpleDark.getResColor(),
    R.color.colorOrigin.getResColor(),
    R.color.colorOriginDark.getResColor(),
    R.color.colorYellowDark.getResColor(),
    R.color.colorYellowDarker.getResColor(),
    R.color.colorGreen.getResColor(),
    R.color.colorGreenLight.getResColor(),
    R.color.colorGreenDark.getResColor(),
    R.color.colorGreenDarker.getResColor(),
    R.color.colorBrown.getResColor(),
    R.color.colorBrownLight.getResColor()
)

fun randomInt(size: Int) = Random.nextInt(0, size)

fun getRandomColor() = mColors[randomInt(mColors.size)]