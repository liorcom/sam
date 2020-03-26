package com.lior.sam.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lior.sam.database.NewsLocalCache
import com.lior.sam.model.GetNewsResult
import com.lior.sam.model.NewsEntity
import com.lior.sam.model.NewsResponse
import com.lior.sam.network.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository(
    private val service: NewsService,
    private val cache: NewsLocalCache
) {
    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    fun getNews(source: String): GetNewsResult {
        Log.d("NewsRepository", "source: $source")
        requestAndSaveData(source)

        // Get data from the local cache
        val data = cache.newsBySource(source)

        return GetNewsResult(data, networkErrors)
    }

    /**
     * Get news based on a source.
     * @param source news source type
     * The result of the request is handled by the implementation of the functions passed as params
     * @param onSuccess function that defines how to handle the list of NewsModel received
     * @param onError function that defines how to handle request failure
     */
    private fun getNews(
        service: NewsService,
        source: String,
        onSuccess: (repos: List<NewsEntity>) -> Unit,
        onError: (error: String) -> Unit
    ) {
        Log.d(TAG, "source: $source")

        service.getNews(source).enqueue(
            object : Callback<NewsResponse> {
                override fun onFailure(call: Call<NewsResponse>?, t: Throwable) {
                    Log.e(TAG, "failed to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                    call: Call<NewsResponse>?,
                    response: Response<NewsResponse>
                ) {
                    Log.d(TAG, "response: $response")
                    if (response.isSuccessful) {
                        val news = response.body()?.articles?.map {
                            NewsEntity(
                                source,
                                it?.title,
                                it?.url,
                                it?.urlToImage
                            )
                        } ?: emptyList()
                        onSuccess(news)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
        )
    }

    private fun requestAndSaveData(source: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        getNews(service, source, { news ->
            cache.insert(news) {
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val TAG = "NewsRepository"
    }
}