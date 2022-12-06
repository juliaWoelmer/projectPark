package com.example.arborparker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.arborparker.MainActivityViewModel.Companion.user_id


class ViewProfileActivity : AppCompatActivity() {

    // stores the users id
    var id: Int = user_id!!

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "ViewProfileActivity onCreate fxn runs")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        var btn_edit = findViewById(R.id.btn_edit) as Button

        btn_edit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java));
        }

        var btn_back = findViewById(R.id.btn_back) as Button

        btn_back.setOnClickListener {
            finish()
            //startActivity(Intent(this, MapsActivity::class.java));
        }

        var btn_logout = findViewById(R.id.btn_logout) as Button

        btn_logout.setOnClickListener {

            // reset the app to day mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            // set user id to null so its no longer connected to database
            user_id = null

            startActivity(Intent(this, LoginActivity::class.java));
        }

        val apiNetwork = MainActivityViewModel()
        apiNetwork.getUserInfoById(id!!) {
            Log.d("DEBUG", "Testing public user id " + id)
            val etFirstName: TextView = findViewById<TextView>(R.id.et_first_name)
            val etLastName: TextView = findViewById<TextView>(R.id.et_last_name)
            val etEmail: TextView  = findViewById<TextView>(R.id.et_email)
            if (it != null && it.isNotEmpty()) {
                val firstName = it.first().firstName ?: ""
                val lastName = it.first().lastName ?: ""
                val email = it.first().email ?: ""

                etFirstName.text = firstName
                etLastName.text = lastName
                etEmail.text = email
            } else {
                Log.d("DEBUG", "Error getting user information")
                etFirstName.text = "Not Available"
                etLastName.text = "Not Available"
                etEmail.text = "Not Available"
            }
        }
    }
}