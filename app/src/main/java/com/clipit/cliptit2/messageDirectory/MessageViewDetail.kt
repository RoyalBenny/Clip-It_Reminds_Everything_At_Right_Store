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
package com.clipit.cliptit2.messageDirectory

import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Window
import android.widget.TextView
import com.clipit.cliptit2.firebaseDirectory.*
import com.clipit.cliptit2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_message_view_detail.*

class MessageViewDetail : AppCompatActivity() {

    private val messageGlobal = MessageGlobalClass().returnMessageArray()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_view_detail)
        setSupportActionBar(tool_bar_at_message_detail)
        val position = intent.getIntExtra("position",-1)
        if(android.os.Build.VERSION.SDK_INT >= 23){
            val windows : Window = this.window
            windows.statusBarColor  = this.getColor(R.color.material_new_brown_strong_10_transparent)
        }

        tool_bar_at_message_detail.setNavigationOnClickListener {
            super.onBackPressed()
        }

        val fbAuth = FirebaseAuth.getInstance()
        val fbData = FirebaseDatabase.getInstance().getReference("messages")
        val data = messageGlobal[position]
        messageGlobal[position].seen = true
        val view = findViewById<TextView>(R.id.letter_view_in_message_detail)
        view.text = data.senderName[0].toString()
        senderNameView_in_message_detail.text = data.senderName
        seenView_in_message_detail.setImageResource(R.drawable.ic_outline_done_all_24px)
        timeView_in_message_detail.text = data.timeStamp
        messageView_in_message_detail.text = data.message
        changecolor(data.senderName[0])

        fbData.child(fbAuth.currentUser!!.phoneNumber!!).child(data.key).setValue(MessageClass(data.items,data.senderPhone,data.senderName,data.message,data.timeStamp,true,data.key))
        val parent =ArrayList<String>()
        val child = ArrayList<ArrayList<String>>()
        data.items.forEach {
            parent.add(it.category)
            child.add(it.items as ArrayList<String>)

        }

        message_text_view_at_message_detail.text=""
        child.forEach {
            it.forEach {ki->

                message_text_view_at_message_detail.append(ki+"\n")
            }


        }
    }


    private fun changecolor(char : Char) {

        val holder2 = findViewById<TextView>(R.id.letter_view_in_message_detail)
        when(char.toUpperCase()){
            'A'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue_ink_))
            'B'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.shrine_colour_primay_pink))
            'C'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.status_bar_color_home))
            'D'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.yellow_colour))
            'E'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.borrown_color))
            'F'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.button_blue_100))
            'G'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_color_50))
            'H'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_error_50))
            'I'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPurple500))
            'J'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorGrey))
            'K'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorLightBlue300))
            'L'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorGreen400))
            'M'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.materialViolet))
            'N'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.materialGrey))
            'O'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material_new_blue_strong_200))
            'P'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material_new_rose_strong_100))
            'Q'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material_new_rose_strong_200))
            'R'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material_strong_blue_50))
            'S'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material_strong_violet_50))
            'T'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material_new_grey_strong_20))
            'U'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material_new_grey_strong_100))
            'V'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.new_material_grey_50))
            'W'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorGreen800))
            'X'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.material))
            'Y'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_50))
            'Z'->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red_color_30))

            else->holder2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
        }

    }

    override fun onBackPressed() {
        messageAdapter!!.notifyDataSetChanged()
        super.onBackPressed()
    }
}
