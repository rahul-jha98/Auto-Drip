package com.jrlabls.auto_drip.models

import android.os.Parcel
import android.os.Parcelable

class BedEntry (
    var Id : Int = 0,
    var Level: Long = 0L,
    var Switch: String? = "",
    var Switch1: Int = 0,
    var bottle: Int = 0,
    var bottlecount: Int = 0,
    var doctorname: String? = "",
    var medicine: String? = "",
    var patient: Int = 0,
    var patientname: String? = "",
    var rate: Int = 0,
    var timeleft: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Id)
        parcel.writeLong(Level)
        parcel.writeString(Switch)
        parcel.writeInt(Switch1)
        parcel.writeInt(bottle)
        parcel.writeInt(bottlecount)
        parcel.writeString(doctorname)
        parcel.writeString(medicine)
        parcel.writeInt(patient)
        parcel.writeString(patientname)
        parcel.writeInt(rate)
        parcel.writeInt(timeleft)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BedEntry> {
        override fun createFromParcel(parcel: Parcel): BedEntry {
            return BedEntry(parcel)
        }

        override fun newArray(size: Int): Array<BedEntry?> {
            return arrayOfNulls(size)
        }
    }

}