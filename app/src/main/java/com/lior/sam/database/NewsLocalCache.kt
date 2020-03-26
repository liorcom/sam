package com.lior.sam.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.lior.sam.model.NewsEntity
import java.util.concurrent.Executor

class NewsLocalCache(
    private val newsDao: NewsDao,
    private val ioExecutor: Executor
) {

    /**
     * Insert a list of news in the database, on a background thread.
     */
    fun insert(news: List<NewsEntity>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("NewsLocalCache", "inserting ${news.size} models")
            newsDao.insert(news)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<NewsModel>> from the Dao, based on a source type.
     * @param source data source
     */
    fun newsBySource(source: String): LiveData<List<NewsEntity>> {
        return newsDao.newsBySource(source)
    }
}