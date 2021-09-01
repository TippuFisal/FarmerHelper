package com.sheriff.farmerhelper.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.sheriff.farmerhelper.R
import com.sheriff.farmerhelper.utility.Utils
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sheriff.farmerhelper.model.data.response.Market
import com.sheriff.farmerhelper.model.data.response.Scheme
import com.sheriff.farmerhelper.ui.scheme.SchemeAdapter
import com.sheriff.farmerhelper.ui.scheme.SchemeFragment
import com.sheriff.farmerhelper.ui.scheme.SchemeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_scheme.*
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocation: Location
    val PERMISSION_ID = 1010

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermission()
        getLastLocation()
        initHome()
    }

    fun requestPermission() {
        //this function will allows us to tell the user to request the necessary permsiion if they are not garented
        try {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_ID
            )
            getLastLocation()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isLocationEnabled(): Boolean {
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.log(TAG, "You have the Permission")
            }
        }
    }

    /**
     * getLastLocation
     */
    fun getLastLocation() {
        if (CheckPermission()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        try{
                            mLocation = location
                            Log.d(TAG, "Your Location:" + location.longitude)
                            // tvLocationName.setText(getCityName(location.latitude, location.longitude), TextView.BufferType.EDITABLE);
                            val geocoder: Geocoder
                            geocoder = Geocoder(requireContext(), Locale.getDefault())
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);

                            tv_yourlocation.visibility =View.VISIBLE
                            tv_yourlocation.text = "Your in this location : ${addresses.get(0).getLocality()}, ${addresses.get(0).postalCode}"

                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                tv_yourlocation.visibility =View.GONE
                Toast.makeText(
                    requireContext(),
                    "Please Turn on Your device Location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            requestPermission()
        }
    }

    /**
     * NewLocationData
     */
    fun NewLocationData() {
        try {
            val locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 0
            locationRequest.fastestInterval = 0
            locationRequest.numUpdates = 1
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            mLocation = locationResult.lastLocation
            Log.d(TAG, "your last last location: " + mLocation.longitude.toString())
            //  tvLocationName.text = "You Last Location is : Long: " + lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + getCityName(lastLocation.latitude, lastLocation.longitude)
        }
    }

    fun CheckPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun initHome() {
        try {
            homeViewModel.market.observe(requireActivity(), {
                Utils.log(TAG, it.toString())
                initHomeAdapter(it)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initHomeAdapter(data: ArrayList<Market>) {
        try {
            val layoutManager = LinearLayoutManager(requireContext())
            rvHome.layoutManager = layoutManager
            val adapter = HomeAdapter(requireContext(), data)
            rvHome.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        var TAG = "HomeFragment"
    }

}