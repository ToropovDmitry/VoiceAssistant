package com.example.voiceassistent

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, "messageDb", null, 1) {

//    val DATABASE_VERSION = 1
//    val DATABASE_NAME = "messageDb"
    val TABLE_MESSAGES = "messages"
    val FIELD_ID = "id"
    val FIELD_MESSAGE = "message"
    val FIELD_SEND = "send"
    val FIELD_DATE = "date"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + TABLE_MESSAGES + "(" +
                    FIELD_ID + " integer primary key," +
                    FIELD_MESSAGE + " text," +
                    FIELD_SEND + " integer," +
                    FIELD_DATE + " text" + ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists $TABLE_MESSAGES")
        onCreate(db)
    }
}