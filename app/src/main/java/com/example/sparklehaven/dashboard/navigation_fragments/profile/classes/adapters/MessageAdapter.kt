package com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sparklehaven.R
import com.example.sparklehaven.dashboard.navigation_fragments.profile.classes.model.Message
import com.example.sparklehaven.utils.singleton.FirebaseModule

class MessageAdapter(private val messages: List<Message>, private val onReply: (Message) -> Unit) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val currentUser = FirebaseModule.firebaseAuth.currentUser?.uid
    private var replyPosition: Int? = null

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = if (viewType == VIEW_TYPE_SENT) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_sent, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false)
        }
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUser) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.text
        holder.itemView.setOnClickListener {
            onReply(message)
        }

        // Highlight the message being replied to
        if (position == replyPosition) {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    fun setReplyPosition(position: Int) {
        val previousPosition = replyPosition
        replyPosition = position
        notifyItemChanged(previousPosition ?: -1)
        notifyItemChanged(position)
    }

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }
}
