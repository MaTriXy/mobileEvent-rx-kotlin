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
import java.util.*


/**
 * Created by shaulr on 02/08/2017.
 */
@RunWith(AndroidJUnit4::class)
class CallLogTest {

    lateinit var context : Context
    lateinit var manager : CallLogManager
    val days = 1000 * 60 * 60 * 24
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
        //test write
        val logs = ArrayList<CallLogDao>()
        logs.add(CallLogDao(1,"123123", System.currentTimeMillis(), 1000, CallLog.Calls.OUTGOING_TYPE,true,"Test 1"))
        logs.add(CallLogDao(2,"456456", System.currentTimeMillis(), 2000, CallLog.Calls.INCOMING_TYPE,true,"Test 2"))
        logs.add(CallLogDao(3,"654321", System.currentTimeMillis() -  days * 5, 20000, CallLog.Calls.OUTGOING_TYPE,true,"Test 3"))
        assertEquals("managed to write 3 records", manager.write(logs), 3)
        assertNotNull("managed to write a record", manager.write(CallLogDao(4,"11111", System.currentTimeMillis(), 1000, CallLog.Calls.INCOMING_TYPE,true,"Test 5")))
        assertNotNull("managed to write a record", manager.write(CallLogDao(5,"123123", System.currentTimeMillis()-  days * 10, 1000, CallLog.Calls.OUTGOING_TYPE,true,"Test 4")))

        //test read all with no selection
        assertTrue("managed to read at least two records", manager.read().size >= 3)

        //test read with selection
        val fourDaysAgo = Date(System.currentTimeMillis() - 4 * days)
        val sixDaysAgo = Date(System.currentTimeMillis() - 6 * days)
        val dateSelectionArgs = manager.getDateSelectionArgs(sixDaysAgo, fourDaysAgo)
        val numberSelectionArgs = manager.getNumberSelectionArgs("123123")

        assertTrue("managed to read at least one record filtered by date", manager.read(manager.SELECTION_DATE, dateSelectionArgs ).size >= 1)
        assertTrue("managed to read at least one record filtered by number", manager.read(manager.SELECTION_NUMBER, numberSelectionArgs ).size >= 1)


        //test delete by selection
        assertTrue("managed to delete at least one record filtered by date", manager.deleteCallLogByDate(sixDaysAgo, fourDaysAgo) >= 1)
        assertTrue("managed to delete at least one record filtered by number", manager.deleteCallLogByNumber("123123") >= 1)

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