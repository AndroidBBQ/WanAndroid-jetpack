package com.bbq.login.ui

import android.os.Bundle
import com.bbq.base.base.BaseVMActivity
import com.bbq.login.R
import com.bbq.base.bean.User
import com.bbq.base.utils.StatusBarUtil
import com.bbq.login.databinding.ActivityLoginBinding
import com.bbq.login.model.LoginLayoutBean
import com.bbq.login.utils.UserManager
import com.bbq.login.viewmodel.LoginViewModel
import com.bbq.net.model.BaseResult
import com.bbq.net.model.DataStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseVMActivity<ActivityLoginBinding>() {
    val viewModel: LoginViewModel by viewModel()
    private lateinit var mData: LoginLayoutBean

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.setDarkMode(this)
        mData = LoginLayoutBean()
        mBinding.bean = mData
    }

    override fun initData() {
        super.initData()
        mBinding.close.setOnClickListener {
            finish()
        }

        mBinding.featureName.setOnClickListener {
            mData.isLogin = !mData.isLogin
            initEditText()
        }

        mBinding.btnLogin.setOnClickListener {
            if (mData.isLogin) { //登录
                if (getUserName().isNullOrEmpty()) {
                    toast("账号不能为空")
                    return@setOnClickListener
                }
                if (getPassword().isNullOrEmpty()) {
                    toast("密码不能为空")
                    return@setOnClickListener
                }
                loginAction()
            } else { //注册
                if (getUserName().isNullOrEmpty()) {
                    toast("账号不能为空")
                    return@setOnClickListener
                }
                if (getPassword().isNullOrEmpty()) {
                    toast("密码不能为空")
                    return@setOnClickListener
                }
                if (getSurePassword().isNullOrEmpty()) {
                    toast("确认密码不能为空")
                    return@setOnClickListener
                }
                registerAction()
            }
        }
    }

    private fun loginAction() {
        viewModel.login(getUserName(), getPassword())
    }

    private fun registerAction() {
        viewModel.register(getUserName(), getPassword(), getSurePassword())
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mLoginUser.observe(this, {
            handleResult(it)
        })
        viewModel.mRegisterUser.observe(this, {
            handleResult(it)
        })
    }

    private fun handleResult(it: BaseResult<User>) {
        when (it.dataStatus) {
            DataStatus.STATE_LOADING ->
                showLoading()
            DataStatus.STATE_ERROR -> {
                dismissLoading()
                toast(it.exception!!.msg)
            }
            DataStatus.STATE_SUCCESS -> {
                dismissLoading()
                saveUser(it.data!!)
            }
        }
    }

    private fun saveUser(data: User) {
        UserManager.saveUser(data)
        finish()
    }

    private fun getUserName(): String {
        return mBinding.userName.text.toString().trim()
    }

    private fun getPassword(): String {
        return mBinding.password.text.toString().trim()
    }

    private fun getSurePassword(): String {
        return mBinding.surePassword.text.toString().trim()
    }

    private fun initEditText() {
        mBinding.userName.text = null
        mBinding.password.text = null
        mBinding.surePassword.text = null
    }
}