package com.example.smiley.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.databinding.ItemArticleBinding
import com.example.smiley.models.Article

private typealias OnClickArticle = (Article) -> Unit

class ArticleAdapter(
    private val articleList: List<Article>,
    private val onClickArticle: OnClickArticle
):RecyclerView.Adapter<ArticleAdapter.ItemArticleViewHolder>() {

    inner class ItemArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            with(binding) {
                txtTitle.text = article.title
                txtPreview.text = article.content
            }

            itemView.setOnClickListener {
                onClickArticle(article)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ItemArticleViewHolder, position: Int) {
        holder.bind(articleList[position])
    }
}