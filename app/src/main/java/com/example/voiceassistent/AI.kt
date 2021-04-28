package com.example.voiceassistent

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.voiceassistent.NumberInformation.InfoNumberToString
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer
import java.util.regex.Matcher
import java.util.regex.Pattern

class AI {
    private val dayFormat = SimpleDateFormat("dd.MM.yyyy")

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getAnswer(
        question: String,
        context: Context,
        callback: Consumer<String?>
    ) {
        val dictionary =
            HashMap<String, String?>()
        dictionary[context.getString(R.string.hello)] = context.getString(R.string.hello_answer)
        dictionary[context.getString(R.string.hi)] = context.getString(R.string.hello_answer)
        dictionary[context.getString(R.string.hel)] = context.getString(R.string.hello_answer)
        dictionary[context.getString(R.string.how_are_you_question)] =
            context.getString(R.string.how_are_you_answer)
        dictionary[context.getString(R.string.how_fares_it)] =
            context.getString(R.string.how_are_you_answer)
        dictionary[context.getString(R.string.what_is_up)] =
            context.getString(R.string.how_are_you_answer)
        dictionary[context.getString(R.string.what_are_you_doing_question_one)] =
            context.getString(R.string.what_are_you_doing_answer)
        dictionary[context.getString(R.string.what_are_you_doing_question_two)] =
            context.getString(R.string.what_are_you_doing_answer)
        dictionary[context.getString(R.string.what_are_you_doing_question_three)] =
            context.getString(R.string.what_are_you_doing_answer)
        dictionary[context.getString(R.string.what_are_you_doing_question_four)] =
            context.getString(R.string.what_are_you_doing_answer)
        dictionary[context.getString(R.string.what_day_today_question)] = getDay()
        dictionary[context.getString(R.string.what_time_is_it_now_question)] = getTime()
        dictionary[context.getString(R.string.what_day_of_the_week_question)] = getDayOfWeek()
        val infoNumberPattern = Pattern.compile(
            "факт о числе (\\d+)",
            Pattern.CASE_INSENSITIVE
        )
        val infoNumbMatcher: Matcher = infoNumberPattern.matcher(question)
        if (dictionary.containsKey(question.toLowerCase())) {
            callback.accept(dictionary[question.toLowerCase()])
            return
        }
        if (question.contains("сколько дней до")) {
            callback.accept(getDeadLine(question.toLowerCase()))
            return
        }
        if (infoNumbMatcher.find()) {
            val number = infoNumbMatcher.group(1)
            InfoNumberToString().getInfoNumber(number, object : Consumer<String?> {
                override fun accept(translation: String?) {
                    dictionary[number] = translation
                    callback.accept(dictionary[number])
                }
            })
        }
         else {
            callback.accept("Вопрос понял! Думаю.")
            return
        }
    }

    private fun getDay(): String? {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        return "Сегодня " + simpleDateFormat.format(date)
    }

    private fun getTime(): String? {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return "Сейчас " + simpleDateFormat.format(date)
    }

    private fun getDayOfWeek(): String? {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("EEEE")
        return "Сегодня " + simpleDateFormat.format(date)
    }

    private fun getDeadLine(DateEnd: String): String? {
        val date = Date()
        var diff: Long = -1
        try {
            val dateEnd = dayFormat.parse(DateEnd.substring(15))
            diff =
                Math.round((dateEnd.time - date.time) / 86400000.toDouble())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return if (diff < 0) "Данная дата уже прошла" else "До введенной даты осталось $diff дней"
    }
}