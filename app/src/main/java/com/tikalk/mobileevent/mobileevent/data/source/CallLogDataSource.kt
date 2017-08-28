package com.tikalk.mobileevent.mobileevent.data.source

import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import io.reactivex.Observable

interface CallLogDataSource {

    fun getCallLog(): Observable<List<CallLogDao>>
}