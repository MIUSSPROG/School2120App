package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.profile.ProfileDocs
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class SignInUsecase(private val repository: MainRepository) {
    operator fun invoke(login: String, password: String): Flow<Resource<ProfileDocs>> {
        return repository.signIn(login, password)
    }
}