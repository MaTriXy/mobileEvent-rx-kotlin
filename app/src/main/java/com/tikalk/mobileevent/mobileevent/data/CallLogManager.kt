package com.tikalk.mobileevent.mobileevent.data

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CallLog
import android.support.v4.content.ContextCompat
import android.util.Log

/**
 * Created by shaulr on 02/08/2017.
 */
class CallLogManager(val context: Context) {
    private val TAG: String = "CallLogManager"

    fun read() : List<CallLogDao>{
        val ret = ArrayList<CallLogDao>()
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
            if(!cursor.moveToFirst()) {
                return ret
            }
            for(i in 0 .. cursor.count) {
                val logItem = CallLogDao(cursor)
                ret.add(logItem)
            }
        } else  {
            Log.e(TAG, "permission READ_CALL_LOG not granted! Skipping query")
        }

        return ret
    }

    fun write(logs: List<CallLogDao>): Int {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG)) {
            val contentValues = ArrayList<ContentValues>()
            for (log in logs) {
                contentValues.add(log.toContentValues())
            }
            val valuesArray = contentValues.toTypedArray()
            return context.contentResolver.bulkInsert(CallLog.Calls.CONTENT_URI, valuesArray)
        } else {
            Log.e(TAG, "permission WRITE_CALL_LOG not granted! Skipping insert")
        }
        return -1
    }
}