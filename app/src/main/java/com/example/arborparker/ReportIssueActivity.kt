package com.example.arborparker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText


class ReportIssueActivity : AppCompatActivity() {
    lateinit var etIssue: EditText

    // stores the users id
    var id: Int = MainActivityViewModel.user_id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_issue)
        viewInitializations()

        var btn_upload = findViewById(R.id.btn_upload) as Button
    }


    fun viewInitializations() {
        etIssue = findViewById(R.id.et_issue)
    }

    fun performReportIssue() {
        startActivity(Intent(this, MapsActivity::class.java));
    }
}