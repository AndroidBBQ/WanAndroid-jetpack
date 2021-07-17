package com.bbq.user.ui

import android.view.View
import android.webkit.CookieManager
import com.bbq.base.base.BaseVMFragment
import com.bbq.base.route.CollectionServiceUtils
import com.bbq.base.route.LoginServiceUtils
import com.bbq.base.utils.SpUtils
import com.bbq.base.utils.isNull
import com.bbq.net.model.DataStatus
import com.bbq.user.R
import com.bbq.user.databinding.FragmentUserBinding
import com.bbq.user.viewmodel.UserItem
import com.bbq.user.viewmodel.UserVM
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : BaseVMFragment<FragmentUserBinding>() {

    private val viewModel: UserVM by viewModel()
    override fun getLayoutId(): Int {
        return R.layout.fragment_user
    }

    override fun initView(view: View) {
        val item = UserItem()
        mBinding.item = item

        if (LoginServiceUtils.isLogin()) {
            getInterValue()
        } else {
            mBinding.cirUserImg.setBackgroundResource(R.drawable.ic_user_un_authen)
        }

        mBinding.tvBtnExitLogin.setOnClickListener {
            CookieManager.getInstance().removeAllCookie()
            LoginServiceUtils.removeUserInfo()
            SpUtils.clearAll()
            item.refresh()
            mBinding.tvUserIntegral.visibility = View.GONE
        }

        LoginServiceUtils.getLiveData().observe(this, {
            item.refresh()
            getInterValue()

        })

        mBinding.cirUserImg.setOnClickListener {
            if (!LoginServiceUtils.isLogin()) {
                LoginServiceUtils.start(requireContext())
                return@setOnClickListener
            }
        }
        mBinding.tvUserName.setOnClickListener {
            if (!LoginServiceUtils.isLogin()) {
                LoginServiceUtils.start(requireContext())
                return@setOnClickListener
            }
        }

        mBinding.viewFavoriteBg.setOnClickListener {
            if (!LoginServiceUtils.isLogin()) {
                LoginServiceUtils.start(requireContext())
                return@setOnClickListener
            }
            CollectionServiceUtils.launch(requireContext())
        }
    }

    private fun getInterValue() {
        val option = RequestOptions.errorOf(R.drawable.ic_user_un_authen)
        Glide.with(this)
            .load(LoginServiceUtils.getUserInfo()?.icon)
            .apply(option)
            .into(mBinding.cirUserImg)
        val coin = SpUtils.getInt("key_integral")
        if (coin == 0) {
            viewModel.getInterData()
        } else {
            mBinding.tvUserIntegral.text = "积分:${coin}"
        }
    }

    override fun startObserver() {
        super.startObserver()
        viewModel.mInterData.observe(this, {
            when (it.dataStatus) {
                DataStatus.STATE_LOADING -> {
                    showLoading()
                }
                DataStatus.STATE_SUCCESS -> {
                    dismissLoading()
                    SpUtils.put("key_integral", it.data?.coinCount)
                    mBinding.tvUserIntegral.text = "积分:${it.data?.coinCount}"
                }
                DataStatus.STATE_ERROR -> {
                    dismissLoading()
                }
            }
        })
    }
}