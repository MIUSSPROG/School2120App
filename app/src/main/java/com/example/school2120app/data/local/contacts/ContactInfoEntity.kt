package com.example.school2120app.data.local.contacts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val position: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    val buildingType: String,
    val phone: String,
    val email: String,
    val photoUrl: String
)