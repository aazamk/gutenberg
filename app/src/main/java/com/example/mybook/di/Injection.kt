package com.example.mybook.di

import androidx.lifecycle.ViewModelProvider
import com.example.mybook.model.GutenbergDataSource
import com.example.mybook.model.GutenbergRepository
import com.example.mybook.service.ApiClient
import com.example.mybook.service.GutenbergRemoteDataSource
import com.example.mybook.viewmodel.ViewModelFactory

/**
 * @author Azam Khan
 */
object Injection {

    private val gutenbergDataSource: GutenbergDataSource = GutenbergRemoteDataSource(ApiClient)
    private val gutenbergRepository = GutenbergRepository(gutenbergDataSource)
    private val bookViewModelFactory = ViewModelFactory(gutenbergRepository)

    fun providerRepository(): GutenbergDataSource {
        return gutenbergDataSource
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return bookViewModelFactory
    }
}