package com.lior.sam.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lior.sam.Injection
import com.lior.sam.R
import com.lior.sam.model.NewsEntity
import com.lior.sam.model.Sources
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    private lateinit var viewModel: NewsViewModel
    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(this)).get(NewsViewModel::class.java)
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv_news.addItemDecoration(decoration)
        rv_news.layoutManager = LinearLayoutManager(this)
        initAdapter()
        btn_source1.setOnClickListener {
            viewModel.getNews(Sources.APPLE.type)
        }
        btn_source2.setOnClickListener {
            viewModel.getNews(Sources.TECHCRUNCH.type)
        }
    }

    private fun initAdapter() {
        rv_news.adapter = adapter
        viewModel.news.observe(this, Observer<List<NewsEntity>> {
            Log.d("Activity", "list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            tv_empty_list.visibility = View.VISIBLE
            rv_news.visibility = View.GONE
        } else {
            tv_empty_list.visibility = View.GONE
            rv_news.visibility = View.VISIBLE
        }
    }
}
