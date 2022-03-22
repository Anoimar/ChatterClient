package com.thernat.chatter.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thernat.chatter.Greeting
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thernat.chatter.AppApi
import com.thernat.chatter.android.ui.ChatAdapter
import com.thernat.chatter.android.ui.MessageItem

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val messages: MutableList<MessageItem> = mutableListOf()
        val adapter = ChatAdapter(messages).also {
            findViewById<RecyclerView>(R.id.chat_window).adapter = it
        }
        AppApi().chat { from, msg, isMe ->
            messages.add(MessageItem(from, msg, isMe))
            runOnUiThread{
                adapter.notifyItemInserted(messages.size)
            }
        }
    }
}
