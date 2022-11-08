package com.example.arborparker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.arborparker.network.UserPreferencesInfo

class PreferenceActivity : AppCompatActivity() {

    // on below line we are creating
    // a variable for our button
    lateinit var settingsBtn: Button

    // stores the users id
    var id: Int = MainActivityViewModel.user_id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        var btn_map = findViewById(R.id.btn_map) as Button
        var toggle_stairs = findViewById(R.id.switchstairs) as Switch
        var toggle_theme = findViewById(R.id.switchtheme) as Switch

        val apiNetwork = MainActivityViewModel()
        apiNetwork.getUserInfoById(id) {
            Log.d("DEBUG", "Get user id function runs" + id)
            var allowStairs = 0
            var colorTheme = "Day"
            if (it != null && it.isNotEmpty()) {
                allowStairs = it.first().allowStairs ?: 0
                colorTheme = it.first().colorTheme ?: "Day"
                Log.d("DEBUG", "allowStairs: " + allowStairs)
                Log.d("DEBUG", "colorTheme: " + colorTheme)
                toggle_stairs.isChecked = allowStairs != 0
                toggle_theme.isChecked = colorTheme != "Day"
            } else {
                Log.d("DEBUG", "Error getting user information")
                toggle_stairs.isChecked = false
                toggle_theme.isChecked = false
            }
        }

        btn_map.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java));
        }

        toggle_stairs.setOnClickListener {
            updateDatabase(toggle_stairs, toggle_theme, apiNetwork)
            startActivity(Intent(this, MapsActivity::class.java));
        }

        // set the switch to listen on checked change
        toggle_theme.setOnClickListener {
            updateDatabase(toggle_stairs, toggle_theme, apiNetwork)
            // if the button is checked, i.e., towards the right or enabled
            // enable dark mode, change the text to disable dark mode
            // else keep the switch text to enable dark mode
            if (toggle_theme.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                toggle_theme.text = "Disable dark mode"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                toggle_theme.text = "Enable dark mode"
            }
        }
    }

    private fun updateDatabase(toggle_stairs: Switch, toggle_theme: Switch, apiNetwork: MainActivityViewModel) {
        var allowStairs = 0
        var colorTheme = "Day"
        if (toggle_stairs.isChecked) {
            allowStairs = 1
        }
        if (toggle_theme.isChecked) {
            colorTheme = "Night"
        }
        Log.d("DEBUG", "Going to try setting preferences to following")
        Log.d("DEBUG", "allowStairs: " + allowStairs)
        Log.d("DEBUG", "colorTheme: " + colorTheme)
        var userPreferencesInfo = UserPreferencesInfo(id, allowStairs, colorTheme)
        apiNetwork.editUserPreferences(id, userPreferencesInfo) {
            if (it != null) {
                Log.d("DEBUG", "Success editing user preferences")
                Log.d("DEBUG", "Rows affected: " + it.rowsAffected)
            } else {
                Log.d("DEBUG", "Error editing user preferences")
            }
        }
    }
}
