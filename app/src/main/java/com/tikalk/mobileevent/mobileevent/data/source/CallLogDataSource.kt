package com.tikalk.mobileevent.mobileevent.data.source

import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import io.reactivex.Observable

/**
 * Created by ronelg on 8/6/17.
 */
interface CallLogDataSource {

  fun getCallLog(): Observable<List<CallLogDao>>
}