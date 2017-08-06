package com.tikalk.mobileevent.mobileevent.data.source

import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import com.tikalk.mobileevent.mobileevent.data.source.local.CallLogLocalDataSource
import io.reactivex.Observable

/**
 * Created by ronelg on 8/6/17.
 */
class CallLogRepository(val callLogLocalDataSource: CallLogLocalDataSource) : CallLogDataSource {

  override fun getCallLog(): Observable<List<CallLogDao>> {
    return callLogLocalDataSource.getCallLog()
  }

  companion object {
    private lateinit var INSTANCE: CallLogRepository
    private var needsNewInstance = true

    @JvmStatic fun getInstance(callLogLocalDataSource: CallLogLocalDataSource): CallLogRepository {
      if (needsNewInstance) {
        INSTANCE = CallLogRepository(callLogLocalDataSource)
        needsNewInstance = false
      }
      return INSTANCE
    }
  }

}