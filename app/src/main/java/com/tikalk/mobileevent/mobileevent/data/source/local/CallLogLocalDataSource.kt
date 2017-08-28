package com.tikalk.mobileevent.mobileevent.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import com.squareup.sqlbrite2.SqlBrite
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import com.tikalk.mobileevent.mobileevent.data.source.CallLogDataSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class CallLogLocalDataSource private constructor(context: Context) : CallLogDataSource {

    val sqlBrite = SqlBrite.Builder().build()
    val resolver = sqlBrite.wrapContentProvider(context.contentResolver, Schedulers.io())

    @SuppressLint("MissingPermission")
    override fun getCallLog(): Observable<List<CallLogDao>> {
        return resolver.createQuery(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc", false)
                .mapToList({ c -> CallLogDao(c) })
                .firstElement()
                .toObservable()
    }

    companion object {
        private lateinit var INSTANCE: CallLogLocalDataSource
        private var needsNewInstance = true

        @JvmStatic fun getInstance(context: Context): CallLogLocalDataSource {
            if (needsNewInstance) {
                INSTANCE = CallLogLocalDataSource(context)
                needsNewInstance = false
            }
            return INSTANCE
        }
    }
}