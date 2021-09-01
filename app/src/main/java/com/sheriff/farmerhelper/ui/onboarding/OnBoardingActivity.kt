package com.sheriff.farmerhelper.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sheriff.farmerhelper.MainActivity
import com.sheriff.farmerhelper.R
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        initButton()
    }

    private fun initButton(){
        try {
            btn_find_market.setOnClickListener {
                pbOnBoarding.visibility = View.VISIBLE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            btn_govt_scheme.setOnClickListener {
                pbOnBoarding.visibility = View.VISIBLE
                toGovtScheme = true
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    companion object {
        var toGovtScheme = false
    }
}