package com.example.arborparker

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.input.key.Key.Companion.D
import com.example.arborparker.network.UserPreferencesInfo

class PreferenceActivity : AppCompatActivity() {


    // checks if dark mode was activated
    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    // on below line we are creating
    // a variable for our button
    lateinit var settingsBtn: Button

    // stores the users id
    var id: Int = MainActivityViewModel.user_id!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        var btn_map = findViewById(R.id.btn_map) as Button
        var toggle_theme = findViewById(R.id.switchtheme) as Switch
        var toggle_vanAccessible = findViewById(R.id.switchvan) as Switch

        val apiNetwork = MainActivityViewModel()
        var colorTheme = "Day"
        var isVanAccessibleRequired = false
        apiNetwork.getUserInfoById(id) {
            Log.d("DEBUG", "Get user id function runs in preferences " + id)
            if (it != null && it.isNotEmpty()) {
                colorTheme = it[0].colorTheme ?: "Day"
                isVanAccessibleRequired = it[0].vanAccessible
                Log.d("DEBUG", "colorTheme: " + colorTheme)
                Log.d("DEBUG", "vanAccessible: " + isVanAccessibleRequired)


                // sets darkmode switch on if selected already
                if (isDarkModeOn()) {
                    toggle_theme.text = "Disable dark mode"
                    toggle_theme.isChecked = true
                }
                //toggle_theme.isChecked = colorTheme != "Day"
                toggle_vanAccessible.isChecked = isVanAccessibleRequired
            } else {
                Log.d("DEBUG", "Error getting user information")
                toggle_theme.isChecked = false
                toggle_vanAccessible.isChecked = false
            }
        }

        btn_map.setOnClickListener {
            updateDatabase(toggle_theme, toggle_vanAccessible, apiNetwork)
            //startActivity(Intent(this, MapsActivity::class.java))
            finish()
        }


        // set the switch to listen on checked change
        toggle_theme.setOnClickListener {
            // if the button is checked, i.e., towards the right or enabled
            // enable dark mode, change the text to disable dark mode
            // else keep the switch text to enable dark mode
            updateDatabase(toggle_theme, toggle_vanAccessible, apiNetwork)
            if (toggle_theme.isChecked) {
                colorTheme = "Night"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                toggle_theme.text = "Disable dark mode"
                Log.d("DEBUG", "Night Mode On")
            } else {
                colorTheme = "Day"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                toggle_theme.text = "Enable dark mode"
                Log.d("DEBUG", "Night Mode Off")
            }

        }

        toggle_vanAccessible.setOnClickListener {
            updateDatabase(toggle_theme, toggle_vanAccessible, apiNetwork)
            if (toggle_vanAccessible.isChecked) {
                isVanAccessibleRequired = true
                Log.d("DEBUG", "Van Access Required")
            }
            else {
                isVanAccessibleRequired = false
                Log.d("DEBUG", "Van Access Not Required")
            }
        }
    }

    private fun updateDatabase(toggle_theme: Switch, toggle_vanAccessible: Switch, apiNetwork: MainActivityViewModel) {
        var colorTheme = "Day"
        var isVanAccessibleRequired = false

        if (toggle_theme.isChecked) {
            colorTheme = "Night"
        }

        if (toggle_vanAccessible.isChecked) {
            isVanAccessibleRequired = true
        }

        Log.d("DEBUG", "Going to try setting preferences to following")
        Log.d("DEBUG", "colorTheme: " + colorTheme)
        var userPreferencesInfo = UserPreferencesInfo(id, colorTheme, isVanAccessibleRequired)
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
