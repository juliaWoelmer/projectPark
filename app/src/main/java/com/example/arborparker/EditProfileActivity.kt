package com.example.arborparker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class EditProfileActivity : AppCompatActivity() {

    lateinit var etFirstName: EditText
    lateinit var etLastName:EditText
    lateinit var etEmail: EditText
    lateinit var etContactNo:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        viewInitializations()

        var btn_update = findViewById(R.id.btn_update) as Button

        btn_update.setOnClickListener {
            startActivity(Intent(this, ViewProfileActivity::class.java));
        }
    }


    fun viewInitializations() {

        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etEmail  = findViewById(R.id.et_email)


        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    // Checking if the input in form is valid
    fun validateInput(): Boolean {
        if (etFirstName.text.toString().equals("")) {
            etFirstName.setError("Please Enter First Name")
            return false
        }
        if (etLastName.text.toString().equals("")) {
            etLastName.setError("Please Enter Last Name")
            return false
        }
        if (etEmail.text.toString().equals("")) {
            etEmail.setError("Please Enter Email")
            return false
        }
        // checking the proper email format
        if (!isEmailValid(etEmail.text.toString())) {
            etEmail.setError("Please Enter Valid Email")
            return false
        }

        return true
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Hook Click Event

    fun performEditProfile (view: View) {
        if (validateInput()) {

            // Input is valid, here send data to your server

            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val contactNo = etContactNo.text.toString()

            Toast.makeText(this,"Profile Update Successfully",Toast.LENGTH_SHORT).show()
            // Here you can call you API

        }
    }

}