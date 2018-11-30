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
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import android.widget.Toast
import com.clipit.cliptit2.firebaseDirectory.FBAuthGlobal
import com.clipit.cliptit2.firebaseDirectory.RegisterClass
import com.clipit.cliptit2.MainActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.clipit.cliptit2.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_phone_verifiy.*


class PhoneVerifyActivity : AppCompatActivity() {
    private var verificationId : String? =null
    private val fbAuth = FirebaseAuth.getInstance()
    lateinit var phone : String
    private val fbData = FirebaseDatabase.getInstance().getReference("register")
    var resend  = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verifiy)
        if(android.os.Build.VERSION.SDK_INT >= 23){
            val windows : Window = this.window
            windows.statusBarColor  = this.getColor(R.color.material_new_blue_strong_200)
        }

        tool_bar_at_phone_verification.setNavigationOnClickListener {
            super.onBackPressed()
        }

        progressbar.visibility = View.GONE
        phone = intent.getStringExtra("phone")
        sendVerificationCode(phone)

        timer()
        resend_text_at_phone_verification.setOnClickListener {
            if(resend){
                sendVerificationCode(phone)
                timer()
                resend=false
            }
        }
        buttonSignIn.setOnClickListener {
            val code = editTextCode.text.toString().trim()
            if(code.isNotEmpty() || code.length>=6){

                verifyTheCode(code)
            }
            else{
                editTextCode.error = "Please enter the code"
                editTextCode.requestFocus()
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        progressbar.visibility=View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                callBack


        )



    }

    private fun  verifyTheCode(code:String){
        try {


            val credential = PhoneAuthProvider.getCredential(verificationId!!,code)
            signInWithCredential(credential)
        }catch (e:Exception){
            Toast.makeText(this,"Wrong Entry",Toast.LENGTH_SHORT).show()
            editTextCode.error = "Wrong Entry"
            editTextCode.requestFocus()
        }


    }


    private val callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code)
                //verifying the code
                verifyTheCode(code)
            }

            signInWithCredential(phoneAuthCredential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@PhoneVerifyActivity, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)
            verificationId = s
        }
    }

    private  fun signInWithCredential(credential: PhoneAuthCredential){

        fbAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) {
                    if(it.isSuccessful){

                        val progressDialog = ProgressDialog(this)
                        progressDialog.setTitle("Verifying")
                        progressDialog.setMessage("Verifying phone number and\n checking user is present.")
                        try{
                        progressDialog.show()}
                        catch (e:Exception){
                            e.printStackTrace()
                        }
                        fbData.child(phone).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(this@PhoneVerifyActivity, "Failed to Login", Toast.LENGTH_SHORT).show()
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
                                else{
                                    val intent = Intent(applicationContext, Profilecreate::class.java)
                                    startActivity(intent)

                                }

                            }
                        })




                    }else{
                        Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()
                    }
                }

    }
    private fun timer() {
        resend=false
        resend_image_at_phone_verification.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PhoneVerifyActivity, R.color.material_new_grey_strong_20))
        resend_text_at_phone_verification.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this@PhoneVerifyActivity, R.color.material_new_grey_strong_20)))
        object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                resend_image_at_phone_verification.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@PhoneVerifyActivity, R.color.material_new_grey_strong_100))
                resend_text_at_phone_verification.text = getString(R.string.resend_sms)
                resend_text_at_phone_verification.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(this@PhoneVerifyActivity, R.color.material_new_grey_strong_100)))
                time_counter_at_phone_verification.text = getString(R.string.time_end)
                resend = true
            }
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                time_counter_at_phone_verification.text = """0:$second"""
            }

        }.start()
    }


}
