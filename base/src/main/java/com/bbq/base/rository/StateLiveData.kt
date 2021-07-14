package com.bbq.base.rository

import androidx.lifecycle.MutableLiveData
import com.bbq.net.model.BaseResult

class StateLiveData<T> : MutableLiveData<BaseResult<T>>() {
}