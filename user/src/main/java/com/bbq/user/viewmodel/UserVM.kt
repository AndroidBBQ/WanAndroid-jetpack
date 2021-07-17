package com.bbq.user.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.bbq.base.base.BaseViewModel
import com.bbq.base.rository.StateLiveData
import com.bbq.user.bean.IntegralBean
import com.bbq.user.repo.UserRepo
import kotlinx.coroutines.launch

class UserVM(application: Application, val repo: UserRepo) : BaseViewModel(application) {

    val mInterData = StateLiveData<IntegralBean>()
    fun getInterData() {
        viewModelScope.launch {
            repo.getInterData(mInterData)
        }
    }
}