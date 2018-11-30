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
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MessageFBWork {

    private val fBData = FirebaseDatabase.getInstance().getReference("messages")



    fun sendMessage(context: Context, senderName:String, senderPhone:String, receiverPhone:String, items : List<ItemFBClass>, message:String){

     val key =  fBData.child(receiverPhone).push().key
        val dateFormat = java.text.SimpleDateFormat("dd MMM yyyy | HH:mm", Locale.US)
        val time = Calendar.getInstance().time
        val timestamp = dateFormat.format(time)

        fBData.child(receiverPhone).child(key!!).setValue(MessageClass(items,senderPhone,senderName,message,timestamp,false, key!!)).addOnCompleteListener {
            if (it.isSuccessful) Toast.makeText(context,"Send Message",Toast.LENGTH_SHORT).show()
            else Toast.makeText(context,"Failed to send",Toast.LENGTH_LONG).show()
        }
    }



}