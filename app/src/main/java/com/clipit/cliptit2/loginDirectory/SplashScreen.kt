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
package com.clipit.cliptit2.loginDirectory

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.clipit.cliptit2.MainActivity
import com.clipit.cliptit2.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash__screen)
        val sharedPerfenece = this.getSharedPreferences("login",android.content.Context.MODE_PRIVATE)
        if(android.os.Build.VERSION.SDK_INT >= 23){
            val windows : Window = this.window
            windows.statusBarColor  = this.getColor(android.R.color.transparent)
        }
        val flash = object : Thread(){
            override fun run() {
                try{
                    Thread.sleep(1000)
                    if(sharedPerfenece.getInt("loginNumber",1)!=0){
                    val intent = Intent(this@SplashScreen, LoginHome::class.java)
                    startActivity(intent)}
                    else{
                        val intent = Intent(this@SplashScreen, MainActivity::class.java)
                        startActivity(intent)
                    }
                    this@SplashScreen.finish()
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
        }
        flash.start()

    }
}
