package com.bbq.navigation.bean

import android.os.Parcel
import android.os.Parcelable

class TreeBean(
    val children: List<TreeBean>? = null,
    var childrenSelectPosition: Int = 0,
    val courseId: String? = "",
    val id: Int,
    val name: String? = "",
    val order: String? = "",
    val parentChapterId: String? = "",
    val userControlSetTop: String? = "",
    val visible: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(CREATOR),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(children)
        parcel.writeInt(childrenSelectPosition)
        parcel.writeString(courseId)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(order)
        parcel.writeString(parentChapterId)
        parcel.writeString(userControlSetTop)
        parcel.writeString(visible)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TreeBean> {
        override fun createFromParcel(parcel: Parcel): TreeBean {
            return TreeBean(parcel)
        }

        override fun newArray(size: Int): Array<TreeBean?> {
            return arrayOfNulls(size)
        }
    }
}