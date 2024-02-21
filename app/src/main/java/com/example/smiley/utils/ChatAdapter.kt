package com.example.smiley.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smiley.databinding.ItemChatLeftBinding
import com.example.smiley.databinding.ItemChatRightBinding
import com.example.smiley.models.Chat
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(
    private val listOfChat: List<Chat>
) : RecyclerView.Adapter<ViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
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
        if (listOfChat[position].senderUid == currentUserId)
            (holder as RightViewHolder).bind(listOfChat[position])
        else
            (holder as LeftViewHolder).bind(listOfChat[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (listOfChat[position].senderUid == currentUserId) RIGHT_VIEW else LEFT_VIEW
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