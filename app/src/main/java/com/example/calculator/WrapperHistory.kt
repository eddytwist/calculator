package com.example.calculator

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WrapperHistory (val histories: MutableList<History> = mutableListOf<History>()) : Parcelable