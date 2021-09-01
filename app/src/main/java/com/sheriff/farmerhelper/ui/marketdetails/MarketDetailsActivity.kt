package com.sheriff.farmerhelper.ui.marketdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.model.data.response.MarketDetail
import com.sheriff.farmerhelper.utility.Utils
import kotlinx.android.synthetic.main.activity_market_details.*
import kotlinx.android.synthetic.main.fragment_scheme.*

class MarketDetailsActivity : AppCompatActivity() {

    private lateinit var marketDetailViewModel: MarketDetailViewModel

    companion object {
        val TAG = "MarketDetailsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_details)

        marketDetailViewModel = ViewModelProvider(this).get(MarketDetailViewModel::class.java)
        initMarketDetail()
    }

    private fun initMarketDetail() {
        try {
            marketDetailViewModel.marketDetail.observe(this, {
                Utils.log(TAG, it.toString())
                initMarketDetailAdapter(it)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initMarketDetailAdapter(data: ArrayList<MarketDetail>) {
        try {
            val layoutManager = LinearLayoutManager(this)
            rvMarketDetail.layoutManager = layoutManager
            val adapter = MarketDetailAdapter(this, data)
            rvMarketDetail.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}