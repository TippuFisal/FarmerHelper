package com.sheriff.farmerhelper.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.ui.onboarding.OnBoardingActivity
import com.sheriff.farmerhelper.ui.register.RegisterActivity
import com.sheriff.farmerhelper.utility.SharedPreference
import com.sheriff.farmerhelper.utility.Utils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    var firestoreReference: FirebaseFirestore = FirebaseFirestore.getInstance()
    var isDone = true

    companion object {
        val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initRegister()
    }

    private fun initRegister(){
        try{
            btn_REGISTER.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }

            btn_login.setOnClickListener {
                when {
                    tied_emaillogin.text!!.isEmpty() -> {
                        Utils.toast(this, "Please Enter Your Email")
                    }
                    !Utils.isValidEmail(tied_emaillogin.text!!.toString()) -> {
                        Utils.toast(this, "Please Enter Valid Email")
                    }
                    tied_paswdlogin.text!!.isEmpty() -> {
                        Utils.toast(this, "Please Enter Your Password")
                    }
                    else -> {
                        checkMailAlreadyExits()
                    }
                }
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    private fun checkMailAlreadyExits() {
        try{
            val emailArray = ArrayList<String>()
                firestoreReference.collection("accounts")
                .addSnapshotListener { snapchat, exception ->
                    when {
                        exception != null -> {
                            Utils.log(TAG, "Failed to fetchUserAppointmentData")
                        }
                        snapchat != null -> {
                            if (isDone){
                                Utils.log(TAG, "Success ")
                                val documents = snapchat.documents
                                for (i in 0 until documents.size){
                                    emailArray.add(documents[i].get("email").toString())
                                    if (documents[i].get("email") == tied_emaillogin.text!!.toString()){
                                        if (documents[i].get("password") == tied_paswdlogin.text!!.toString()){
                                            isDone = false
                                            val name = documents[i].get("name")
                                            val phone = documents[i].get("phone")
                                            saveUserLogin(name.toString(), phone.toString())
                                        }else{
                                            Utils.toast(this, "Wrong Password")
                                        }
                                    }
                                }
                                if (emailArray.isNotEmpty() && !emailArray.contains(tied_emaillogin.text!!.toString())){
                                    if (isDone){
                                        Utils.toast(this, "Email ID doesn't exists")
                                    }
                                }
                                emailArray.clear()
                            }
                        }
                    }
                }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun saveUserLogin(name: String, phone: String){
        try {
            SharedPreference.savePreferenceManager(SharedPreference.USER_LOGIN_STATUS, true, this)
            SharedPreference.savePreferenceManager(SharedPreference.USER_NAME, name, this)
            SharedPreference.savePreferenceManager(SharedPreference.USER_PHONE_NUMBER, phone, this)

            Utils.toast(this, "Success")
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}