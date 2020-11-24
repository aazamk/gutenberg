package com.example.mybook.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybook.model.GutenbergRepository
import com.example.mybook.model.Results
import com.example.mybook.service.OperationCallback
import com.example.mybook.view.BookShelveActivity.Companion.TAG

/**
 * @author Azam Khan
 */
class BookShelvesViewModel(private val repository: GutenbergRepository) : ViewModel() {

    private val _books = MutableLiveData<List<Results>>().apply { value = emptyList() }
    val books: LiveData<List<Results>> = _books
    private var _nextPageUrl: String? = "";


    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList
    lateinit var topic: String


    fun loadBookData(topic: String, search: String) {
        _isViewLoading.value = true
        repository.fetchBooks(object : OperationCallback<Results> {
            override fun onError(error: String?) {
                _isViewLoading.value = false
                _onMessageError.value = error
            }

            override fun onSuccess(data: List<Results>?, nextUrl: String?) {
                _nextPageUrl = nextUrl;
                _isViewLoading.value = false
                Log.d("ViewModel", "onSuccess() called with: nextUrl = $nextUrl")
                if (data.isNullOrEmpty()) {
                    _isEmptyList.value = true

                } else {
                    _books.value = data
                }
            }
        }, topic, search, _nextPageUrl)
    }

    fun onLoadMore() {
        Log.d(TAG, "onLoadMore: ")
        loadBookData("", "")
    }

    fun resetNextPageUrl() {
        _nextPageUrl = ""
    }

}