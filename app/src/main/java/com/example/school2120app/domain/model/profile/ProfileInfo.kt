package com.example.school2120app.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileInfo(
    val id: Int,
    val surname: String,
    val name: String,
    val patronymic: String,
    val email: String,
    val place: String,
    val functionality: String,
    val docs: List<Map<String, UserDoc>>,
    val docsHistory: List<Map<String, UserDocHistory>>,
    val error: String
): Parcelable