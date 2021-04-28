package com.example.voiceassistent.Message

import java.text.SimpleDateFormat
import java.util.*

class MessageEntity {
    var text: String? = null
    var date: String? = null
    var isSend = 0

    constructor(text: String?, date: String?, isSend: Int) {
        this.text = text
        this.date = date
        this.isSend = isSend
    }

    constructor(message: Message) {
        text = message.text
        val date = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        this.date = dateFormat.format(date)
        if (message.isSend!!) isSend = 1 else isSend = 0
    }
}