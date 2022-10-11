package com.example.arborparker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class PreferenceActivity : AppCompatActivity() {

    // on below line we are creating
    // a variable for our button
    lateinit var settingsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        var btn_map = findViewById(R.id.btn_map) as Button

        btn_map.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java));
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
