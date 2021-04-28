package com.example.voiceassistent

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.voiceassistent.Message.Message
import com.example.voiceassistent.Message.MessageEntity
import com.example.voiceassistent.Message.MessageListAdapter
import java.text.ParseException
import java.util.*
import java.util.function.Consumer

class MainActivity : AppCompatActivity() {
    protected var sendButton: Button? = null
    protected var questionText: EditText? = null
    protected var chatMessageList: RecyclerView? = null
    protected var textToSpeech: TextToSpeech? = null
    protected var messageListAdapter: MessageListAdapter? = null
    val nameVariableKey = "NAME_VARIABLE"
    val textViewTextKey = "TEXTVIEW_TEXT"
    val APP_PREFERENCES = "mysettings"
    private var isLight = true
    private val THEME = "THEME"
    var sPref: SharedPreferences? = null
    var dBHelper: DBHelper? = null
    var database: SQLiteDatabase? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        sPref =
            getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        isLight = sPref?.getBoolean(THEME, true)!!
        if (!isLight) delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        super.onCreate(savedInstanceState)
        dBHelper = DBHelper(this)
        database = dBHelper!!.writableDatabase
        messageListAdapter = MessageListAdapter()
        if (savedInstanceState == null) {
            val cursor =
                database!!.query(dBHelper!!.TABLE_MESSAGES, null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                val messageIndex = cursor.getColumnIndex(dBHelper!!.FIELD_MESSAGE)
                val dateIndex = cursor.getColumnIndex(dBHelper!!.FIELD_DATE)
                val sendIndex = cursor.getColumnIndex(dBHelper!!.FIELD_SEND)
                do {
                    val entity = MessageEntity(
                        cursor.getString(messageIndex),
                        cursor.getString(dateIndex), cursor.getInt(sendIndex)
                    )
                    var message: Message? = null
                    try {
                        message = Message(entity)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    if (message != null) {
                        messageListAdapter!!.messageList.add(message)
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        setContentView(R.layout.activity_main)
        sendButton = findViewById(R.id.sendButton)
        questionText = findViewById(R.id.questionField)
        chatMessageList = findViewById(R.id.chatMessageList)
        chatMessageList!!.setLayoutManager(LinearLayoutManager(this))
        chatMessageList!!.setAdapter(messageListAdapter)
        sendButton!!.setOnClickListener(View.OnClickListener { onSend() })
        textToSpeech = TextToSpeech(applicationContext,
            OnInitListener { i ->
                if (i != TextToSpeech.ERROR) {
                    textToSpeech!!.language = Locale("ru")
                }
            })
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected fun onSend() {
        if (!questionText!!.text.toString().isEmpty()) {
            val text = questionText!!.text.toString()
            messageListAdapter!!.messageList.add(Message(text, true))
            AI().getAnswer(text, this, object : Consumer<String?> {
                override fun accept(answer: String?) {
                    messageListAdapter!!.messageList.add(Message(answer, false))
                    chatMessageList!!.scrollToPosition(messageListAdapter!!.messageList.size - 1)
                    questionText!!.setText("")
                    textToSpeech!!.speak(answer, TextToSpeech.QUEUE_FLUSH, null, null)
                    messageListAdapter!!.notifyDataSetChanged()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.day_settings -> {
                isLight = true
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
            R.id.night_settings -> {
                isLight = false
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        val editor = sPref!!.edit()
        editor.putBoolean(THEME, isLight)
        editor.apply()
        database!!.delete(dBHelper!!.TABLE_MESSAGES, null, null)
        for (i in 0 until messageListAdapter!!.messageList.size) {
            val entity = MessageEntity(messageListAdapter!!.messageList[i])
            val contentValues = ContentValues()
            contentValues.put(dBHelper!!.FIELD_MESSAGE, entity.text)
            contentValues.put(dBHelper!!.FIELD_SEND, entity.isSend)
            contentValues.put(dBHelper!!.FIELD_DATE, entity.date)
            database!!.insert(dBHelper!!.TABLE_MESSAGES, null, contentValues)
        }
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            "messageHistory",
            messageListAdapter!!.messageList as ArrayList<out Parcelable?>
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        messageListAdapter!!.messageList =
            savedInstanceState.getParcelableArrayList<Parcelable>("messageHistory") as ArrayList<Message>
    }
}