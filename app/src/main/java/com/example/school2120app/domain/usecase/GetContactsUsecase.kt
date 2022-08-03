package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetContactsUsecase(private val repository: MainRepository) {
    operator fun invoke(fetchFromRemote: Boolean): Flow<Resource<List<ContactInfo>>>{
        return repository.getContacts(fetchFromRemote)
    }
}