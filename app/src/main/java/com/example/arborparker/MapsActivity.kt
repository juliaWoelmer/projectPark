package com.example.arborparker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.arborparker.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import com.google.maps.android.SphericalUtil
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.data.geojson.GeoJsonLayer
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import android.content.Intent
import android.widget.Button
import com.example.arborparker.dropinui.NavigationViewActivity
import com.example.arborparker.dropinui.RequestRouteWithNavigationViewActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mapbox.geojson.Point


private const val TAG = "MyLogTag"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var col: MutableSet<MyItem>
    private lateinit var lines: Pair<Polyline, Polyline>
    private lateinit var destination: Place
    companion object {
        var DestPoint = Point.fromLngLat(0.0, 0.0) as Point
        var SpotPoint = Point.fromLngLat(0.0, 0.0) as Point
    }
    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e.,
    // how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient
    // has a new Location
    private lateinit var locationCallback: LocationCallback

    // This will store current location info
    //private var currentLocation: LatLng = LatLng(42.279594, -83.732124)
    private var userLocation: LatLng = LatLng(42.279594, -83.732124)
    // Initializing other items
    // from layout file
    var latitudeTextView: TextView? = null
    var longitTextView: TextView? = null
    // Initializing other items
    // from layout file

    var PERMISSION_ID = 44

    private val AUTOCOMPLETE_REQUEST_CODE = 1

    inner class MyItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String
    ) : ClusterItem {

        private val position: LatLng
        private val title: String
        private val snippet: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            return title
        }

        override fun getSnippet(): String? {
            return snippet
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
        }
    }

    // geo loc
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.lastLocation.addOnCompleteListener{ task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        userLocation = LatLng(location.getLatitude(),location.getLongitude())
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Looper.myLooper()?.let {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                it
            )
        }
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: android.location.Location = locationResult.lastLocation
            userLocation = LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())

        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation()
        }
    }

    // Generate url to request directions
    private fun getDirectionURL(origin:LatLng, dest:LatLng, mode: String, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=$mode" +
                "&key=$secret"
    }

    // Decode response to JSON
    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    fun shutdownAndAwaitTermination(pool: ExecutorService) {
        pool.shutdown() // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow() // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate")
            }
        } catch (ie: InterruptedException) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow()
            // Preserve interrupt status
            Thread.currentThread().interrupt()
        }
    }

    // Get Directions
    private fun getDirections(origin: LatLng, dest: LatLng, mode: String): ArrayList<List<LatLng>> {
        // Get Directions From API
        val url = getDirectionURL(origin, dest, mode, BuildConfig.MAPS_API_KEY)
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        //var resp: ArrayList<List<LatLng>>
        var result =  ArrayList<List<LatLng>>()
        executor.execute {
            //Background work here
            val data = URL(url).readText()
            try{
                val respObj = Gson().fromJson(data,MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            handler.post {}
        }
        shutdownAndAwaitTermination(executor)
        Log.i(TAG, "Spot: ${result}")
        return result
    }

    private fun drawPolyLine(nodes: ArrayList<List<LatLng>>, color: Int): Polyline {
        val lineoption = PolylineOptions()
        for (i in nodes.indices){
            lineoption.addAll(nodes[i])
            lineoption.width(10f)
            lineoption.color(color)
            lineoption.geodesic(true)
        }
        val line = mMap.addPolyline(lineoption)
        return line
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var btn_profile = findViewById(R.id.btn_profile) as Button
        btn_profile.setOnClickListener {
            startActivity(Intent(this, ViewProfileActivity::class.java))
        }

        var btn_settings = findViewById(R.id.btn_settings) as Button
        btn_settings.setOnClickListener {
            startActivity(Intent(this, PreferenceActivity::class.java))
        }

        var btn_nav = findViewById(R.id.btn_nav) as Button
        btn_nav.setOnClickListener {
            startActivity(Intent(this, RequestRouteWithNavigationViewActivity::class.java))
        }

        // sets up the autocomplete places search
        // Initialize the SDK with the Google Maps Platform API key
        Places.initialize(this, BuildConfig.MAPS_API_KEY)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}, ${place.latLng}")
                destination = place
                DestPoint = Point.fromLngLat(place.latLng.longitude, place.latLng.latitude) as Point
                var min: Double
                var spot: MyItem
                min = SphericalUtil.computeDistanceBetween(place.latLng, col.elementAt(0).getPosition())
                spot = col.elementAt(0)
                for (item in col) {
                    val distance = SphericalUtil.computeDistanceBetween(place.latLng, item.getPosition())
                    if (distance < min) {
                        min = distance
                        spot = item
                    }
                }
                lines.first.remove()
                lines.second.remove()
                SpotPoint = Point.fromLngLat(spot.position.longitude, spot.position.latitude) as Point
                // Get Directions From API
                var res_driv = (getDirections(userLocation, spot.position, "driving"))
                //Thread.sleep(0.01.toLong())
                var res_walk = getDirections(destination.latLng, spot.position, "walking")
                lines = Pair(drawPolyLine(res_driv, Color.BLUE), drawPolyLine(res_walk, Color.RED))
                val builder = LatLngBounds.Builder()
                builder.include(userLocation)
                builder.include(destination.latLng)
                builder.include(spot.position)
                // Position Map
                val bounds = builder.build()
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 250))

                Log.i(TAG, "Place: ${place.name}, ${place.id}, ${place.latLng}")
                Log.i(TAG, "Spot: ${spot.title}, ${spot.position}")
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
        lines = Pair(mMap.addPolyline(PolylineOptions()), mMap.addPolyline(PolylineOptions()))
        lines.first.remove()
        lines.second.remove()
        // Add a marker in Ann Arbor and move the camera
        val arbor = LatLng(42.279594, -83.732124)
        val zoomLevel = 12.0f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arbor, zoomLevel))
        // Add GeoJson Layer containing parking spots
        setUpClusterer()
        filterMapSpotsAndDisplay()

        //allow zoom widget
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    fun filterMapSpotsAndDisplay() {
        val context: Context = applicationContext
        val layer = GeoJsonLayer(mMap, R.raw.parkingmap, context)
        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getSpots()
        viewModel.spotList.observe(this, Observer {
            val spotHash = it.map{it.id to it.isOpen}.toMap()
            val closedSpots = layer.features.filter { feature ->
                val spotId = feature.getProperty("SpotId").toInt()
                !(spotHash[spotId] ?: false)
            }
            for (closedSpot in closedSpots) {
                layer.removeFeature(closedSpot)
            }
            col = mutableSetOf<MyItem>()
            for (feature in layer.features) {
                val geo = feature.geometry.geometryObject.toString()
                val latlong = geo.substring(10).dropLast(1).split(",".toRegex()).toTypedArray()
                val lat = latlong[0].toDouble()
                val lng = latlong[1].toDouble()
                val id = feature.getProperty("SpotId")
                val offsetItem =
                    MyItem(lat, lng, id, "Snippet")
                //col.addItem(offsetItem)
                col.add(offsetItem)
            }
            clusterManager.addItems(col)
            val zoom = mMap.cameraPosition.zoom
            val latLng = mMap.cameraPosition.target
            val newZoom = zoom + 0.0001f
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,newZoom))
        })

    }


    // Declare a variable for the cluster manager.
    private lateinit var clusterManager: ClusterManager<MyItem>

    private fun setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        val context: Context = applicationContext
        clusterManager = ClusterManager(context, mMap)

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)

        // Add cluster items (markers) to the cluster manager.
        //addItems()
    }

}