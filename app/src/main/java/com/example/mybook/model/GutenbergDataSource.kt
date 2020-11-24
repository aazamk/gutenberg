package com.example.mybook.model

import com.example.mybook.service.OperationCallback

/**
 * @author Azam Khan
 */
interface GutenbergDataSource {
    fun retrieveBooks(
        callback: OperationCallback<Results>,
        topic: String,
        search: String,
        nextPageUrl: String?
    )


}