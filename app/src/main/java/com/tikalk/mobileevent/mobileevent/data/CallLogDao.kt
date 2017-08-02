package com.tikalk.mobileevent.mobileevent.data

import android.content.ContentValues
import android.database.Cursor
import android.provider.CallLog

/**
 * Created by shaulr on 02/08/2017.
 */
data class CallLogDao( var number: String?,
                       var date: Long,
                       var duration: Long,
                       var type: Int,
                       var new: Boolean,
                       var name: String? ) {
    constructor() : this(null, 0, 0, 0, false, null)

    constructor(cursor : Cursor) : this() {
        number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
        date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
        duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
        type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))
        new = (cursor.getInt(cursor.getColumnIndex(CallLog.Calls.NEW)) == 1)
        name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
    }

    fun toContentValues() : ContentValues {
        val values = ContentValues()
        values.put(CallLog.Calls.NUMBER, number)
        values.put(CallLog.Calls.DATE, date)
        values.put(CallLog.Calls.DURATION, duration)
        values.put(CallLog.Calls.TYPE, type)
        values.put(CallLog.Calls.NEW, new)
        values.put(CallLog.Calls.CACHED_NAME, name)
        return values
    }

}