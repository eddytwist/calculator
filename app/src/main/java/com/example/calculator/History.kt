package com.example.calculator

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class History (val result: String?) : Parcelable