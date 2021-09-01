package com.sheriff.farmerhelper.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.model.data.request.RegisterRequest
import com.sheriff.farmerhelper.ui.login.LoginActivity
import com.sheriff.farmerhelper.ui.onboarding.OnBoardingActivity
import com.sheriff.farmerhelper.utility.SharedPreference
import com.sheriff.farmerhelper.utility.Utils
import com.sheriff.farmerhelper.utility.Utils.Companion.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var firestoreReference: FirebaseFirestore = FirebaseFirestore.getInstance()
    var isDone = true

    companion object {
        val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initUI()
    }

    private fun initUI(){
        try {
            btn_register.setOnClickListener {
                validateRegister()
            }

            ll_haveaccount.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun validateRegister(){
        try{
            when{
                tied_firstname.text!!.isEmpty() -> {
                    toast(this, "Please Enter Your Name")
                }
                tied_phonenumber.text!!.isEmpty() -> {
                    toast(this, "Please Enter Your Phone No")
                }
                tied_phonenumber.text!!.length < 10 -> {
                    toast(this, "Please Enter Your 10 digit Phone No")
                }
                tied_emailregister.text!!.isEmpty() -> {
                    toast(this, "Please Enter Your Email")
                }
                !Utils.isValidEmail(tied_emailregister.text!!.toString()) -> {
                    Utils.toast(this, "Please Enter Valid Email")
                }
                tied_paswdregister.text!!.isEmpty() -> {
                    toast(this, "Please Enter Your Password")
                }
                tied_address.text!!.isEmpty() -> {
                    toast(this, "Please Enter Your Address")
                }
                else -> {
                    checkMailAlreadyExits()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun checkMailAlreadyExits() {
        try{
            rlRegisterProgressBar.visibility = View.VISIBLE
            val emailArray = ArrayList<String>()
            emailArray.clear()
            firestoreReference.collection("accounts")
                .addSnapshotListener { snapchat, exception ->
                    when {
                        exception != null -> {
                            Utils.log(TAG, "Failed to fetchUserAppointmentData")
                        }
                        snapchat != null -> {
                            when(isDone){
                                true -> {
                                    Utils.log(TAG, "Success ")
                                    val documents = snapchat.documents
                                    emailArray.clear()
                                    for (i in 0 until documents.size){
                                        emailArray.add(documents[i].get("email").toString())
                                    }
                                    if (emailArray.isNotEmpty() && emailArray.contains(tied_emailregister.text!!.toString())){
                                        if (isDone){
                                            emailArray.clear()
                                            toast(this, "Email ID Already Exists")
                                            rlRegisterProgressBar.visibility = View.GONE
                                        }
                                    }else{
                                        isDone = false
                                        emailArray.clear()
                                        rlRegisterProgressBar.visibility = View.GONE
                                        initRegister()
                                    }
                                    emailArray.clear()
                                }
                                false -> isDone = false
                            }
                        }
                    }
                }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun initRegister(){
        try {
            val registerRequest = RegisterRequest(
                tied_firstname.text!!.toString(),
                tied_phonenumber.text!!.toString(),
                tied_emailregister.text!!.toString(),
                tied_paswdregister.text!!.toString(),
                tied_address.text!!.toString()
            )
            firestoreReference.collection("accounts").document(tied_emailregister.text!!.toString())
                .set(registerRequest)
                .addOnSuccessListener {
                    saveUserLogin()
                    rlRegisterProgressBar.visibility = View.GONE
                    toast(this, "Success")
                    startActivity(Intent(this, OnBoardingActivity::class.java))
                    // close this activity
                    finish()
                }
                .addOnFailureListener {
                    rlRegisterProgressBar.visibility = View.GONE
                    toast(this, it.message!!)
                }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    private fun saveUserLogin(){
        try {
            SharedPreference.savePreferenceManager(SharedPreference.USER_LOGIN_STATUS, true, this)
            SharedPreference.savePreferenceManager(SharedPreference.USER_NAME, tied_firstname.text!!.toString(), this)
            SharedPreference.savePreferenceManager(SharedPreference.USER_PHONE_NUMBER, tied_phonenumber.text!!.toString(), this)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}