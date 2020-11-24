package com.example.mybook.service

/**
 * @author Azam Khan
 */
interface OperationCallback<T> {
    fun onSuccess(data: List<T>?, next: String?)
    fun onError(error: String?)
}