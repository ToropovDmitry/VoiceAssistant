package com.example.voiceassistent.Message

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.R
import java.text.DateFormat
import java.text.SimpleDateFormat

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected var messageText: TextView? = null
    protected var messageDate: TextView? = null
    protected var messageName: TextView? = null

    init {
        messageText = itemView.findViewById(R.id.messageTextView)
        messageDate = itemView.findViewById(R.id.messageDateView)
        messageName = itemView.findViewById(R.id.nameTextView)
    }

    fun bind(message: Message) {
        messageText!!.text = message.text
        val fmt: DateFormat = SimpleDateFormat()
        messageDate!!.text = fmt.format(message.date)
        messageName!!.text = message.name
    }

}