package com.example.smiley.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smiley.databinding.ItemChatLeftBinding
import com.example.smiley.databinding.ItemChatRightBinding
import com.example.smiley.models.Chat

class ChatAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val listOfChat = mutableListOf<Chat>()
    companion object {
        const val RIGHT_VIEW = 1
        const val LEFT_VIEW = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            LEFT_VIEW -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemChatLeftBinding.inflate(inflater, parent, false)
                LeftViewHolder(binding)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemChatRightBinding.inflate(inflater, parent, false)
                RightViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = listOfChat.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listOfChat[position].type == "SEND")
            (holder as RightViewHolder).bind(listOfChat[position])
        else
            (holder as LeftViewHolder).bind(listOfChat[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (listOfChat[position].type == "SEND") RIGHT_VIEW else LEFT_VIEW
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Chat>) {
        listOfChat.clear()
        listOfChat.addAll(list)
        notifyDataSetChanged()
    }

    inner class LeftViewHolder(private val binding: ItemChatLeftBinding) : ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.apply {
                chat.also {
                    txtMessage.text = chat.message
                    txtTime.text = chat.time
                }
            }
        }
    }

    inner class RightViewHolder(private val binding: ItemChatRightBinding) : ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.apply {
                chat.also {
                    txtMessage.text = chat.message
                    txtTime.text = chat.time
                }
            }
        }
    }
}