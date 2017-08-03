package com.tikalk.mobileevent.mobileevent

import android.content.Context
import android.provider.CallLog
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import com.tikalk.mobileevent.mobileevent.data.CallLogManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule
import android.support.test.rule.GrantPermissionRule
import com.tikalk.mobileevent.mobileevent.data.ICallLogListener
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.CountDownLatch


/**
 * Created by shaulr on 02/08/2017.
 */
@RunWith(AndroidJUnit4::class)
class CallLogTest {

    lateinit var context : Context
    lateinit var manager : CallLogManager

    @Rule @JvmField var permissionRule: GrantPermissionRule? =
            GrantPermissionRule.grant(  android.Manifest.permission.WRITE_CALL_LOG,
                                        android.Manifest.permission.READ_CALL_LOG)

    @Before
    fun setup() {
        context = InstrumentationRegistry.getContext()
        manager = CallLogManager(context)
    }

    @Test
    fun callLogTest() {
        val logs = ArrayList<CallLogDao>()
        logs.add(CallLogDao("123123", System.currentTimeMillis(), 1000, CallLog.Calls.OUTGOING_TYPE,true,"Test 1"))
        logs.add(CallLogDao("456456", System.currentTimeMillis(), 2000, CallLog.Calls.INCOMING_TYPE,true,"Test 2"))

        assertEquals("managed to write two records", manager.write(logs), 2)
        assertTrue("managed to read at least two records", manager.read().size >= 2)
    }

    @Test
     fun testAsync() = runBlocking<Unit> {
        var logs = ArrayList<CallLogDao>()
        var asyncError: String? = null
        var job = manager.readAsync(object : ICallLogListener {
            override fun onOperationStarted(operation: ICallLogListener.Operation) {

            }

            override fun onOperationEnded(operation: ICallLogListener.Operation) {
            }

            override fun onOperationProgress(operation: ICallLogListener.Operation, objects: List<CallLogDao>) {
                logs.addAll(objects)
            }

            override fun onOperationError(operation: ICallLogListener.Operation, error: String) {
                asyncError = error
            }
        })
        if(job != null) {
            job.join()
        }
        assertNull(asyncError, asyncError)
        assertTrue("Got some call logs", logs.size > 0)
    }
}