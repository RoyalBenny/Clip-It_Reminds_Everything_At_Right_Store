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

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Window
import android.widget.Toast
import com.clipit.cliptit2.firebaseDirectory.FBAuthGlobal
import com.clipit.cliptit2.firebaseDirectory.RegisterClass
import com.clipit.cliptit2.MainActivity
import com.clipit.cliptit2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile_create.*

class Profilecreate : AppCompatActivity() {

    private val fbAuth = FirebaseAuth.getInstance()
    val userId = fbAuth.currentUser!!.uid
    val userPhone = fbAuth.currentUser!!.phoneNumber
    private val fbData = FirebaseDatabase.getInstance().getReference("register")

    override fun onCreate(savedInstanceState: Bundle?) {
        if(android.os.Build.VERSION.SDK_INT >= 23){
            val windows : Window = this.window
            windows.statusBarColor  = this.getColor(R.color.material_new_blue_strong_200)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_create)


        tool_bar_at_profile.setNavigationOnClickListener {
            super.onBackPressed()
        }

        fbData.child(userPhone!!).addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@Profilecreate, "Failed to Login", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    val fbAuthGlobal = p0.getValue(RegisterClass::class.java)!!
                    FBAuthGlobal.user = fbAuthGlobal
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finishAffinity()
                    startActivity(intent)
                    return
                }
            }
        })

         sign_in_button.setOnClickListener {
            if(editTextName.text!=null){
                checkUserPresentInRegister(this,editTextName.text.toString())
                val sharedPerfeneceSettings = this.getSharedPreferences("Settings",android.content.Context.MODE_PRIVATE)
                sharedPerfeneceSettings.edit().putString("UserName",editTextName.text.toString()).apply()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                finishAffinity()
                startActivity(intent)
            }
        }
    }


    private fun checkUserPresentInRegister(context: Context,name : String){
        fbData.child(userPhone!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,"Failed to Login", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChildren()){
                    val   fbAuthGlobal = p0.getValue(RegisterClass::class.java)!!
                    FBAuthGlobal.user = fbAuthGlobal



                    return
                }

                val key = fbData.push().key

                FBAuthGlobal.user = RegisterClass(userPhone, name,userId, key!!)
                fbData.child(userPhone).setValue(RegisterClass(userPhone, name,userId,key)).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(context,"Login Succesfull",Toast.LENGTH_SHORT).show()

                    }else{
                        Snackbar.make(sign_in_button,"Registration failed",Snackbar.LENGTH_SHORT).show()
                    }
                }

            }
        })

    }





}
