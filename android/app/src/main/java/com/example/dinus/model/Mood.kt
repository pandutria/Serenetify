package com.example.dinus.model

import android.os.Parcel
import android.os.Parcelable

data class Mood(
    val anger: Double,
    val fear: Double,
    val happy: Double,
    val love: Double,
    val sadness: Double,
    val surprise: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(anger)
        parcel.writeDouble(fear)
        parcel.writeDouble(happy)
        parcel.writeDouble(love)
        parcel.writeDouble(sadness)
        parcel.writeDouble(surprise)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mood> {
        override fun createFromParcel(parcel: Parcel): Mood {
            return Mood(parcel)
        }

        override fun newArray(size: Int): Array<Mood?> {
            return arrayOfNulls(size)
        }
    }
}