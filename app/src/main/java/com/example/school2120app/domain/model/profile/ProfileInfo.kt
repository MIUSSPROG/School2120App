package com.example.school2120app.domain.model.profile

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
){
    fun toProfileDocs(): ProfileDocs{
        return ProfileDocs(
            id = id,
            surname = surname,
            name = name,
            patronymic = patronymic,
            email = email,
            place = place,
            functionality = functionality,
            ArrayList<UserDoc>(),
            ArrayList<UserDoc>()
        )
    }
}