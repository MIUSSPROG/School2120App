package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class SubscribeDocumentUsecase(private val mainRepository: MainRepository) {
    operator fun invoke(link: String): Flow<Resource<String>>{
        return mainRepository.subscribeDocument(url = link)
    }
}