package com.example.arborparker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arborparker.databinding.ActivityMapsBinding
import com.example.arborparker.network.RetrofitClient
import com.example.arborparker.network.SpotApi
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.data.geojson.GeoJsonLayer


private const val TAG = "MyLogTag"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    private val AUTOCOMPLETE_REQUEST_CODE = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        // sets up the autocomplete places search
        // Initialize the SDK with the Google Maps Platform API key
        Places.initialize(this, BuildConfig.MAPS_API_KEY)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })

    }
    
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Ann Arbor and move the camera
        val arbor = LatLng(42.279594, -83.732124)
        val zoomLevel = 12.0f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arbor, zoomLevel))
        getCurrentMapSpots()
    }

    fun getCurrentMapSpots() {
        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getSpots()
        viewModel.spotList.observe(this, Observer {
            val spotHash = it.map{it.id to it.isOpen}.toMap()
            val context: Context = applicationContext
            val layer = GeoJsonLayer(mMap, R.raw.parkingmap, context)
            val closedSpots = layer.features.filter { feature ->
                val spotId = feature.getProperty("SpotId").toInt()
                !(spotHash[spotId] ?: false)
            }
            for (closedSpot in closedSpots) {
                layer.removeFeature(closedSpot)
            }
            layer.addLayerToMap()
        })
    }


}