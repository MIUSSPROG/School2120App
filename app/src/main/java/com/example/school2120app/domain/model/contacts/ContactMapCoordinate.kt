package com.example.school2120app.domain.model.contacts

data class ContactMapCoordinate(
    val lat: Double,
    val long: Double,
    val address: String,
    val type: BuildingType
)

enum class BuildingType{
    SCHOOL,
    KINDERGARTEN
}
