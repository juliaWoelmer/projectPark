package com.example.arborparker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import timber.log.Timber


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // get reference to all views
        var et_user_name = findViewById(R.id.et_user_name) as EditText
        var et_password = findViewById(R.id.et_password) as EditText
        var btn_submit = findViewById(R.id.btn_submit) as Button
        var btn_create = findViewById(R.id.btn_create) as Button

        // set on-click listener
        btn_submit.setOnClickListener {
            //val user_name = et_user_name.text;
            //val password = et_password.text;
            //Toast.makeText(this@LoginActivity, user_name, Toast.LENGTH_LONG).show()

            // your code to validate the user_name and password combination
            // and verify the same

            //connects to text blocks
            var get_user_name = findViewById(R.id.et_user_name) as EditText
            var get_password = findViewById(R.id.et_password) as EditText

            // gets text from input
            val usernametxt = get_user_name.text.toString()
            val passwordtxt = get_password.text.toString()

            // error handling for empty text boxes
            if (usernametxt.isEmpty()) {
                get_user_name.error = "username is required"
                //get_user_name.error.requestFocus()
                return@setOnClickListener
            }

            if (passwordtxt.isEmpty()) {
                get_password.error = "password is required"
                //get_user_name.error.requestFocus()
                return@setOnClickListener
            }


            Timber.d("Testing")
            Log.d("DEBUG", "Testing")
            // gets user
            val apiNetwork = MainActivityViewModel()
            Log.d("DEBUG", "Username " + usernametxt)
            apiNetwork.getUserId(usernametxt) {
                Timber.d("UserID " + it)
                Log.d("DEBUG", "UserId " + it)

                // makes sure there are no errors
                if (it != null) {

                    // makes sure the username exists
                    // makes sure the password entered is the same password stored
                    if (it.isNotEmpty() && it[0].password == passwordtxt) {
                        // it = newly added user parsed as response
                        // it[x]?.id = newly added user ID

                        val alertDialogBuilder = AlertDialog.Builder(this)
                        alertDialogBuilder.setTitle("Welcome")
                        alertDialogBuilder.setMessage("Welcome " + it[0].username)
                        alertDialogBuilder.setCancelable(true)

                        alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
                            Toast.makeText(
                                applicationContext,
                                android.R.string.ok, Toast.LENGTH_SHORT
                            ).show()
                        }

                        val alert1: AlertDialog = alertDialogBuilder.create()
                        alert1.show()

                        // takes user to map screen
                        startActivity(Intent(this, MapsActivity::class.java))
                    } else {
                        Log.d("DEBUG", "Error getting user information")

                        // error handling for when user doesnt exist

                        val alertDialogBuilder = AlertDialog.Builder(this)
                        alertDialogBuilder.setTitle("Error")
                        alertDialogBuilder.setMessage("User does not exist. Please check the username and password are correct or create an account.")
                        alertDialogBuilder.setCancelable(true)

                        alertDialogBuilder.setPositiveButton(android.R.string.ok) { _, _ ->
                            Toast.makeText(
                                applicationContext,
                                android.R.string.ok, Toast.LENGTH_SHORT
                            ).show()
                        }

                        val alert1: AlertDialog = alertDialogBuilder.create()
                        alert1.show()
                    }
                }
            }
        }

        // takes user to create profile page if they click create account
        btn_create.setOnClickListener {
            //val user_name = et_user_name.text;
            //val password = et_password.text;
            //Toast.makeText(this@LoginActivity, user_name, Toast.LENGTH_LONG).show()

            // your code to validate the user_name and password combination
            // and verify the same

            startActivity(Intent(this, CreateProfileActivity::class.java));
        }
    }
}