package com.example.school2120app.data.remote.profile.dto

data class StatInfoDto(
    val docs: Array<Map<String, StatInfoHistoryDto>>
    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StatInfoDto

        if (!docs.contentEquals(other.docs)) return false

        return true
    }

    override fun hashCode(): Int {
        return docs.contentHashCode()
    }
}
