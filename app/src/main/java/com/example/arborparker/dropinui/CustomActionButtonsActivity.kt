package com.example.arborparker.dropinui

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.dropin.MapboxExtendableButtonParams
import com.example.arborparker.R
import com.example.arborparker.databinding.MapboxActivityCustomizeActionButtonsBinding

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class CustomActionButtonsActivity : AppCompatActivity() {

    private lateinit var binding: MapboxActivityCustomizeActionButtonsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapboxActivityCustomizeActionButtonsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navigationView.api.routeReplayEnabled(true)

        binding.navigationView.customizeViewStyles {
            audioGuidanceButtonParams = MapboxExtendableButtonParams(
                com.mapbox.navigation.ui.app.R.style.MapboxStyleAudioGuidanceButton_Circle,
                defaultLayoutParams().apply {
                    topMargin = 10
                    marginStart = 10
                }
            )
            cameraModeButtonParams = MapboxExtendableButtonParams(
                com.mapbox.navigation.dropin.R.style.MapboxStyleCameraModeButton_Circle,
                defaultLayoutParams().apply {
                    topMargin = 10
                    marginStart = 10
                }
            )
            recenterButtonParams = MapboxExtendableButtonParams(
                com.mapbox.navigation.dropin.R.style.MapboxStyleRecenterButton_Circle,
                defaultLayoutParams().apply {
                    topMargin = 10
                    marginStart = 10
                }
            )
            startNavigationButtonParams = MapboxExtendableButtonParams(
                R.style.MyCustomStartNavigationButtonCircular,
                defaultLayoutParams().apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            )
            endNavigationButtonParams = MapboxExtendableButtonParams(
                R.style.MyCustomEndNavigationButtonCircular,
                defaultLayoutParams().apply {
                    marginEnd = 40
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            )
            routePreviewButtonParams = MapboxExtendableButtonParams(
                R.style.MyCustomRoutePreviewButtonCircular,
                defaultLayoutParams().apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            )
        }
    }

    private fun defaultLayoutParams() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}
