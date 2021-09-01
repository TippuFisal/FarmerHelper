package com.sheriff.farmerhelper.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.model.data.response.Market
import com.sheriff.farmerhelper.utility.Utils
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.sheriff.farmerhelper.ui.marketdetails.MarketDetailsActivity


class HomeAdapter(var mContext: Context, private var data: ArrayList<Market>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgMarket: AppCompatImageView = itemView.findViewById(R.id.imgMarket)
        val tvMarketName: MaterialTextView =
            itemView.findViewById(R.id.tvMarketName)
        val tvMarketAddress: MaterialTextView = itemView.findViewById(R.id.tvMarketAddress)
        val btnMap: MaterialButton = itemView.findViewById(R.id.btnMap)
        val btnOverview: MaterialButton = itemView.findViewById(R.id.btnOverview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Utils.setImage(holder.imgMarket, data[position].image_url, mContext)
            holder.tvMarketName.text = data[position].title
            holder.tvMarketAddress.text = data[position].address


            holder.btnMap.setOnClickListener {
                val url = data[position].redirect_url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                mContext.startActivity(intent)
            }

            holder.btnOverview.setOnClickListener {
                mContext.startActivity(Intent(mContext, MarketDetailsActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}