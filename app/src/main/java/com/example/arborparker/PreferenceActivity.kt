package com.example.arborparker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class PreferenceActivity : AppCompatActivity() {

    // on below line we are creating
    // a variable for our button
    lateinit var settingsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        var btn_map = findViewById(R.id.btn_map) as Button
        var toggle_stairs = findViewById(R.id.switchstairs) as Switch
        var toggle_theme = findViewById(R.id.switchtheme) as Switch

        btn_map.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java));
        }

        toggle_stairs.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java));
        }


        // set the switch to listen on checked change
        toggle_theme.setOnCheckedChangeListener { _, isChecked ->

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
}
