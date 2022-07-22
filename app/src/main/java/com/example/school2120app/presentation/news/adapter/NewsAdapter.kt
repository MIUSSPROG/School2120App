package com.example.school2120app.presentation.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.school2120app.core.util.ActionListener
import com.example.school2120app.databinding.NewsRvItemBinding
import com.example.school2120app.domain.model.news.News

class NewsAdapter(
    private val actionListener: ActionListener<News>
): ListAdapter<News, NewsAdapter.NewsViewHolder>(DiffCallback()), View.OnClickListener{

    class NewsViewHolder(private val binding: NewsRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(news: News){
            binding.apply {
                root.tag = news
                tvNewsItemHeader.text = HtmlCompat.fromHtml(news.name, HtmlCompat.FROM_HTML_MODE_COMPACT)
                tvNewsItemAnons.text = HtmlCompat.fromHtml(news.anons, HtmlCompat.FROM_HTML_MODE_COMPACT)
                val publishDate = news.publishDate.split("T").first()
                tvNewsItemPublishDate.text = publishDate
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.bind(curItem)
    }

    override fun onClick(v: View) {
        val news = v.tag as News
        actionListener.itemClick(news)
    }
}