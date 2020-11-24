package com.example.mybook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybook.model.GutenbergRepository

/**
 * @author Azam Khan
 */
class ViewModelFactory(private val repository: GutenbergRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookShelvesViewModel(repository) as T
    }
}