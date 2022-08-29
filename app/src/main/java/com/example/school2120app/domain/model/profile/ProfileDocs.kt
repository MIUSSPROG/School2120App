package com.example.school2120app.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileDocs(
    val id: Int,
    val surname: String,
    val name: String,
    val patronymic: String,
    val email: String,
    val place: String,
    val functionality: String,
    val subscribedDocs: ArrayList<UserDoc>,
    val unsubscribedDocs: ArrayList<UserDoc>
): Parcelable