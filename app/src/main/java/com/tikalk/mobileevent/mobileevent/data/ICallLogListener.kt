package com.tikalk.mobileevent.mobileevent.data

/**
 * Created by shaulr on 03/08/2017.
 */
interface ICallLogListener {
    enum class Operation { write, read}
    fun onOperationStarted( operation: Operation)
    fun onOperationProgress( operation: Operation, objects : List<CallLogDao>)
    fun onOperationEnded( operation: Operation)
    fun onOperationError(operation: Operation, error: String)
}