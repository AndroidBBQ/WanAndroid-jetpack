package com.bbq.webview.route

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.bbq.base.route.WebService
import com.bbq.webview.ui.WebActivity

@Route(path = "/web/web")
class WebServiceImpl : WebService {
    override fun goWeb(context: Context, title: String, url: String, id: Int?, isCollect: Boolean) {
        WebActivity.launch(context, title, url,id, isCollect)
    }

    override fun init(context: Context?) {
    }
}