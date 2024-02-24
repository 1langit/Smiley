package com.example.smiley.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smiley.databinding.ActivityChatBinding
import com.example.smiley.models.Chat
import com.example.smiley.models.ChatRoom
import com.example.smiley.models.Dentist
import com.example.smiley.models.Patient
import com.example.smiley.utils.ChatAdapter
import com.example.smiley.utils.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firestore: FirebaseFirestore
    private val chatLiveData: MutableLiveData<List<Chat>> by lazy {
        MutableLiveData<List<Chat>>()
    }
    private lateinit var chatroomId: String
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        prefManager = PrefManager.getInstance(this@ChatActivity)
        chatroomId = intent.getStringExtra("chatroomId")!!
        val otherUserId = intent.getStringExtra("otherUserId")!!

        getOtherUser()
        getUserChat()
        showChat()

        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }

            btnSend.setOnClickListener {
                val message = edtMessage.text.toString()
                if (message.isNotBlank()) {
                    val chat = Chat(
                        senderUid = uid,
                        recieverUid = otherUserId,
                        message = message,
                        time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                    )

//                    firestore.collection("chat")
//                        .add(chat)
//                        .addOnFailureListener {
//                            Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
//                        }.addOnSuccessListener { document ->
//                            chat.id = document.id
//                            document.set(chat).addOnFailureListener {
//                                Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
//                            }.addOnSuccessListener {
//                                edtMessage.setText("")
//                            }
//                        }

                    firestore.collection("chatroom")
                        .document(chatroomId)
                        .collection("chat")
                        .add(chat)
                        .addOnFailureListener {
                            Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener { document ->
                            document.update("id", document.id).addOnFailureListener {
                                Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener {
                                edtMessage.setText("")
                            }
                        }
                }
            }
        }
    }

    private fun showChat() {
        chatLiveData.observe(this@ChatActivity) { chatList ->
//            val filteredUserChat = chatList.filter {
//                (it.senderUid == uid || it.senderUid == if (prefManager.getRole() == "patient") dentist.uid else patient.uid)
//                        && (it.recieverUid == uid || it.recieverUid == if (prefManager.getRole() == "patient") dentist.uid else patient.uid)
//            }.sortedBy {
//                it.time
//            }

            binding.rvChat.apply {
                adapter = ChatAdapter(chatList)
                layoutManager = LinearLayoutManager(
                    applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    private fun getUserChat() {
//        firestore.collection("chat")
//            .whereIn("senderUid", listOf(uid, dentist.uid))
//            .whereIn("receiverUid", listOf(uid, dentist.uid))
//            .orderBy("time")
//            .addSnapshotListener { snapshots, error ->
//                if (error != null) {
//                    Log.d("Chat", "error listening to changes")
//                }
//                if (snapshots != null) {
//                    val chatList = snapshots.toObjects(Chat::class.java)
//                    chatLiveData.postValue(chatList)
//                }
//        }
        firestore.collection("chatroom")
            .document(chatroomId)
            .collection("chat")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.d("Chat", "error listening to changes")
                }
                if (snapshots != null) {
                    val chatList = snapshots.toObjects(Chat::class.java)
                    chatLiveData.postValue(chatList)
                }
            }
    }

    private fun getOtherUser() {
        firestore.collection("chatroom")
            .document(chatroomId)
            .get()
            .addOnFailureListener {
                Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                if (it.exists()) {
                    val chatroom = it.toObject(ChatRoom::class.java)!!
                    if (prefManager.getRole() == "patient") {
                        binding.txtTitle.text = chatroom.dentistName
                    } else {
                        binding.txtTitle.text = chatroom.patientName
                    }
                } else {
                    Toast.makeText(this@ChatActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
//        if (prefManager.getRole() == "patient") {
//            firestore.collection("dentist")
//                .document(intent.getStringExtra("id")!!)
//                .get()
//                .addOnFailureListener {
//                    Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
//                }.addOnSuccessListener {
//                    if (it.exists()) {
//                        if (prefManager.getRole() == "patient") {
//                            dentist = it.toObject(Dentist::class.java)!!
//                            binding.txtTitle.text = dentist.name
//                        } else {
//                            patient = it.toObject(Patient::class.java)!!
//                            binding.txtTitle.text = patient.name
//                        }
//                    } else {
//                        Toast.makeText(this@ChatActivity, "User not found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
    }
}