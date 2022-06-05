package com.shoshin.logincalc.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginData(
    val accessToken: String? = null
): Parcelable