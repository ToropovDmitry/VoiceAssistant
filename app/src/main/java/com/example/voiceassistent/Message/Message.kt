package com.example.voiceassistent.Message

import android.os.Parcel
import android.os.Parcelable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Message : Parcelable {
    var text: String? = null
    var name: String? = null
    var date: Date? = null
    var isSend: Boolean? = null

    constructor(text: String?) {
        this.text = text
        date = Date()
    }

    constructor(text: String?, isSend: Boolean) {
        this.text = text
        this.isSend = isSend
        date = Date()
        if (isSend) name = "Пользователь" else name = "Ассистент"
    }

    protected constructor(`in`: Parcel) {
        text = `in`.readString()
        val tmpIsSend = `in`.readByte()
        isSend = if (tmpIsSend.toInt() == 0) null else tmpIsSend.toInt() == 1
    }

    val CREATOR: Parcelable.Creator<Message> =
        object : Parcelable.Creator<Message> {
            override fun createFromParcel(`in`: Parcel): Message {
                return  Message(`in`)
            }

            override fun newArray(size: Int): Array<Message?> {
                return arrayOfNulls<Message>(size)
            }
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(text)
        dest.writeByte((if (isSend == null) 0 else if (isSend as Boolean) 1 else 2).toByte())
    }

    @Throws(ParseException::class)
    constructor (entity: MessageEntity) {
        text = entity.text
        val dateFormat = SimpleDateFormat()
        dateFormat.applyPattern("dd.MM.yyyy")
        date = dateFormat.parse(entity.date)
        if (entity.isSend === 1) isSend = true else isSend = false
    }
}