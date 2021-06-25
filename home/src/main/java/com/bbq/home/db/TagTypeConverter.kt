package com.bbq.home.db

import androidx.room.TypeConverter
import com.bbq.home.bean.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * @date：2021/5/20
 * @author fuusy
 * @instruction： List<Tag>的类型转换
 */
class TagTypeConverter {
    @TypeConverter
    fun stringToObject(value: String): List<Tag> {
        val listType = object : TypeToken<List<Tag>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Tag>): String {
        return Gson().toJson(list)
    }
}