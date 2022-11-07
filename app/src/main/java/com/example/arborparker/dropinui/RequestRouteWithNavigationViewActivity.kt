package com.example.arborparker.dropinui

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.example.arborparker.MapsActivity
import com.example.arborparker.dropinui.CustomActionButtonsActivity
import com.example.arborparker.R
import com.example.arborparker.ViewProfileActivity
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
import com.mapbox.navigation.base.trip.model.RouteLegProgress
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.arrival.ArrivalObserver
import com.mapbox.navigation.dropin.actionbutton.ActionButtonDescription
import com.mapbox.navigation.dropin.infopanel.InfoPanelBinder
import com.mapbox.navigation.dropin.navigationview.NavigationViewListener
import com.mapbox.navigation.ui.base.view.MapboxExtendableButton
import com.mapbox.navigation.utils.internal.ifNonNull

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
        }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.navigationView.removeListener(navigationStateListener)
        binding.navigationView.unregisterMapObserver(mapViewObserver)
        MapboxNavigationApp.current()?.unregisterLocationObserver(locationObserver)
        MapboxNavigationApp.current()?.unregisterArrivalObserver(arrivalObserver)
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
                startActivity(Intent(this@RequestRouteWithNavigationViewActivity, MapsActivity::class.java))
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
    private fun requestRoutes(origin: Point, destination: Point) {
        MapboxNavigationApp.current()!!.requestRoutes(
            routeOptions = RouteOptions
                .builder()
                .applyDefaultNavigationOptions()
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

