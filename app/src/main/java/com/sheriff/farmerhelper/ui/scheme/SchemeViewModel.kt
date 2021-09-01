package com.sheriff.farmerhelper.ui.scheme

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.sheriff.farmerhelper.model.data.response.Scheme
import com.sheriff.farmerhelper.utility.Utils

class SchemeViewModel : ViewModel() {

    private lateinit var firestore: FirebaseFirestore
    private var _scheme: MutableLiveData<ArrayList<Scheme>> =
        MutableLiveData<ArrayList<Scheme>>()

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
            firestore.collection("scheme").addSnapshotListener { snapshot, exception ->
                when { // if there is an exception
                    exception != null -> {
                        Utils.log(TAG, "Failed to initSchemeData ")
                        return@addSnapshotListener
                    }
                    snapshot != null -> { // in Success case
                        Utils.log(TAG, "Success ")
                        val allBannerImages = ArrayList<Scheme>()
                        val documents = snapshot.documents
                        documents.forEach {
                            val scheme = it.toObject(Scheme::class.java)
                            when {
                                scheme != null -> allBannerImages.add(scheme)
                            }
                        }
                        _scheme.value = allBannerImages
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal var scheme: MutableLiveData<ArrayList<Scheme>>
        get() {
            return _scheme
        }
        set(value) {
            _scheme = value
        }

    companion object {
        var TAG = "SchemeViewModel"
    }
}