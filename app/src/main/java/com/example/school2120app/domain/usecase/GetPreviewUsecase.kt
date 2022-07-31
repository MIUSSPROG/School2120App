package com.example.school2120app.domain.usecase

import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetPreviewUsecase(private val repository: MainRepository) {
    operator fun invoke(previewUrl: String): Flow<Resource<ImageSource>>{
        return repository.getPreview(previewUrl)
    }
}