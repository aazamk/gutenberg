package com.example.mybook.service

import android.text.TextUtils
import android.util.Log
import com.example.mybook.model.GutenbergDataSource
import com.example.mybook.model.Results
import com.example.mybook.view.BookShelveActivity.Companion.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Azam Khan
 */
class GutenbergRemoteDataSource(apiClient: ApiClient) : GutenbergDataSource {

    private var call: Call<BookResponse>? = null
    private val service = apiClient.build()

    override fun retrieveBooks(callback: OperationCallback<Results>, topic:String, search:String, nextPageUrl:String?) {
        if (TextUtils.isEmpty(nextPageUrl)) {
            call = service?.books(topic, search)
        }else{
            call = service?.nextPageBook(nextPageUrl)
        }
        call?.enqueue(object : Callback<BookResponse> {
            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                response.body()?.let {
                    if (response.isSuccessful) {
                        callback.onSuccess(it.data, it.next)
                    } else {
                        callback.onError("Something went wrong...")
                    }
                }
            }
        })
    }
}