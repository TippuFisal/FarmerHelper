package com.sheriff.farmerhelper.ui.scheme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.model.data.response.Scheme
import com.sheriff.farmerhelper.utility.Utils

class SchemeAdapter(var mContext: Context, private var data: ArrayList<Scheme>) :
    RecyclerView.Adapter<SchemeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgNotification: AppCompatImageView = itemView.findViewById(R.id.imgScheme)
        val tvNotificationTitle: AppCompatTextView =
            itemView.findViewById(R.id.tvSchemeTitle)
        val tvNotificationDescription: AppCompatTextView =
            itemView.findViewById(R.id.tvSchemeDescription)
        val tvNotificationTime: AppCompatTextView = itemView.findViewById(R.id.tvSchemeTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scheme, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Utils.setImage(holder.imgNotification, data[position].image_url, mContext)
            holder.tvNotificationTitle.text = data[position].title
            holder.tvNotificationDescription.text = data[position].description
            holder.tvNotificationTime.text = data[position].date_time
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}