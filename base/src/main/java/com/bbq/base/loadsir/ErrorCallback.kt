package com.bbq.base.loadsir

import com.bbq.base.R
import com.kingja.loadsir.callback.Callback

class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.base_layout_error
    }
}