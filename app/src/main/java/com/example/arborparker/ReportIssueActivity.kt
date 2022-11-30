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

        var btn_update = findViewById(R.id.btn_update) as Button

        btn_update.setOnClickListener {
            if(validateInput()){
                startActivity(Intent(this, MapsActivity::class.java));
            }
        }
    }


    fun viewInitializations() {
        etIssue = findViewById(R.id.et_issue)
        // To show back button in actionbar
        // supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Checking if the input in form is valid
    fun validateInput(): Boolean {
        if (etIssue.text.toString().equals("")) {
            etIssue.setError("Please enter the issue")
            return false
        }
        return true
    }
}