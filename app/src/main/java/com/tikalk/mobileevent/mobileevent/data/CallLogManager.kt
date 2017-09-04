package com.tikalk.mobileevent.mobileevent.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.support.v4.content.ContextCompat
import android.util.Log
import com.squareup.sqlbrite2.BriteContentResolver
import com.squareup.sqlbrite2.SqlBrite
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.rx2.rxFlowable
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.*
import kotlin.coroutines.experimental.CoroutineContext


class CallLogManager(val context: Context) {
    private val TAG: String = "CallLogManager"
    private var job: Job? = null
    private var cancelled: Boolean = false

    val SELECTION_DATE = CallLog.Calls.DATE + " > ? and " + CallLog.Calls.DATE + " < ? "
    val SELECTION_NUMBER = CallLog.Calls.NUMBER + " = ?"
    val sqlBrite: SqlBrite = SqlBrite.Builder().build()
    val resolver: BriteContentResolver = sqlBrite.wrapContentProvider(context.contentResolver, Schedulers.io())

    fun getDateSelectionArgs(startDate: Date, endDate: Date) = arrayOf(startDate.time.toString(), endDate.time.toString())
    fun getNumberSelectionArgs(number: String) = arrayOf(number)

    fun query(selection: String? = null, selectionArgs: Array<String>? = null): List<CallLogDao> {
        val ret = ArrayList<CallLogDao>()
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, selection, selectionArgs, null)
            if (!cursor.moveToFirst()) {
                return ret
            }
            for (i in 0..cursor.count) {
                ret.add(CallLogDao(cursor))
            }
            cursor?.close()
        } else {
            Log.e(TAG, "permission READ_CALL_LOG not granted! Skipping query")
        }

        return ret
    }

    @SuppressLint("MissingPermission")
    fun queryRxSqbrite(selection: String? = null, selectionArgs: Array<String>? = null): Observable<List<CallLogDao>> {
        return resolver.createQuery(CallLog.Calls.CONTENT_URI, null, selection, selectionArgs, null, false)
                .mapToList({ c ->
                    CallLogDao(c)
                }).firstElement().toObservable()
    }

    suspend fun queryAsyncAnko(selection: String? = null, selectionArgs: Array<String>? = null): Deferred<List<CallLogDao>>? {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, selection, selectionArgs, null)
            return bg {
                queryAllLogs(cursor, selection, selectionArgs)
            }
        } else {
            return null
        }
    }

    fun queryAsyncCoroutines(listener: ICallLogListener, selection: String? = null, selectionArgs: Array<String>? = null): Job? {
        cancelled = false
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, selection, selectionArgs, null)
            try {
                job = launch(CommonPool) {
                    listener.onOperationStarted(ICallLogListener.Operation.read)

                    if (cursor.moveToFirst()) {
                        do {
                            listener.onOperationProgress(ICallLogListener.Operation.read, CallLogDao(cursor))
                        } while (cursor.moveToNext())
                        listener.onOperationEnded(ICallLogListener.Operation.read)
                        cursor?.close()
                    } else {
                        listener.onOperationError(ICallLogListener.Operation.read, "Cursor could not moveToFirst")
                    }
                }
            } finally {
                return job
            }
        } else {
            Log.e(TAG, "permission READ_CALL_LOG not granted! Skipping query")
            listener.onOperationError(ICallLogListener.Operation.read, "permission READ_CALL_LOG not granted! Skipping query")
            return null
        }
    }

    fun queryRxCoroutines(coroutineContext: CoroutineContext, selection: String? = null, selectionArgs: Array<String>? = null): Flowable<CallLogDao>? {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, selection, selectionArgs, null)
            return coroutinesGenerateFlowable(cursor, coroutineContext)
        }
        return null
    }

    private fun coroutinesGenerateFlowable(cursor: Cursor, coroutineContext: CoroutineContext): Flowable<CallLogDao> {
        return rxFlowable(coroutineContext) {
            if (cursor.moveToFirst()) {
                do {
                    send(CallLogDao(cursor))
                } while (cursor.moveToNext())
            }
        }
    }


    private fun queryAllLogs(cursor: Cursor, selection: String? = null, selectionArgs: Array<String>? = null): List<CallLogDao> {
        val ret = ArrayList<CallLogDao>()
        if (cursor.moveToFirst()) {
            do {
                ret.add(CallLogDao(cursor))
            } while (cursor.moveToNext())
        }
        return ret
    }


    fun deleteCallLogByNumber(number: String): Int {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            return context.contentResolver.delete(CallLog.Calls.CONTENT_URI, SELECTION_NUMBER, getNumberSelectionArgs(number))
        } else {
            Log.e(TAG, "permission WRITE_CALL_LOG not granted! Skipping delete")
        }
        return -1
    }

    fun deleteCallLogByDate(startDate: Date, endDate: Date): Int {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            return context.contentResolver.delete(CallLog.Calls.CONTENT_URI, SELECTION_DATE, getDateSelectionArgs(startDate, endDate))
        } else {
            Log.e(TAG, "permission WRITE_CALL_LOG not granted! Skipping delete")
        }
        return -1
    }

    fun deleteAllCallLog(): Int {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG)) {
            return context.contentResolver.delete(CallLog.Calls.CONTENT_URI,null,null)
        } else {
            Log.e(TAG, "permission WRITE_CALL_LOG not granted! Skipping delete")
        }
        return -1
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

    fun write(log: CallLogDao): Uri? {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALL_LOG)) {
            return context.contentResolver.insert(CallLog.Calls.CONTENT_URI, log.toContentValues())
        } else {
            Log.e(TAG, "permission WRITE_CALL_LOG not granted! Skipping insert")
        }
        return null
    }

}