package com.example.school2120app.data.remote.profile.dto

import com.google.gson.annotations.SerializedName

data class ProfileInfoDto(
    @SerializedName("id_original")
    val id: Int? = null,
    val surname: String? = null,
    val name: String? = null,
    val patronymic: String? = null,
    val email: String? = null,
    val place: String? = null,
    val functionality: String? = null,
    val docs: Array<Map<String, UserDocDto>>,

    @SerializedName("docs_history")
    val docsHistory: Array<Map<String, UserDocHistoryDto>>,
    val error: String? = null

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProfileInfoDto

        if (id != other.id) return false
        if (surname != other.surname) return false
        if (name != other.name) return false
        if (patronymic != other.patronymic) return false
        if (email != other.email) return false
        if (place != other.place) return false
        if (functionality != other.functionality) return false
        if (!docs.contentEquals(other.docs)) return false
        if (!docsHistory.contentEquals(other.docsHistory)) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (surname?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (patronymic?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (place?.hashCode() ?: 0)
        result = 31 * result + (functionality?.hashCode() ?: 0)
        result = 31 * result + docs.contentHashCode()
        result = 31 * result + docsHistory.contentHashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}