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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var prefManager: PrefManager
    private lateinit var firestore: FirebaseFirestore
    private val chatLiveData: MutableLiveData<List<Chat>> by lazy {
        MutableLiveData<List<Chat>>()
    }
    private lateinit var dentist: Dentist
    private lateinit var patient: Patient
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dentist = Dentist()
        patient = Patient()
        firestore = FirebaseFirestore.getInstance()
        prefManager = PrefManager.getInstance(this@ChatActivity)

        firestore.collection(if (prefManager.getRole() == "patient") "dentist" else "patient" )
            .document(intent.getStringExtra("id")!!)
            .get()
            .addOnFailureListener {
                Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                if (it.exists()) {
                    if (prefManager.getRole() == "patient") {
                        dentist = it.toObject(Dentist::class.java)!!
                        binding.txtTitle.text = dentist.name
                    } else {
                        patient = it.toObject(Patient::class.java)!!
                        binding.txtTitle.text = patient.name
                    }
                } else {
                    Toast.makeText(this@ChatActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
        getUserChat()
        showChat()

        with(binding) {
            txtTitle.text = dentist.name
            btnBack.setOnClickListener {
                finish()
            }

            btnSend.setOnClickListener {
                val message = edtMessage.text.toString()
                if (message.isNotBlank()) {
                    val chat = Chat(
                        senderUid = uid,
                        recieverUid = if (prefManager.getRole() == "patient") dentist.uid else patient.uid,
                        message = message,
                        time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                    )

                    firestore.collection("chat")
                        .add(chat)
                        .addOnFailureListener {
                            Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener { document ->
                            chat.id = document.id
                            document.set(chat).addOnFailureListener {
                                Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener {
                                edtMessage.setText("")
                            }
                        }

//                    firestore.collection("chatroom")
//                        .whereArrayContains("participants", uid)
//                        .whereArrayContains("participants", dentist.uid)
//                        .get()
//                        .addOnSuccessListener { snapshot ->
//                            Toast.makeText(this@ChatActivity, "${snapshot.isEmpty()}", Toast.LENGTH_SHORT).show()
//                            if (snapshot.isEmpty()) {
//                                val chatRoom = ChatRoom(
//                                    participants = listOf(uid, dentist.uid),
//                                    lastMessage = message,
//                                    time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
//                                )
//                                firestore.collection("chatroom")
//                                    .add(chatRoom)
//                                    .addOnSuccessListener { chatroomDocument ->
//                                        chatRoom.id = chatroomDocument.id
//                                        chatroomDocument.collection("chat")
//                                            .add(chat)
//                                            .addOnFailureListener {
//                                                Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
//                                            }
//                                            .addOnSuccessListener {
//                                                edtMessage.setText("")
//                                            }
//                                    }
//                            } else {
//                                val document = snapshot.documents.first()
//                                document.reference.collection("chat")
//                                    .add(chat)
//                                    .addOnFailureListener {
//                                        Toast.makeText(this@ChatActivity, it.message, Toast.LENGTH_SHORT).show()
//                                    }
//                                    .addOnSuccessListener {
//                                        edtMessage.setText("")
//                                    }
//                            }
//                        }
                }
            }
        }
    }

    private fun showChat() {
        chatLiveData.observe(this@ChatActivity) { chatList ->
            val filteredUserChat = chatList.filter {
                (it.senderUid == uid || it.senderUid == if (prefManager.getRole() == "patient") dentist.uid else patient.uid)
                        && (it.recieverUid == uid || it.recieverUid == if (prefManager.getRole() == "patient") dentist.uid else patient.uid)
            }.sortedBy {
                it.time
            }

            binding.rvChat.apply {
                adapter = ChatAdapter(filteredUserChat)
                layoutManager = LinearLayoutManager(
                    applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    private fun getUserChat() {
        firestore.collection("chat")
//            .whereIn("senderUid", listOf(uid, dentist.uid))
//            .whereIn("receiverUid", listOf(uid, dentist.uid))
//            .orderBy("time")
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
}