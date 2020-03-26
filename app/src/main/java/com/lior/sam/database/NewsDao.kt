package com.lior.sam.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lior.sam.model.NewsEntity

/**
 * Room data access object.
 */
@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: List<NewsEntity>)

    // get news selected by source type
    @Query("SELECT * FROM news WHERE (source= :source)")
    fun newsBySource(source: String): LiveData<List<NewsEntity>>
}