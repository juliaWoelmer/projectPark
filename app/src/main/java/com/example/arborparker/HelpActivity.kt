package com.example.arborparker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button


class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        var btn_ok = findViewById(com.example.arborparker.R.id.btn_ok) as Button

        btn_ok.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java));
        }
    }
}