package com.sheriff.farmerhelper.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.sheriff.farmerhelper.model.data.response.Market
import com.sheriff.farmerhelper.model.data.response.Scheme
import com.sheriff.farmerhelper.utility.Utils

class HomeViewModel : ViewModel() {


    private lateinit var firestore: FirebaseFirestore
    private var _market: MutableLiveData<ArrayList<Market>> =
        MutableLiveData<ArrayList<Market>>()

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
            firestore.collection("market").addSnapshotListener { snapshot, exception ->
                when { // if there is an exception
                    exception != null -> {
                        Utils.log(TAG, "Failed to initSchemeData ")
                        return@addSnapshotListener
                    }
                    snapshot != null -> { // in Success case
                        Utils.log(TAG, "Success ")
                        val allBannerImages = ArrayList<Market>()
                        val documents = snapshot.documents
                        documents.forEach {
                            val scheme = it.toObject(Market::class.java)
                            when {
                                scheme != null -> allBannerImages.add(scheme)
                            }
                        }
                        _market.value = allBannerImages
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal var market: MutableLiveData<ArrayList<Market>>
        get() {
            return _market
        }
        set(value) {
            _market = value
        }

    companion object {
        var TAG = "SchemeViewModel"
    }
}