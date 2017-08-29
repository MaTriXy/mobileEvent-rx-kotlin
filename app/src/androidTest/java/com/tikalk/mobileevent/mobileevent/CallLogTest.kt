package com.tikalk.mobileevent.mobileevent

import android.content.Context
import android.provider.CallLog
import android.support.test.InstrumentationRegistry
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import com.tikalk.mobileevent.mobileevent.data.CallLogManager
import com.tikalk.mobileevent.mobileevent.data.ICallLogListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.reactive.awaitFirst
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch


@RunWith(AndroidJUnit4::class)
class CallLogTest {
    private val  TAG = "CallLogTest"

    lateinit var testContext: Context
    lateinit var manager: CallLogManager
    val days = 1000 * 60 * 60 * 24
    @Rule
    @JvmField
    var permissionRule: GrantPermissionRule? =
            GrantPermissionRule.grant(android.Manifest.permission.WRITE_CALL_LOG,
                    android.Manifest.permission.READ_CALL_LOG)

    @Before
    fun setup() {
        testContext = InstrumentationRegistry.getContext()
        manager = CallLogManager(testContext)
    }

    @Test
    fun callLogTest() {
        //test write
        val logs = ArrayList<CallLogDao>()
        logs.add(CallLogDao(1, "123123", System.currentTimeMillis(), 1000, CallLog.Calls.OUTGOING_TYPE, true, "Test 1"))
        logs.add(CallLogDao(2, "456456", System.currentTimeMillis(), 2000, CallLog.Calls.INCOMING_TYPE, true, "Test 2"))
        logs.add(CallLogDao(3, "654321", System.currentTimeMillis() - days * 5, 20000, CallLog.Calls.OUTGOING_TYPE, true, "Test 3"))
        assertEquals("managed to write 3 records", manager.write(logs), 3)
        assertNotNull("managed to write a record", manager.write(CallLogDao(4, "11111", System.currentTimeMillis(), 1000, CallLog.Calls.INCOMING_TYPE, true, "Test 5")))
        assertNotNull("managed to write a record", manager.write(CallLogDao(5, "123123", System.currentTimeMillis() - days * 10, 1000, CallLog.Calls.OUTGOING_TYPE, true, "Test 4")))

        //test read all with no selection
        assertTrue("managed to read at least two records", manager.read().size >= 3)

        //test read with selection
        val fourDaysAgo = Date(System.currentTimeMillis() - 4 * days)
        val sixDaysAgo = Date(System.currentTimeMillis() - 6 * days)
        val dateSelectionArgs = manager.getDateSelectionArgs(sixDaysAgo, fourDaysAgo)
        val numberSelectionArgs = manager.getNumberSelectionArgs("123123")

        assertTrue("managed to read at least one record filtered by date", manager.read(manager.SELECTION_DATE, dateSelectionArgs).size >= 1)
        assertTrue("managed to read at least one record filtered by number", manager.read(manager.SELECTION_NUMBER, numberSelectionArgs).size >= 1)


        //test delete by selection
        assertTrue("managed to delete at least one record filtered by date", manager.deleteCallLogByDate(sixDaysAgo, fourDaysAgo) >= 1)
        assertTrue("managed to delete at least one record filtered by number", manager.deleteCallLogByNumber("123123") >= 1)

    }

    @Test
    fun testAsyncCoroutines() {
        val latch = CountDownLatch(1)
        var logs = ArrayList<CallLogDao>()
        var asyncError: String? = null
        manager.readAsync(object : ICallLogListener {
            override fun onOperationStarted(operation: ICallLogListener.Operation) {

            }

            override fun onOperationEnded(operation: ICallLogListener.Operation) {
                latch.countDown()
            }

            override fun onOperationProgress(operation: ICallLogListener.Operation, log: CallLogDao) {
                logs.add(log)
            }

            override fun onOperationError(operation: ICallLogListener.Operation, error: String) {
                asyncError = error
                latch.countDown()
            }
        })
        latch.await()

        assertNull(asyncError, asyncError)
        assertTrue("Got some call logs", logs.size > 0)
    }


    @Test
    fun testAsyncAnko() = runBlocking{
        val deferredLogs = manager.readAsyncAnko()
        assertNotNull("returned null", deferredLogs)
        val actualLogs = deferredLogs?.await()
        assertNotNull("got some logs", actualLogs)
        assertTrue("actually have some logs", actualLogs!!.size > 0)
    }


    @Test
    fun testRx2Coroutines() = runBlocking {
        val source = manager.coroutinesRxQuery(coroutineContext)
        if (source != null) {
            var success = false
            source.observeOn(Schedulers.io(), false, 1) // specify buffer size of 1 item
                    .doOnComplete {
                        Log.d(TAG, "rx complete")
                    }
                    .subscribe {
                        x -> Log.d(TAG, "got " + x.toString())
                        success = true
                    }
            source.awaitFirst()
            assertTrue("got published via RX", success)
        } else {
            assertTrue("failed to generate RX stream", false)
        }
    }

    @Test
    fun testRxSqBrite() {
        val latch = CountDownLatch(1)
        var gotList = ArrayList<CallLogDao> ()
        manager.queryRx().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    list: List<CallLogDao> ->
                    gotList.addAll( list)
                    latch.countDown()
        })
        latch.await()
        assertTrue("got some logs", gotList.size > 0)
    }
}