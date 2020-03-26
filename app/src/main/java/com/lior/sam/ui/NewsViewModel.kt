package com.lior.sam.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.lior.sam.model.GetNewsResult
import com.lior.sam.model.NewsEntity
import com.lior.sam.repository.NewsRepository

class NewsViewModel(private val repository: NewsRepository): ViewModel() {
    private val sourceLiveData = MutableLiveData<String>()
    private val newsResult: LiveData<GetNewsResult> = Transformations.map(sourceLiveData) {
        repository.getNews(it)
    }

    val news: LiveData<List<NewsEntity>> = Transformations.switchMap(newsResult) {
        it.data
    }

    val networkErrors: LiveData<String> = Transformations.switchMap(newsResult) {
        it.networkErrors
    }

    /**
     * get news based on a source String.
     */
    fun getNews(source: String) {
        sourceLiveData.value = source
    }
}