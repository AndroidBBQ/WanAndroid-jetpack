package com.bbq.base.utils

import android.app.Activity
import java.lang.IllegalArgumentException

object ActivityUtil {

    var mActivities = arrayListOf<Activity>()

    fun addActivity(activity: Activity) {
        if (!mActivities.contains(activity)) {
            mActivities.add(activity)
        }
    }

    fun removeActivity(activity: Activity) {
        if (mActivities.contains(activity)) {
            mActivities.remove(activity)
        }
    }

    fun getStackTopAct(): Activity {
        if (mActivities.isEmpty()) {
            throw  IllegalArgumentException("can't get Activity ")
        }
        return mActivities[mActivities.size - 1]
    }

    fun finishAllActivity() {
        mActivities.forEach {
            if (!it.isFinishing) {
                it.finish()
            }
        }
    }


}