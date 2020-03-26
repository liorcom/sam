package com.lior.sam.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lior.sam.R
import com.lior.sam.model.NewsEntity

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.tv_title)
    private val image: ImageView = view.findViewById(R.id.iv_news_cover)
    private var newsEntity: NewsEntity? = null

    init {
        view.setOnClickListener {
            newsEntity?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(newsEntity: NewsEntity?) {
        if (newsEntity == null) {
            val resources = itemView.resources
            title.text = resources.getString(R.string.loading)
            image.visibility = View.GONE
        } else {
            image.visibility = View.VISIBLE
            showRepoData(newsEntity)
        }
    }

    private fun showRepoData(newsEntity: NewsEntity) {
        this.newsEntity = newsEntity
        title.text = newsEntity.title
        Glide.with(itemView.context)
            .load(newsEntity.imageUrl)
            .into(image)
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item, parent, false)
            return NewsViewHolder(view)
        }
    }
}