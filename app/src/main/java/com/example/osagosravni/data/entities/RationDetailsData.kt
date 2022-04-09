package com.example.osagosravni.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RationDetailsData(
    val factors: ArrayList<Factor>
) : Parcelable

@Parcelize
data class Factor(
    val detailText: String,
    val headerValue: String,
    val name: String,
    val title: String,
    val value: String
) : Parcelable