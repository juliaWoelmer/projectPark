package com.example.arborparker.dropinui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.arborparker.*
import com.example.arborparker.dropinui.CustomActionButtonsActivity
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.dropin.NavigationView
import com.mapbox.navigation.dropin.map.MapViewObserver
import com.example.arborparker.databinding.MapboxActivityRequestRouteNavigationViewBinding
import com.example.arborparker.network.SpotWithUser
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.navigation.base.trip.model.RouteLegProgress
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.arrival.ArrivalObserver
import com.mapbox.navigation.dropin.actionbutton.ActionButtonDescription
import com.mapbox.navigation.dropin.infopanel.InfoPanelBinder
import com.mapbox.navigation.dropin.navigationview.NavigationViewListener
import com.mapbox.navigation.ui.base.view.MapboxExtendableButton
import com.mapbox.navigation.utils.internal.ifNonNull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * The example demonstrates how to use [MapboxNavigationApp] to request routes outside [NavigationView]
 * and transition [NavigationView] to active navigation state.
 *
 * Before running the example make sure you have put your access_token in the correct place
 * inside [app-preview/src/main/res/values/mapbox_access_token.xml]. If not present then add
 * this file at the location mentioned above and add the following content to it
 *
 * <?xml version="1.0" encoding="utf-8"?>
 * <resources xmlns:tools="http://schemas.android.com/tools">
 *     <string name="mapbox_access_token"><PUT_YOUR_ACCESS_TOKEN_HERE></string>
 * </resources>
 *
 * The example uses replay location engine to facilitate navigation without physically moving.
 *
 * How to use the example:
 * - Start the example
 * - Grant the location permissions if not already granted
 * - Long press anywhere on the map
 * - NavigationView should transition to active guidance
 */
