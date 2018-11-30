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

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.clipit.cliptit2.MainActivity
import com.clipit.cliptit2.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    private val countryPhoneId = CountryPhoneId()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sharedPerfenece = this.getSharedPreferences("login",android.content.Context.MODE_PRIVATE)

        if(android.os.Build.VERSION.SDK_INT >= 23){
            val windows : Window = this.window
            windows.statusBarColor  = this.getColor(R.color.material_new_blue_strong_200)
        }

        tool_bar_at_login_activity.setNavigationOnClickListener {
            super.onBackPressed()
        }
       spinnerCountry.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,countryPhoneId.countryNames)
        spinnerCountry.onItemSelectedListener = this

        buttonContinue.setOnClickListener {
            val code = countryPhoneId.countryAreaCodes[spinnerCountry.selectedItemPosition]
            val number = editTextMobile.text.toString().trim()

            if(number.isEmpty()||code.isEmpty()){
                editTextMobile.error = "Please Enter the number"
                editTextMobile.requestFocus()
                return@setOnClickListener
            }
            val intent = Intent(this, PhoneVerifyActivity::class.java)
            intent.putExtra("phone","+$code$number")
            startActivity(intent)
        }

        val view = findViewById<TextView>(R.id.skip_login_at_activity_login)
        view.setOnClickListener {
            sharedPerfenece.edit().putInt("loginNumber",0).apply()
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        country_code_view_in_login.text = "+ ${countryPhoneId.countryAreaCodes[position]}"
    }
}
