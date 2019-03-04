/*
Clip-It

 Copyright (C) <2018>  <Royal Benny>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

*/
package com.clipit.cliptit2

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.bottomappbar.BottomAppBar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.transition.*
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import com.clipit.cliptit2.archiveDirectory.ArchiveFragment
import com.clipit.cliptit2.backgroundDirectory.AlarmReceiver
import com.clipit.cliptit2.backgroundDirectory.ForeGroundKotlin
import com.clipit.cliptit2.createDirectory.CreateActivity
import com.clipit.cliptit2.deleteDirectory.DeleteFragment
import com.clipit.cliptit2.deleteDirectory.isRestored
import com.clipit.cliptit2.firebaseDirectory.RegisterFBWorks
import com.clipit.cliptit2.helpSupport.HelpSupport
import com.clipit.cliptit2.homeDirectory.HomeFragmet
import com.clipit.cliptit2.loginDirectory.LoginActivity
import com.clipit.cliptit2.messageDirectory.MessageView
import com.clipit.cliptit2.recentDirectory.RecentFragment
import com.clipit.cliptit2.settingsDirectory.SettingsActivity
import com.clipit.cliptit2.tabbedViewDirectory.TabbedViewBottomSheet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

@SuppressLint("StaticFieldLeak")
lateinit var con : Context

var fabClicked = false
var number = 0
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(appBarId)
        val mAuth = FirebaseAuth.getInstance()
        con = this
        if(mAuth.currentUser!=null) {
            val registerFBWorks = RegisterFBWorks()
            registerFBWorks.checkUserPresentInRegister(this)
        }


        val sharedPerfeneceSettings = this.getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)

        if(sharedPerfeneceSettings.getInt("BackGround",1)==1) {

            val alarm = Context.ALARM_SERVICE
            val am = getSystemService(alarm) as AlarmManager
            val intent2 = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 12, intent2, 0)

            val type = AlarmManager.RTC
            am.setRepeating(type, System.currentTimeMillis(), 60000, pendingIntent)
            val receiver = ComponentName(this,AlarmReceiver::class.java)
            this.packageManager.setComponentEnabledSetting(
                    receiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP
            )
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, appBarId, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)




        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_to_use_as_home, HomeFragmet())
        transaction.addToBackStack(null)
        transaction.commit()
        nav_view.menu.getItem(0).isChecked = true
        if(fabClicked) {
            appBarId.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_app_bar_appear))
            floatingActionButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_shoot_out))
            view_at_app_bar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_app_bar_appear))
            fabClicked = false
        }
        floatingActionButton.setOnClickListener {

            val slide = Slide()
            slide.slideEdge = Gravity.BOTTOM
            slide.duration = 150
            val bottomAppBarView = findViewById<BottomAppBar>(R.id.appBarId) as ViewGroup
            TransitionManager.beginDelayedTransition(bottomAppBarView,slide)
            appBarId.visibility = View.GONE
            fabClicked = true
            val intent = Intent(applicationContext, CreateActivity::class.java)
            startActivity(intent)
            this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
            this.finish()
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else if(number==0){
            val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val service = activityManager.getRunningServices(Int.MAX_VALUE)
            if (service.isNotEmpty()) {
                service.forEach {
                    if (ForeGroundKotlin::class.java.name == it.service.className && it.pid != 0) {
                        stopService(Intent(this, ForeGroundKotlin::class.java))
                        return@forEach
                    }
                }

            }
            stopService(Intent(this, ForeGroundKotlin::class.java))
            this.finish()
        }
        else if(number!=0 && number!=2){
                supportFragmentManager.beginTransaction().replace(R.id.layout_to_use_as_home, HomeFragmet()).addToBackStack(null).commit()
                isRestored = 0
            val view = findViewById<FrameLayout>(R.id.layout_to_use_as_message_view)
            number=0
            view.visibility = View.GONE
            nav_view.menu.getItem(0).isChecked = true

        }else if(number==2){
            number=0
            nav_view.menu.getItem(0).isChecked = true
            super.onBackPressed()
        }
        else {
            nav_view.menu.getItem(0).isChecked = true
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.appbarmenuitems, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true


            R.id.appbar_search -> {

                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.layout_to_use_as_bottom_sheet,
                        TabbedViewBottomSheet()
                ).addToBackStack(null).commit()
                number = 2

            }

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_to_use_as_home, HomeFragmet())
                transaction.addToBackStack(null)
                transaction.commit()
                number = 0
                val view = findViewById<FrameLayout>(R.id.layout_to_use_as_message_view)
                view.visibility = View.GONE
            }
            R.id.nav_inbox -> {
                val view = findViewById<FrameLayout>(R.id.layout_to_use_as_message_view)
                view.visibility = View.VISIBLE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_to_use_as_message_view, MessageView())
                transaction.addToBackStack(null)
                transaction.commit()
                number = 1

            }
            R.id.nav_delete -> {
                val view = findViewById<FrameLayout>(R.id.layout_to_use_as_message_view)
                view.visibility = View.VISIBLE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_to_use_as_message_view, DeleteFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                number = 1

            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_archive -> {
                val view = findViewById<FrameLayout>(R.id.layout_to_use_as_message_view)
                view.visibility = View.VISIBLE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_to_use_as_message_view, ArchiveFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                number = 1
            }

            R.id.nav_logout -> {
                try {
                    val sharedPerfeneceSettings = this.getSharedPreferences("Settings",android.content.Context.MODE_PRIVATE)
                    sharedPerfeneceSettings.edit().putString("UserName","Not Login").apply()
                    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                    if (mAuth.currentUser != null) {

                        mAuth.signOut()
                        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error Occur", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.nav_recent -> {

                val view = findViewById<FrameLayout>(R.id.layout_to_use_as_message_view)
                view.visibility = View.VISIBLE
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_to_use_as_message_view, RecentFragment())
                transaction.addToBackStack(null)
                transaction.commit()
                number = 1
            }
            R.id.nav_help -> {
                val intent  = Intent(applicationContext,HelpSupport::class.java)
                startActivity(intent)
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    override fun onStop() {
        val intent = Intent(applicationContext,ForeGroundKotlin::class.java)

        try {

            stopService(intent)
        }catch (e:Exception){
            e.printStackTrace()
        }

        super.onStop()
    }








}

