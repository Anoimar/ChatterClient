package com.thernat.chatter.android.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thernat.chatter.android.R

class ChatAdapter (private var messages: List<MessageItem>):
    RecyclerView.Adapter<ChatAdapter.MessageItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return MessageItemHolder(view)
    }

    override fun onBindViewHolder(holder: MessageItemHolder, position: Int) {
        holder.msgField.text = messages[position].content
    }

    override fun getItemCount() = messages.size

    class MessageItemHolder(view: View): RecyclerView.ViewHolder(view) {
        val msgField: TextView = view.findViewById(R.id.text_view)
    }
}
