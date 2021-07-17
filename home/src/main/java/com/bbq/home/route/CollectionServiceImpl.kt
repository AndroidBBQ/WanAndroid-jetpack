package com.bbq.home.route

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.bbq.base.route.CollectionService
import com.bbq.home.ui.CollectionActivity

@Route(path = "/home/collect")
class CollectionServiceImpl : CollectionService {
    override fun launch(context: Context) {
        CollectionActivity.launch(context)
    }

    override fun init(context: Context?) {
    }
}