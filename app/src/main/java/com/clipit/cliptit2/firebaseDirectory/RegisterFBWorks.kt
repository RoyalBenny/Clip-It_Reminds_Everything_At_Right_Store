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
package com.clipit.cliptit2.firebaseDirectory

import android.content.Context
import android.widget.Toast
import com.clipit.cliptit2.con
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterFBWorks {

    private val fbAuth = FirebaseAuth.getInstance()
    val userPhone = fbAuth.currentUser!!.phoneNumber
    private val fbData = FirebaseDatabase.getInstance().getReference("register")
    val sharedPerfeneceSettings = con.getSharedPreferences("Settings",android.content.Context.MODE_PRIVATE)!!
    fun checkUserPresentInRegister(context: Context) {
        fbData.child(userPhone!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Failed to Login", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    val fbAuthGlobal = p0.getValue(RegisterClass::class.java)!!
                    FBAuthGlobal.user = fbAuthGlobal
                    sharedPerfeneceSettings.edit().putString("UserName",fbAuthGlobal.userName).apply()
                    return
                }


            }

        })
    }




    fun sendMessage(context: Context, senderName: String, phone: String, items: List<ItemFBClass>, message: String) {
        fbData.child(phone).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.hasChildren()) {
                    Toast.makeText(context, "No such user", Toast.LENGTH_LONG).show()
                    return
                }

                val messageFBWork = MessageFBWork()
                messageFBWork.sendMessage(context, senderName, userPhone!!, phone, items, message)


            }

        })


    }

}



