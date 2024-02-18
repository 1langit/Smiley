package com.example.smiley.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.R
import com.example.smiley.databinding.ActivityChatBinding
import com.example.smiley.models.Chat
import com.example.smiley.utils.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val chatAdapter: ChatAdapter by lazy {
        ChatAdapter()
    }

//    private val listDemo = listOf(
//        Chat("RECEIVE", "How are you today?", "15.46", true),
//        Chat("SEND", "How are you today?", "15.48",true),
//        Chat("RECEIVE", "I am unusually good", "15.51",true),
//        Chat("SEND", "Very good! ", "15.56",true),
//        Chat("RECEIVE", "What time does it start?", "16.01",true),
//        Chat("RECEIVE", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "16.03",true),
//        Chat("SEND", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ", "16.16",false),
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }

            rvChat.apply {
                adapter = chatAdapter
                layoutManager = LinearLayoutManager(
                    applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
//            chatAdapter.setData(listDemo)
//            rvChat.smoothScrollToPosition(listDemo.size - 1)
        }
    }
}