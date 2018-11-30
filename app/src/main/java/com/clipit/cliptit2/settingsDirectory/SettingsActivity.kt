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
package com.clipit.cliptit2.settingsDirectory

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Window
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import com.clipit.cliptit2.backgroundDirectory.AlarmReceiver
import com.clipit.cliptit2.backgroundDirectory.ForeGroundKotlin
import com.clipit.cliptit2.databaseDirectory.JsonDataBase
import com.clipit.cliptit2.R
import kotlinx.android.synthetic.main.activity_settings_.*
import java.io.ByteArrayOutputStream

class SettingsActivity : AppCompatActivity() {
    private var userName : String? = null
    val db = JsonDataBase(this)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_)

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            val windows: Window = this.window
            windows.statusBarColor = this.getColor(R.color.material_new_brown_strong_10_transparent)
        }

        tool_bar_at_settings.setNavigationOnClickListener{
            super.onBackPressed()
        }

        val sharedPrefeneceSettings = this.getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        userName = sharedPrefeneceSettings.getString("UserName","Not Login")!!
        var userImage: ByteArray? = null
        val image = findViewById<ImageView>(R.id.profile_image_at_settings)
        try {
            Thread(Runnable {
                if(userName!!.isNotEmpty() &&userName!="Not Login") {
                    try {
                        userImage = db.readImage(userName!!)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    if (userImage != null) {
                        val imag = BitmapFactory.decodeByteArray(userImage, 0, userImage!!.size)
                        profile_image_at_settings.post { profile_image_at_settings.setImageBitmap(imag) }
                    }
                }
            }).start()

        }catch (e:Exception){
            e.printStackTrace()
        }


        image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 2)
            } else {
                if(userName!="Not Login"){
                    val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent,1)
                }
            }


        }




        user_name_at_settings.text = sharedPrefeneceSettings.getString("UserName", "Not Login")
        switch_to_turn_off_background_process.isChecked = sharedPrefeneceSettings.getInt("BackGround", 1) == 1
        switch_to_turn_off_background_process.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sharedPrefeneceSettings.edit().putInt("BackGround", 1).apply()
                val alarm = Context.ALARM_SERVICE
                val am = getSystemService(alarm) as AlarmManager
                val intent2 = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(this, 12, intent2, 0)

                val type = AlarmManager.RTC
                am.setRepeating(type, System.currentTimeMillis(), 60000, pendingIntent)
            } else {
                sharedPrefeneceSettings.edit().putInt("BackGround", 0).apply()
                val alarm = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarm.cancel(PendingIntent.getBroadcast(this, 12, Intent(this, AlarmReceiver::class.java), 0))
                stopService(Intent(this, ForeGroundKotlin::class.java))
            }
        }

        val seekBarSearching = findViewById<SeekBar>(R.id.seekbar_at_searching)
        seekBarSearching.progress = sharedPrefeneceSettings.getInt("Searching", 1000)
        text_view_to_display_searching_value.text = "${sharedPrefeneceSettings.getInt("Searching", 1000)} m"
        seekBarSearching.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                text_view_to_display_searching_value.text = "$progress m"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                val value = seekBar!!.progress
                println("Searching value is $value at starting")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val value = seekBar!!.progress
                if (value < 100) {
                    Toast.makeText(this@SettingsActivity, "Less than 100 not possible", Toast.LENGTH_SHORT).show()
                    seekBarSearching.progress = sharedPrefeneceSettings.getInt("Searching", 1000)
                    text_view_to_display_searching_value.text = "${sharedPrefeneceSettings.getInt("Searching", 1000)} m"
                } else {
                    println("Searching value is $value")
                    sharedPrefeneceSettings.edit().putInt("Searching", value).apply()
                }

            }

        })


        val seekBarTracking = findViewById<SeekBar>(R.id.seekbar_for_tracking)
        seekBarTracking.progress = sharedPrefeneceSettings.getInt("Tracking", 500)
        text_view_to_display_tracking_value.text = "${sharedPrefeneceSettings.getInt("Tracking", 500)} m"
        seekBarTracking.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                text_view_to_display_tracking_value.text = "$progress m"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                val value = seekBar!!.progress
                println("Tracking value is $value at starting")

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val value = seekBar!!.progress
                if (value < 100) {
                    Toast.makeText(this@SettingsActivity, "Less than 100 not possible", Toast.LENGTH_SHORT).show()
                    seekBarTracking.progress = sharedPrefeneceSettings.getInt("Tracking", 500)
                    text_view_to_display_tracking_value.text = "${sharedPrefeneceSettings.getInt("Tracking", 500)} m"
                } else {
                    println("Tracking value is $value")
                    sharedPrefeneceSettings.edit().putInt("Tracking", value).apply()
                }
            }
        })


        val seekBarDisturb = findViewById<SeekBar>(R.id.seekbar_for_dont_disturb)
        seekBarDisturb.progress = sharedPrefeneceSettings.getInt("Disturb", 5)
        text_view_to_display_disturb_value.text = "${sharedPrefeneceSettings.getInt("Disturb", 5)} min"
        seekBarDisturb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                text_view_to_display_disturb_value.text = "$progress min"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                val value = seekBar!!.progress
                println("Disturb value is $value at starting")

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val value = seekBar!!.progress
                if (value < 2) {
                    Toast.makeText(this@SettingsActivity, "Less than 2 min not possible", Toast.LENGTH_SHORT).show()
                    seekBarDisturb.progress = sharedPrefeneceSettings.getInt("Disturb", 5)
                    text_view_to_display_disturb_value.text = "${sharedPrefeneceSettings.getInt("Disturb", 5)} min"
                } else {
                    println("Disturb value is $value")
                    sharedPrefeneceSettings.edit().putInt("Disturb", value).apply()
                }
            }
        })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode==2){
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
                if(userName!="Not Login"){
                    val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent,1)
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==1&&resultCode== Activity.RESULT_OK && data!=null){

            try {

                if (userName!!.isNotEmpty() && userName != "Not Login") {

                    Thread(Runnable {
                        val image = data.data
                        val selectedImage = MediaStore.Images.Media.getBitmap(this.contentResolver, image)
                        profile_image_at_settings.post { profile_image_at_settings.setImageBitmap(selectedImage) }
                        val outputStream = ByteArrayOutputStream()
                        selectedImage?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                        val byteArray = outputStream.toByteArray()
                        db.insertImage(userName!!, byteArray)


                    }).start()


                }

            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