@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class RequestRouteWithNavigationViewActivity : AppCompatActivity(), OnMapLongClickListener {

    private var lastLocation: Location? = null
    private lateinit var binding: MapboxActivityRequestRouteNavigationViewBinding
    // stores the users id
    var user_id: Int = MainActivityViewModel.user_id
    // stores the users id
    var spotId: Int = MapsActivity.SpotID.toInt()

    /**
     * Gets notified with location updates.
     *
     * Exposes raw updates coming directly from the location services
     * and the updates enhanced by the Navigation SDK (cleaned up and matched to the road).
     */
    private val locationObserver = object : LocationObserver {
        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            lastLocation = locationMatcherResult.enhancedLocation
        }

        override fun onNewRawLocation(rawLocation: Location) {
            // no impl
        }
    }

    /**
     * Notifies with attach and detach events on [MapView]
     */
    private val mapViewObserver = object : MapViewObserver() {
        override fun onAttached(mapView: MapView) {
            mapView.gestures.addOnMapLongClickListener(
                this@RequestRouteWithNavigationViewActivity
            )
        }

        override fun onDetached(mapView: MapView) {
            mapView.gestures.removeOnMapLongClickListener(
                this@RequestRouteWithNavigationViewActivity
            )
        }
    }


    /**
     * Gets notified with arrival updates.
     */
    private val arrivalObserver = object : ArrivalObserver {

        override fun onWaypointArrival(routeProgress: RouteProgress) {
            // do something when the user arrives at a waypoint
        }

        override fun onNextRouteLegStart(routeLegProgress: RouteLegProgress) {
            // do something when the user starts a new leg
        }

        override fun onFinalDestinationArrival(routeProgress: RouteProgress) {
            // do something when the user reaches the final destination
            val alert1: AlertDialog = showAlertParkingSpot() as AlertDialog
            alert1.show()
        }
    }
    /**
     * Gets notified with arrival updates.
     */
    private val arrivalObserver2 = object : ArrivalObserver {

        override fun onWaypointArrival(routeProgress: RouteProgress) {
            // do something when the user arrives at a waypoint
        }

        override fun onNextRouteLegStart(routeLegProgress: RouteLegProgress) {
            // do something when the user starts a new leg
        }

        override fun onFinalDestinationArrival(routeProgress: RouteProgress) {
            // do something when the user reaches the final destination
            val alert1: AlertDialog = showAlertDestinationArrival() as AlertDialog
            alert1.show()
        }
    }

    private fun showAlertParkingSpot(): Dialog {
        return this?.let {
            val builder = AlertDialog.Builder(this@RequestRouteWithNavigationViewActivity)
            builder.setTitle("You have arrived at the parking spot.")
            builder.setMessage("Are you able to park in the parking spot?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Set spot as occupied by user in database
                        val apiNetwork = MainActivityViewModel()
                        val currentTime = LocalDateTime.now()
                        val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val currentTimeFormatted = currentTime.format(timeFormatter).toString()
                        val spotWithUser = SpotWithUser(false, user_id, currentTimeFormatted)
                        apiNetwork.editSpotAvailability(spotId, spotWithUser) {
                            if (it != null) {
                                Log.d("DEBUG", "Success editing spot availability")
                                Log.d("DEBUG", "User_id " + user_id + " occupied spot " + spotId)
                                Log.d("DEBUG", "Rows affected: " + it.rowsAffected)
                            } else {
                                Log.d("DEBUG", "Error editing spot availability")
                            }
                        }
                        // proceed to show walking route
                        MapboxNavigationApp.current()?.unregisterArrivalObserver(arrivalObserver)
                        MapboxNavigationApp.current()?.registerArrivalObserver(arrivalObserver2)
                        requestRoutes(MapsActivity.SpotPoint, MapsActivity.DestPoint, DirectionsCriteria.PROFILE_WALKING)
                        //finish();
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        // show no dialog
                        val alert2: AlertDialog = showAlertParkingSpotNo() as AlertDialog
                        alert2.show()
                        //finish();
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    private fun showAlertParkingSpotNo(): Dialog {
        return this?.let {
            val builder = AlertDialog.Builder(this@RequestRouteWithNavigationViewActivity)
            builder.setTitle("Why not?")
            builder.setItems(arrayOf<String>("Spot is already taken", "Spot is obstructed", "Someone is illegally parked in the spot"),
                DialogInterface.OnClickListener { dialog, which ->
                    val apiNetwork = MainActivityViewModel()
                    var spotWithUser = SpotWithUser(false, null, null)
                    var alertAuthorities = false
                    val currentTime = LocalDateTime.now()
                    val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val currentTimeFormatted = currentTime.format(timeFormatter).toString()
                    if (which == 0){ //spot is already taken
                        // Mark spot as not open with current time as timeLastOccupied and userid as null
                        spotWithUser = SpotWithUser(false, null, currentTimeFormatted)
                    }
                    else if (which == 1){ //spot is obstructed
                        // Mark spot as not open with timeLastOccupied = null and userid as null
                        spotWithUser = SpotWithUser(false, null, null)
                        alertAuthorities = true
                    }
                    else{ //someone is illegally parked
                        // Mark spot as not open with current time as timeLastOccupied and userId as null
                        spotWithUser = SpotWithUser(false, null, currentTimeFormatted)
                        alertAuthorities = true
                    }
                    editIssueSpot(apiNetwork, spotWithUser)
                    val rerouteAlert: AlertDialog = showAlertReroute(alertAuthorities) as AlertDialog
                    rerouteAlert.show()
                })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun editIssueSpot(apiNetwork: MainActivityViewModel, spotWithUser: SpotWithUser) {
        apiNetwork.editSpotAvailability(spotId, spotWithUser) {
            if (it != null) {
                Log.d("DEBUG", "Success editing spot availability of issue spot")
                Log.d("DEBUG", "Occupied spot " + spotId)
                Log.d("DEBUG", "Rows affected: " + it.rowsAffected)
            } else {
                Log.d("DEBUG", "Error editing spot availability of issue spot")
            }
        }
    }

    private fun showAlertReroute(alertAuthorities: Boolean): Dialog {
        return this?.let {
            val builder = AlertDialog.Builder(this@RequestRouteWithNavigationViewActivity)
            if (alertAuthorities) {
                builder.setTitle("Thank you, the proper authorities have been notified")
            }
            builder.setMessage("You will be rerouted to the closest available spot")
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // reroute to new parking spot

                        //finish();
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showAlertDestinationArrival(): Dialog {
        return this?.let {
            val builder = AlertDialog.Builder(this@RequestRouteWithNavigationViewActivity)
            builder.setTitle("You have arrived at your destination")
                .setPositiveButton("Close",
                    DialogInterface.OnClickListener { dialog, id ->
                        // delete dialog
                        finish();
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    // Naviagtion State listener
    private val navigationStateListener = object : NavigationViewListener() {
        override fun onFreeDrive() {
            customizeInfoPanel(false)
        }

        override fun onDestinationPreview() {
            customizeInfoPanel(false)
        }

        override fun onRoutePreview() {
            customizeInfoPanel(false)
        }

        override fun onActiveNavigation() {
            customizeInfoPanel(false)
        }

        override fun onArrival() {
            customizeInfoPanel(false)
        }
    }
    private fun customizeInfoPanel(shouldCustomize: Boolean) {
        binding.navigationView.customizeViewBinders {
            infoPanelBinder = if (shouldCustomize) {
                CustomInfoPanelBinder()
            } else {
                InfoPanelBinder.defaultBinder()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapboxActivityRequestRouteNavigationViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set to false if you want to handle map long click listener in the app
        binding.navigationView.customizeViewOptions {
            enableMapLongClickIntercept = false
        }
        binding.navigationView.customizeViewBinders {
            customActionButtons = listOf(
                ActionButtonDescription(
                    customActionButton(R.drawable.mapbox_ic_settings),
                    ActionButtonDescription.Position.START
                )
            )
        }
        binding.navigationView.addListener(navigationStateListener)
        binding.navigationView.registerMapObserver(mapViewObserver)
        MapboxNavigationApp.current()?.registerLocationObserver(locationObserver)
        MapboxNavigationApp.current()?.registerArrivalObserver(arrivalObserver)
        requestRoutes(MapsActivity.UserPoint, MapsActivity.SpotPoint)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.navigationView.removeListener(navigationStateListener)
        binding.navigationView.unregisterMapObserver(mapViewObserver)
        MapboxNavigationApp.current()?.unregisterLocationObserver(locationObserver)
        MapboxNavigationApp.current()?.unregisterArrivalObserver(arrivalObserver)
        MapboxNavigationApp.current()?.unregisterArrivalObserver(arrivalObserver2)
    }

    // Action button
    private fun customActionButton(@DrawableRes image: Int): View {
        // The example adds `MapboxExtendableButton`, but you can inflate any custom view you want
        return MapboxExtendableButton(context = this).apply {
            this.setState(
                MapboxExtendableButton.State(image)
            )
            containerView.setPadding(10.dp, 13.dp, 10.dp, 13.dp)
            setOnClickListener {
                //startActivity(Intent(this@RequestRouteWithNavigationViewActivity, MapsActivity::class.java))
                finish()
            }
        }
    }
    private val Number.dp get() = com.mapbox.android.gestures.Utils.dpToPx(toFloat()).toInt()

    // Nav
    override fun onMapLongClick(point: Point): Boolean {
        ifNonNull(lastLocation) {
            requestRoutes(Point.fromLngLat(it.longitude, it.latitude), MapsActivity.SpotPoint)
        }
        return false
    }

    // Nav
    private fun requestRoutes(origin: Point, destination: Point, nav_mode: String = DirectionsCriteria.PROFILE_DRIVING_TRAFFIC) {
        MapboxNavigationApp.current()!!.requestRoutes(
            routeOptions = RouteOptions
                .builder()
                .applyDefaultNavigationOptions(nav_mode)
                .applyLanguageAndVoiceUnitOptions(this)
                .coordinatesList(listOf(origin, destination))
                .alternatives(true)
                .build(),
            callback = object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                    // no impl
                }

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
                    // no impl
                }

                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    binding.navigationView.api.routeReplayEnabled(true)
                    binding.navigationView.api.startActiveGuidance(routes)
                }
            }
        )
    }

}

