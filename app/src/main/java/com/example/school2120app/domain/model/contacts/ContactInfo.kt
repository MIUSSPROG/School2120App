package com.example.school2120app.domain.model.contacts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactInfo(
    val position: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    val buildingType: String,
    val phone: String,
    val email: String,
    val photoUrl: String
): Parcelable
