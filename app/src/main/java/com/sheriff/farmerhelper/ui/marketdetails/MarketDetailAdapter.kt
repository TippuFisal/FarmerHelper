package com.sheriff.farmerhelper.ui.marketdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.model.data.response.MarketDetail
import com.sheriff.farmerhelper.utility.Utils

class MarketDetailAdapter(var mContext: Context, private var data: ArrayList<MarketDetail>) :
    RecyclerView.Adapter<MarketDetailAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgMarketDetail: AppCompatImageView = itemView.findViewById(R.id.imgMarketDetail)
        val tvMarketDetailName: AppCompatTextView =
            itemView.findViewById(R.id.tvMarketDetailName)
        val tvMarketDetailPrice: AppCompatTextView =
            itemView.findViewById(R.id.tvMarketDetailPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_market_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Utils.setImage(holder.imgMarketDetail, data[position].image_url, mContext)
            holder.tvMarketDetailName.text = data[position].name
            holder.tvMarketDetailPrice.text = data[position].price
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}