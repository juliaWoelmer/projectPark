package com.example.arborparker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.arborparker.MainActivityViewModel.Companion.user_id


class ViewProfileActivity : AppCompatActivity() {

    // stores the users id
    var id: Int = user_id

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG", "ViewProfileActivity onCreate fxn runs")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        var btn_edit = findViewById(R.id.btn_edit) as Button

        btn_edit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java));
        }

        var btn_logout = findViewById(R.id.btn_logout) as Button

        btn_logout.setOnClickListener {
            startActivity(Intent(this, ViewProfileActivity::class.java));
        }

        val apiNetwork = MainActivityViewModel()
        apiNetwork.getUserInfoById(id) {
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