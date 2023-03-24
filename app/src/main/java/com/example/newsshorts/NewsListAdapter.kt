package com.example.newsshorts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsshorts.databinding.ItemNewsBinding

class NewsListAdapter(private val listener: NewsItemClicked): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {
    inner class NewsViewHolder(val item: ItemNewsBinding): RecyclerView.ViewHolder(item.root)
    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem=items[position]
        holder.item.title.text = currentItem.title
        holder.item.author.text=currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.item.image)

        holder.item.root.setOnClickListener {
            listener.onItemClicked(currentItem)
        }

    }
    fun updateNews(updatedNews: ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }
}


interface NewsItemClicked{
    fun onItemClicked(item: News)
}