package com.sheriff.farmerhelper.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.ui.login.LoginActivity
import com.sheriff.farmerhelper.utility.SharedPreference
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiData()
    }

    private fun initUiData(){
        try {
            val name = SharedPreference.retrivePreferenceManager(SharedPreference.USER_NAME, requireContext()).toString()
            val phone = SharedPreference.retrivePreferenceManager(SharedPreference.USER_PHONE_NUMBER, requireContext()).toString()
            tvFirstLetter.text = name[0].toString().toUpperCase()
            tvAccountName.text = name
            tvAccountPhone.text = phone


            btnSignout.setOnClickListener {
                SharedPreference.clearPreferenceManager(requireContext())
                Toast.makeText(requireContext(),"Logged out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}