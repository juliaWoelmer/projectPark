package com.example.arborparker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.arborparker.R

class ViewProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)
        var btn_edit = findViewById(R.id.btn_edit) as Button

        btn_edit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java));
        }

        var btn_map = findViewById(R.id.btn_map) as Button

        btn_map.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java));
        }
    }
}