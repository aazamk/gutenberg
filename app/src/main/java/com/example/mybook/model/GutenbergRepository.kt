package com.example.mybook.model

import com.example.mybook.service.OperationCallback


/**
 * @author Azam Khan
 */
class GutenbergRepository(private val gDataSource: GutenbergDataSource) {

    fun fetchBooks(callback: OperationCallback<Results>, topic: String, search: String, nextPageUrl: String?) {
        gDataSource.retrieveBooks(callback, topic, search, nextPageUrl)
    }


}