package com.example.school2120app.domain.usecase

import com.example.school2120app.core.util.Resource
import com.example.school2120app.domain.model.menu.remote.MenuItem
import com.example.school2120app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class GetMenusUsecase(private val repository: MainRepository) {
    operator fun invoke(): Flow<Resource<List<MenuItem>>> {
        return repository.getMenus()
    }
}