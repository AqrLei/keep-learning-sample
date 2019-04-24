package com.open.aqrlei.ipc

import android.os.Parcel
import android.os.Parcelable

/**
 * 必须与相应的AIDL在同个包下
 */
data class Info(var data: String,
                var times: Int) : Parcelable {
    companion object CREATOR : Parcelable.Creator<Info> {
        /**
         * 从序列化的对象中创建原始对象
         * */
        override fun createFromParcel(source: Parcel): Info = Info(source)

        /**
         * 创建指定长度的原始对象数组
         * */
        override fun newArray(size: Int): Array<Info?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt())

    /**
     * 将当前对象写入序列化对象中
     * @param flags  1表示当前对象需要最为返回值返回，不能立即释放，几乎所有情况都为0
     * [android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE]
     * */
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(data)
            writeInt(times)
        }
    }

    /**
     * @retrun 如果有文件描述符返回1, 否则返回0(几乎所有情况都是0)
     * [android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR]
     * */
    override fun describeContents() = 0
}