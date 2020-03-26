package com.lior.sam

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.lior.sam.database.NewsDatabase
import com.lior.sam.database.NewsLocalCache
import com.lior.sam.network.NewsService
import com.lior.sam.repository.NewsRepository
import com.lior.sam.ui.ViewModelFactory
import java.util.concurrent.Executors

object Injection {
    /**
     * Creates an instance of [NewsLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): NewsLocalCache {
        val database = NewsDatabase.getInstance(context)
        return NewsLocalCache(database.newsDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [NewsRepository] based on the [NewsService] and a
     * [NewsLocalCache]
     */
    private fun provideNewsRepository(context: Context): NewsRepository {
        return NewsRepository(NewsService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideNewsRepository(context))
    }
}