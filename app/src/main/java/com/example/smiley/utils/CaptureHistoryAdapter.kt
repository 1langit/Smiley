package com.example.smiley.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.databinding.ItemArticleBinding
import com.example.smiley.models.Classification

private typealias OnClickResult = (Classification) -> Unit

class CaptureHistoryAdapter(
    private val historyList: List<Classification>,
    private val onClickResult: OnClickResult
): RecyclerView.Adapter<CaptureHistoryAdapter.ItemHistoryViewHolder>() {

    inner class ItemHistoryViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(classification: Classification) {
            with(binding) {
                txtTitle.text = classification.classification
                txtPreview.text = classification.date
            }

            itemView.setOnClickListener {
                onClickResult(classification)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHistoryViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: ItemHistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }
}