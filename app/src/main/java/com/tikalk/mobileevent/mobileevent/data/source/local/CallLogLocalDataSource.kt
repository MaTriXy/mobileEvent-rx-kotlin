package com.tikalk.mobileevent.mobileevent.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import com.squareup.sqlbrite2.SqlBrite
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import com.tikalk.mobileevent.mobileevent.data.source.CallLogDataSource
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by ronelg on 8/6/17.
 */
class CallLogLocalDataSource private constructor(context: Context) : CallLogDataSource {

  val sqlBrite = SqlBrite.Builder().build()
  val resolver = sqlBrite.wrapContentProvider(context.contentResolver, Schedulers.io())

  @SuppressLint("MissingPermission")
  override fun getCallLog(): Observable<List<CallLogDao>> {
    return resolver.createQuery(CallLog.Calls.CONTENT_URI, null, null, null, null, false)
        .mapToList({ c ->
          val id = c.getInt(c.getColumnIndex(CallLog.Calls._ID))
          val number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER))
          val date = c.getLong(c.getColumnIndex(CallLog.Calls.DATE))
          val duration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION))
          val type = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))
          val new = (c.getInt(c.getColumnIndex(CallLog.Calls.NEW)) == 1)
          val name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME))

          CallLogDao(id,number,date,duration, type, new, name)
        })
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