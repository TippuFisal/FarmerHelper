package com.sheriff.farmerhelper.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.ui.login.LoginActivity
import com.sheriff.farmerhelper.ui.onboarding.OnBoardingActivity
import com.sheriff.farmerhelper.utility.SharedPreference
import com.sheriff.farmerhelper.utility.Utils

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        callHomeScreen()
    }

    private fun callHomeScreen() {
        try {
            when {
                Utils.isConnected(this) -> {
                    Handler().postDelayed({
                        handleLogin()
                    }, SPLASH_TIME_OUT)
                }
                else -> {
                    Utils.singleButtonDialogWithTitle(
                        this, getString(R.string.str_no_internet), getString(
                            R.string.str_please_check_internet
                        ), getString(R.string.btn_retry)
                    ) {

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleLogin(){
        try {
            if (SharedPreference.retrivePreferenceManager(SharedPreference.USER_LOGIN_STATUS, this) == true){
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}