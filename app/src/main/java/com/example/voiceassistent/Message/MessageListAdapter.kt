package com.example.voiceassistent.Message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.R
import java.util.*

class MessageListAdapter : RecyclerView.Adapter<MessageViewHolder>() {
    var messageList: ArrayList<Message> = ArrayList()
    private val ASSISTANT_TYPE = 0
    private val USER_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View
        if (viewType == USER_TYPE) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_message, parent, false) //создание сообщения от пользователя
        } else {
            view = LayoutInflater.from(parent.context).inflate(
                R.layout.assistant_message,
                parent,
                false
            ) //создание сообщения от ассистента
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messageViewHolder: MessageViewHolder = holder as MessageViewHolder
        messageViewHolder.bind(messageList[position])
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(index: Int): Int {
        val message = messageList[index]
        return if (message.isSend!!) {
            USER_TYPE
        } else ASSISTANT_TYPE
    }
}