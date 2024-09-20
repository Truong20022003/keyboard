package com.sticker.nicekeyboard.ui.fragment_theme.theme

import android.os.Parcel
import android.os.Parcelable

data class ThemeModel(
    var id: Int,
    var pathTheme: String,
    var pathBackground: String,
    var themeName: String,
    var isSelected: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(pathTheme)
        parcel.writeString(pathBackground)
        parcel.writeString(themeName)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ThemeModel> {
        override fun createFromParcel(parcel: Parcel): ThemeModel {
            return ThemeModel(parcel)
        }

        override fun newArray(size: Int): Array<ThemeModel?> {
            return arrayOfNulls(size)
        }
    }


}