package com.tikalk.mobileevent.mobileevent.dashboard

import android.Manifest
import android.os.Bundle
import android.provider.CallLog
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.calllog.CallLogFragment
import com.tikalk.mobileevent.mobileevent.calllog.CallLogPresenter
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import com.tikalk.mobileevent.mobileevent.data.CallLogManager
import com.tikalk.mobileevent.mobileevent.data.source.CallLogRepository
import com.tikalk.mobileevent.mobileevent.data.source.local.CallLogLocalDataSource
import com.tikalk.mobileevent.mobileevent.util.ActivityUtils

/**
 * Created by ronelg on 8/17/17.
 */
class DashboardActivity : AppCompatActivity() {

  private lateinit var drawerLayout: DrawerLayout

  private lateinit var dashboardPresenter: DashboardPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.callog_activity)
    requestPermissions()
     // Set up the toolbar.
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.let {
      it.setHomeAsUpIndicator(R.drawable.ic_menu)
      it.setDisplayHomeAsUpEnabled(true)
    }

    // Set up the navigation drawer.
    drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout)).apply {
      setStatusBarBackground(R.color.colorPrimaryDark)
    }
    val navigationView = findViewById<NavigationView>(R.id.nav_view)
    setupDrawerContent(navigationView)

    val dashboardFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
        as DashboardFragment? ?: DashboardFragment.newInstance().also {
      ActivityUtils.addFragmentToActivity(
          supportFragmentManager, it, R.id.contentFrame)
    }

    // Create the presenter
    dashboardPresenter = DashboardPresenter(CallLogRepository.getInstance(
        CallLogLocalDataSource.getInstance(applicationContext)),
        dashboardFragment)
  }
  fun requestPermissions() {
    val rxPermissions = RxPermissions(this) // where this is an Activity instance
    rxPermissions
            .request(Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALL_LOG)
            .subscribe { granted ->
              if (granted) { // Always true pre-M
                Toast.makeText(this, "permissions granted", Toast.LENGTH_LONG).show()
                val manager = CallLogManager(this)
                val uri = manager.write(CallLogDao(1,"323232", System.currentTimeMillis(), 1000, CallLog.Calls.INCOMING_TYPE,true,"Test 123"))
                android.util.Log.d("test", "uri=" + uri.toString())
              } else {
                Toast.makeText(this, "permissions denied", Toast.LENGTH_LONG).show()
              }
            }
  }
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      // Open the navigation drawer when the home icon is selected from the toolbar.
      drawerLayout.openDrawer(GravityCompat.START)
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setupDrawerContent(navigationView: NavigationView) {
    navigationView.setNavigationItemSelectedListener { menuItem ->
      // Close the navigation drawer when an item is selected.
      menuItem.isChecked = true
      drawerLayout.closeDrawers()
      true
    }
  }
}