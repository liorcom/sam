package com.lior.sam.network

import com.lior.sam.model.NewsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * News API setup via Retrofit.
 */
interface NewsService {
    /**
     * Get news.
     */
    @GET("everything")
    fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<NewsResponse>

    companion object {
        private const val BASE_URL = "http://newsapi.org/v2/"
        private const val API_KEY = "f644bdb7fb554867931653461e298edb"

        fun create(): NewsService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(NewsService::class.java)
        }
    }
}