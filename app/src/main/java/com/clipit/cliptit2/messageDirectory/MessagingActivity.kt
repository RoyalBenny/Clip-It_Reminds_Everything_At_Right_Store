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

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.clipit.cliptit2.firebaseDirectory.FBAuthGlobal
import com.clipit.cliptit2.firebaseDirectory.ItemFBClass
import com.clipit.cliptit2.firebaseDirectory.RegisterFBWorks
import com.clipit.cliptit2.R
import kotlinx.android.synthetic.main.activity_messaging.*

class MessagingActivity : AppCompatActivity() {

    var category: String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)
        setSupportActionBar(tool_bar_at_message_send)


        if(android.os.Build.VERSION.SDK_INT >= 23){
            val windows : Window = this.window
            windows.statusBarColor  = this.getColor(R.color.colorGreen800)
        }

        tool_bar_at_message_send.setNavigationOnClickListener {
            super.onBackPressed()
        }

        category = intent.getStringExtra("category")
        val items = intent.getStringArrayListExtra("items")
        itemEditViewMessagingActivity.setText("")
        for (i in 0 until items.size) {
            itemEditViewMessagingActivity.append(items[i])
            if (i != items.size - 1) itemEditViewMessagingActivity.append("\n")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.message_send_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.send_message_in_MS -> {



                if (phoneEdit.text.isEmpty()) {
                    Toast.makeText(this, "Please enter the phone number", Toast.LENGTH_SHORT).show()
                }
                else {
                    val itemArray = itemEditViewMessagingActivity.text.toString().split("\n")
                    val registerFBWorks = RegisterFBWorks()
                    val phone = phoneEdit.text.toString()
                    val message = messageEdit.text.toString()
                    val fBAuthGlobal = FBAuthGlobal().returnCurrentUserDetail()
                    registerFBWorks.sendMessage(this, fBAuthGlobal.userName, phone, listOf(ItemFBClass(category, itemArray)), message)
                }
            }


        }
        return super.onOptionsItemSelected(item)
    }


}