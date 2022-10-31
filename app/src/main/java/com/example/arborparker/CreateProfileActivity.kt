package com.example.arborparker

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.arborparker.network.User
import timber.log.Timber


class CreateProfileActivity : AppCompatActivity() {

    //lateinit var etUserName: EditText
    //lateinit var etPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        //connects to buttons
        var btn_create = findViewById(R.id.btn_create) as Button
        var btn_backtologin = findViewById(R.id.btn_backtologin) as Button

        btn_create.setOnClickListener {
            //connects to text blocks
            var get_user_name = findViewById(R.id.et_user_name) as EditText
            var get_password = findViewById(R.id.et_password) as EditText

            // gets text from input
            val usernametxt = get_user_name.text.toString()
            val passwordtxt = get_password.text.toString()

            // error handling for empty text boxes
            if (usernametxt.isEmpty()) {
            get_user_name.error = "username is required"
            // get_user_name.error.requestFocus()
            return@setOnClickListener
            }

            if (passwordtxt.isEmpty()) {
                get_password.error = "password is required"
                // get_user_name.error.requestFocus()
                return@setOnClickListener
            }

            val userInfo = User(//id = null,
                                username = usernametxt,
                                password = passwordtxt)

            // adds user
            val apiNetwork = MainActivityViewModel()
            apiNetwork.addUser(userInfo) {
                if (it != null) {
                    // it = newly added user parsed as response
                    // it?.id = newly added user ID

                    // takes user to map screen
                    startActivity(Intent(this, MapsActivity::class.java))

                } else {
                    Timber.d("Error registering new user")

                    // error handling for when user already exists
                    // still fixing errors

                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Username Taken")
                    alertDialogBuilder.setMessage("That username is already taken. Please choose another.")
                    alertDialogBuilder.setCancelable(true)

                    alertDialogBuilder.setPositiveButton(android.R.string.yes) { _,_ ->
                        Toast.makeText(applicationContext,
                            android.R.string.yes, Toast.LENGTH_SHORT).show()
                    }

                    val alert1: AlertDialog = alertDialogBuilder.create()
                    alert1.show()

                }

            }

        }

        btn_backtologin.setOnClickListener {

            // takes user back to login screen
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}