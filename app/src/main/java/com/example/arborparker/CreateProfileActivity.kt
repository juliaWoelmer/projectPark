package com.example.arborparker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class CreateProfileActivity : AppCompatActivity() {

    lateinit var etUserName: EditText
    lateinit var etPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        var btn_create = findViewById(R.id.btn_create) as Button
        var btn_backtologin = findViewById(R.id.btn_backtologin) as Button

        btn_create.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java));
        }

        btn_backtologin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java));
        }
    }

}