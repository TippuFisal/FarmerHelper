package com.sheriff.farmerhelper.ui.scheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.model.data.response.Scheme
import com.sheriff.farmerhelper.utility.Utils
import kotlinx.android.synthetic.main.fragment_scheme.*

class SchemeFragment : Fragment() {

    private lateinit var schemeViewModel: SchemeViewModel

    companion object {
        var TAG = "SchemeFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scheme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        schemeViewModel =
            ViewModelProvider(this).get(SchemeViewModel::class.java)
        initScheme()
    }


    private fun initScheme() {
        try {
            schemeViewModel.scheme.observe(requireActivity(), {
                Utils.log(TAG, it.toString())
                initSchemeAdapter(it)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSchemeAdapter(data: ArrayList<Scheme>) {
        try {
            val layoutManager = LinearLayoutManager(requireContext())
            rvScheme.layoutManager = layoutManager
            val adapter = SchemeAdapter(requireContext(), data)
            rvScheme.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}