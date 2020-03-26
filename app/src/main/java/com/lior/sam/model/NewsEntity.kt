package com.lior.sam.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    val source: String?,
    val title: String?,
    val url: String?,
    val imageUrl: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)