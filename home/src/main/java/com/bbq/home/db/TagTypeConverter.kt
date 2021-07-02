package com.bbq.home.db

import androidx.room.TypeConverter
import com.bbq.home.bean.ArticleTag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @date：2021/5/20
 * @author fuusy
 * @instruction： List<Tag>的类型转换
 */
class TagTypeConverter {
    @TypeConverter
    fun stringToObject(value: String): List<ArticleTag> {
        val listType = object : TypeToken<List<ArticleTag>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<ArticleTag>): String {
        return Gson().toJson(list)
    }
}