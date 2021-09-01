package com.sheriff.farmerhelper.ui.marketdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.sheriff.farmerhelper.model.data.response.MarketDetail
import com.sheriff.farmerhelper.utility.Utils

class MarketDetailViewModel: ViewModel() {

    private lateinit var firestore: FirebaseFirestore
    private var _marketDetail: MutableLiveData<ArrayList<MarketDetail>> =
        MutableLiveData<ArrayList<MarketDetail>>()

    init {
        try {
            firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
            initSchemeData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSchemeData() {
        try {
            firestore.collection("marketdetail").addSnapshotListener { snapshot, exception ->
                when { // if there is an exception
                    exception != null -> {
                        Utils.log(TAG, "Failed to initSchemeData ")
                        return@addSnapshotListener
                    }
                    snapshot != null -> { // in Success case
                        Utils.log(TAG, "Success ")
                        val allBannerImages = ArrayList<MarketDetail>()
                        val documents = snapshot.documents
                        documents.forEach {
                            val marketDetail = it.toObject(MarketDetail::class.java)
                            when {
                                marketDetail != null -> allBannerImages.add(marketDetail)
                            }
                        }
                        _marketDetail.value = allBannerImages
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal var marketDetail: MutableLiveData<ArrayList<MarketDetail>>
        get() {
            return _marketDetail
        }
        set(value) {
            _marketDetail = value
        }

    companion object {
        var TAG = "MarketDetailViewModel"
    }


}