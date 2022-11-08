package com.example.arborparker

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.arborparker.dropinui.AddActionButtonsActivity
import com.example.arborparker.dropinui.CustomActionButtonsActivity
import com.example.arborparker.dropinui.CustomInfoPanelActiveGuidanceActivity
import com.example.arborparker.dropinui.CustomInfoPanelActivity
import com.example.arborparker.dropinui.CustomInfoPanelAttributesActivity
import com.example.arborparker.dropinui.CustomLongClickActivity
import com.example.arborparker.dropinui.CustomNavigationViewOptionsActivity
import com.example.arborparker.dropinui.CustomRuntimeStylingActivity
import com.example.arborparker.dropinui.CustomSpeedLimitActivity
import com.example.arborparker.dropinui.CustomTripProgressActivity
import com.example.arborparker.dropinui.CustomViewInjectionActivity
import com.example.arborparker.dropinui.HideViewsInFreeDriveActivity
import com.example.arborparker.dropinui.NavigationViewActivity
import com.example.arborparker.dropinui.RepositionSpeedLimitActivity
import com.example.arborparker.dropinui.RequestRouteWithNavigationViewActivity
import com.example.arborparker.dropinui.ToggleThemeActivity

fun Context.examplesList() = listOf(
    MapboxExample(
        ContextCompat.getDrawable(this, R.drawable.mapbox_screenshot_navigation_view),
        getString(R.string.title_navigation_view),
        getString(R.string.description_navigation_view),
        NavigationViewActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(this, R.drawable.mapbox_screenshot_runtime_styling),
        getString(R.string.title_custom_runtime_styling),
        getString(R.string.description_custom_runtime_styling),
        CustomRuntimeStylingActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_navigation_view_options
        ),
        getString(R.string.title_customize_navigation_view_options),
        getString(R.string.description_customize_navigation_view_options),
        CustomNavigationViewOptionsActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(this, R.drawable.mapbox_screenshot_custom_speed_limit),
        getString(R.string.title_customize_speed_limit),
        getString(R.string.description_customize_speed_limit),
        CustomSpeedLimitActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(this, R.drawable.mapbox_screenshot_custom_trip_progress),
        getString(R.string.title_customize_trip_progress),
        getString(R.string.description_customize_trip_progress),
        CustomTripProgressActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(this, R.drawable.mapbox_screenshot_custom_view_injection),
        getString(R.string.title_custom_view_injection),
        getString(R.string.description_custom_view_injection),
        CustomViewInjectionActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(this, R.drawable.mapbox_screenshot_toggle_theme),
        getString(R.string.title_toggle_theme),
        getString(R.string.description_toggle_theme),
        ToggleThemeActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(this, R.drawable.mapbox_screenshot_custom_info_panel),
        getString(R.string.title_customize_info_panel),
        getString(R.string.description_customize_info_panel),
        CustomInfoPanelActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_custom_infopanel_active_guidance
        ),
        getString(R.string.title_customize_info_panel_active_guidance),
        getString(R.string.description_customize_info_panel_active_guidance),
        CustomInfoPanelActiveGuidanceActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_custom_info_panel_style
        ),
        getString(R.string.title_customize_info_panel_attributes),
        getString(R.string.description_customize_info_panel_attributes),
        CustomInfoPanelAttributesActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_request_routes
        ),
        getString(R.string.title_request_route_outside_navigation_view),
        getString(R.string.description_request_route_outside_navigation_view),
        RequestRouteWithNavigationViewActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_reposition_speed_limit
        ),
        getString(R.string.title_position_speed_limit_bottom),
        getString(R.string.description_position_speed_limit_bottom),
        RepositionSpeedLimitActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_custom_map_long_click
        ),
        getString(R.string.title_long_click_on_map),
        getString(R.string.description_long_click_on_map),
        CustomLongClickActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_hide_views_in_free_drive
        ),
        getString(R.string.title_hide_views_in_free_drive),
        getString(R.string.description_hide_views_in_free_drive),
        HideViewsInFreeDriveActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_custom_action_buttons
        ),
        getString(R.string.title_customize_action_buttons),
        getString(R.string.description_customize_action_buttons),
        CustomActionButtonsActivity::class.java
    ),
    MapboxExample(
        ContextCompat.getDrawable(
            this,
            R.drawable.mapbox_screenshot_add_action_buttons
        ),
        getString(R.string.title_add_action_buttons),
        getString(R.string.description_add_action_buttons),
        AddActionButtonsActivity::class.java
    ),
)
