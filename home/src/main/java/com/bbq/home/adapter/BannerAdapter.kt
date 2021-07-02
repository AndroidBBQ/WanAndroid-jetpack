package com.bbq.home.adapter

import com.bbq.home.bean.BannerBean
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

/**
 * @author jhb
 * @date 2020/10/28
 */
class BannerAdapter(mData: MutableList<BannerBean>?) : BannerImageAdapter<BannerBean>(mData) {
    override fun onBindView(holder: BannerImageHolder, data: BannerBean?, position: Int, size: Int) {
        //图片加载自己实现
        Glide.with(holder.itemView)
            .load(data?.imagePath)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(holder.imageView);

    }
}