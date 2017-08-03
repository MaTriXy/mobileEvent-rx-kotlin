package com.tikalk.mobileevent.mobileevent.data

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CallLog
import android.support.v4.content.ContextCompat
import android.util.Log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch

/**
 * Created by shaulr on 02/08/2017.
 */
class CallLogManager(val context: Context) {
    private val TAG: String = "CallLogManager"
    private  var job: Job? = null
    private var  cancelled: Boolean = false

    fun read() : List<CallLogDao>{
        val ret = ArrayList<CallLogDao>()
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
            if(!cursor.moveToFirst()) {
                return ret
            }
            for(i in 0 .. cursor.count) {
                ret.add(CallLogDao(cursor))
            }
        } else  {
            Log.e(TAG, "permission READ_CALL_LOG not granted! Skipping query")
        }

        return ret
    }


    fun readAsync(listener: ICallLogListener) : Job? {
        cancelled = false
        val ret = ArrayList<CallLogDao>()
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
            try {
                job = launch(CommonPool) {
                    listener.onOperationStarted(ICallLogListener.Operation.read)

                    if (cursor.moveToFirst()) {
                        for(i in 0 .. cursor.count/10) {
                            for (j in 0..10) {
                                if (cancelled) break
                                ret.add(CallLogDao(cursor))
                            }
                            listener.onOperationProgress(ICallLogListener.Operation.read, ret.clone() as List<CallLogDao>)
                            ret.clear()
                        }
                    } else {
                        listener.onOperationError(ICallLogListener.Operation.read, "Cursor could not moveToFirst")
                    }
                }
            } finally {
                listener.onOperationEnded(ICallLogListener.Operation.read)
                return job
            }
        } else  {
            Log.e(TAG, "permission READ_CALL_LOG not granted! Skipping query")
            listener.onOperationError(ICallLogListener.Operation.read, "permission READ_CALL_LOG not granted! Skipping query")
            return null
        }
    }

    fun cancelCurrentJob() {
        cancelled = true
        job!!.cancel()
        job = null
    }

    fun write(logs: List<CallLogDao>): Int {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG)) {
            val contentValues = ArrayList<ContentValues>()
            logs.mapTo(contentValues) { it.toContentValues() }
            val valuesArray = contentValues.toTypedArray()
            return context.contentResolver.bulkInsert(CallLog.Calls.CONTENT_URI, valuesArray)
        } else {
            Log.e(TAG, "permission WRITE_CALL_LOG not granted! Skipping insert")
        }
        return -1
    }
}