package com.lior.sam.model

import androidx.lifecycle.LiveData

/**
 * contains LiveData<List<NewsModel>> holding news data,
 * and a LiveData<String> of network error state.
 */
data class GetNewsResult(
    val data: LiveData<List<NewsEntity>>,
    val networkErrors: LiveData<String>
)