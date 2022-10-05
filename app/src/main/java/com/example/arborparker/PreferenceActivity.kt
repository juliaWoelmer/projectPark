package com.example.arborparker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // on below line we are creating
    // a variable for our button
    lateinit var settingsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // on below line we are initializing
        // our views with their ids.
        settingsBtn = findViewById(R.id.idBtnSettings)

        // on below line we are adding click listener
        // for our settings button
        settingsBtn.setOnClickListener {
            // opening a new intent to open settings activity.
            // opening a new intent to open settings activity.

        }

        if (findViewById<View?>(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return
            }
            // below line is to inflate our fragment.
            fragmentManager.beginTransaction().add(R.id.idFrameLayout, SettingsFragment()).commit()
        }
    }
}
