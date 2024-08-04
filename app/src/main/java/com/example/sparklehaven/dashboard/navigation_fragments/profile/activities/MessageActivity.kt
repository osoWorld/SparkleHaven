package com.example.sparklehaven.dashboard.navigation_fragments.profile.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters.MessageAdapter
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.Message
import com.example.sparklehaven.databinding.ActivityMessageBinding
import com.example.sparklehaven.utils.singleton.FirebaseModule
import com.google.firebase.firestore.DocumentChange

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()
    private val db = FirebaseModule.firebaseFirestore
    private val currentUser = FirebaseModule.firebaseAuth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView
        messageAdapter = MessageAdapter(messageList) { message ->
            showReplyLayout(message)
        }
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            adapter = messageAdapter
        }

        // Set up ItemTouchHelper for swipe actions
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val message = messageList[position]
                showReplyLayout(message)
                messageAdapter.setReplyPosition(position)
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.3f // Customize swipe threshold if necessary
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMessages)

        // Fetch messages from Firestore
        fetchMessages()

        // Send button click listener
        binding.buttonSend.setOnClickListener {
            sendMessage()
        }

        // Close reply preview button
        binding.closeReply.setOnClickListener {
            closeReplyLayout()
        }

        // Text change listener for smooth animations
        binding.editTextMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchMessages() {
        db.collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("MessageActivity", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    Log.d("MessageActivity", "Snapshot size: ${snapshots.size()}")
                    messageList.clear() // Clear the old messages
                    for (document in snapshots.documents) {
                        val message = document.toObject(Message::class.java)
                        if (message != null) {
                            Log.d("MessageActivity", "New message: ${message.text}")
                            messageList.add(message)
                        }
                    }
                    messageAdapter.notifyDataSetChanged() // Notify adapter about data change
                    binding.recyclerViewMessages.scrollToPosition(messageList.size - 1) // Scroll to last message
                } else {
                    Log.d("MessageActivity", "No snapshots")
                }
            }
    }

    private fun sendMessage() {
        val text = binding.editTextMessage.text.toString().trim()
        if (text.isNotEmpty()) {
            val message = Message(
                senderId = currentUser!!,
                text = text,
                timestamp = System.currentTimeMillis()
            )
            db.collection("messages").add(message).addOnSuccessListener {
                binding.editTextMessage.text.clear()
                Log.d("MessageActivity", "Message sent successfully")
            }.addOnFailureListener { e ->
                Log.e("MessageActivity", "Error sending message", e)
            }
        }
    }

    private fun showReplyLayout(message: Message) {
        binding.replyLayout.visibility = View.VISIBLE
        binding.replyText.text = message.text
    }

    private fun closeReplyLayout() {
        binding.replyLayout.visibility = View.GONE
        messageAdapter.setReplyPosition(-1)
    }
}