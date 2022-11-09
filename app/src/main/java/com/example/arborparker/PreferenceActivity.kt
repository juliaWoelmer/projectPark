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
    var id: Int = MainActivityViewModel.user_id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        var btn_map = findViewById(R.id.btn_map) as Button
        var toggle_stairs = findViewById(R.id.switchstairs) as Switch
        var toggle_theme = findViewById(R.id.switchtheme) as Switch

        val apiNetwork = MainActivityViewModel()
        var allowStairs = 0
        var colorTheme = "Day"
        apiNetwork.getUserInfoById(id) {
            Log.d("DEBUG", "Get user id function runs" + id)
            if (it != null && it.isNotEmpty()) {
                allowStairs = it[0].allowStairs ?: 0
                colorTheme = it[0].colorTheme ?: "Day"
                Log.d("DEBUG", "allowStairs: " + allowStairs)
                Log.d("DEBUG", "colorTheme: " + colorTheme)


                // sets darkmode switch on if selected already
                if (isDarkModeOn()) {
                    toggle_theme.text = "Disable dark mode"
                    toggle_theme.isChecked = true
                }

                toggle_stairs.isChecked = allowStairs != 0
                //toggle_theme.isChecked = colorTheme != "Day"
            } else {
                Log.d("DEBUG", "Error getting user information")
                // toggle_stairs.isChecked = false
                toggle_theme.isChecked = false
            }
        }

        // sets the switches to the correct position from database at the start

        //toggle_stairs.isChecked = allowStairs != 0
        //toggle_theme.isChecked = colorTheme != "Day"
        //Log.d("DEBUG", "allowStairs: " + allowStairs)
        //Log.d("DEBUG", "colorTheme: " + colorTheme)



        /*

        if (allowStairs == 0) {
            toggle_stairs.isChecked = false
        }
        else {
            toggle_stairs.isChecked = true
        }

        if (colorTheme == "Day") {
            toggle_theme.isChecked = false
        }
        else {
            toggle_theme.isChecked = true
        }

         */

        btn_map.setOnClickListener {
            updateDatabase(toggle_stairs, toggle_theme, apiNetwork)
            startActivity(Intent(this, MapsActivity::class.java))
        }




        // set the switch to listen on checked change
        toggle_theme.setOnClickListener {

            // if the button is checked, i.e., towards the right or enabled
            // enable dark mode, change the text to disable dark mode
            // else keep the switch text to enable dark mode
            if (toggle_theme.isChecked) {
                colorTheme = "Night"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                toggle_theme.text = "Disable dark mode"
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                colorTheme = "Day"
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
