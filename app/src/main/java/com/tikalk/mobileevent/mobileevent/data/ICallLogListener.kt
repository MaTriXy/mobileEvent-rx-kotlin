package com.tikalk.mobileevent.mobileevent.data

interface ICallLogListener {
    enum class Operation { write, read }

    fun onOperationStarted(operation: Operation)
    fun onOperationProgress(operation: Operation, objects: List<CallLogDao>)
    fun onOperationEnded(operation: Operation)
    fun onOperationError(operation: Operation, error: String)
}