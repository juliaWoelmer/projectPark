package com.example.arborparker

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.example.arborparker.MainActivityViewModel.Companion.user_id
import timber.log.Timber


class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // get reference to all views
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
            apiNetwork.getUserInfoByUsername(usernametxt) {
                Timber.d("UserID " + it)
                Log.d("DEBUG", "UserId " + it)

                // makes sure there are no errors
                if (it != null) {

                    // makes sure the username exists
                    // makes sure the password entered is the same password stored
                    if (it.isNotEmpty() && it[0].password == passwordtxt) {
                        // it = newly added user parsed as response
                        // it[x]?.id = newly added user ID

                        //set public variable
                        user_id = it[0].id

                        Log.d("DEBUG", "Create user id " + user_id)


                                val alertDialogBuilder = AlertDialog.Builder(this)
                        alertDialogBuilder.setTitle("Welcome")
                                    .setMessage("Welcome " + it[0].username)
                                    .setCancelable(true)
                                    .setPositiveButton("Ok",
                                        DialogInterface.OnClickListener { dialog, id ->


                                            // sets users theme preferences
                                            if (it[0].colorTheme == "Night") {
                                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                            } else {
                                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                            }

                                            // takes user to map screen
                                            startActivity(Intent(this, MapsActivity::class.java))

                                        })
                        // Create the AlertDialog object and return it
                        alertDialogBuilder.create()


                        val alert1: AlertDialog = alertDialogBuilder.create()
                        alert1.show()

                        // Get the current app screen width and height
                        val mDisplayMetrics = windowManager.currentWindowMetrics
                        val mDisplayWidth = mDisplayMetrics.bounds.width()
                        val mDisplayHeight = mDisplayMetrics.bounds.height()

                        // Generate custom width and height and
                        // add to the dialog attributes
                        // we multiplied the width and height by 0.5,
                        // meaning reducing the size to 50%
                        val mLayoutParams = WindowManager.LayoutParams()
                        mLayoutParams.width = (mDisplayWidth * 0.7f).toInt()
                        mLayoutParams.height = (mDisplayHeight * 0.25f).toInt()
                        alert1.window?.attributes = mLayoutParams


                    } else {
                        Log.d("DEBUG", "Error getting user information")

                        // error handling for when user doesnt exist

                        val alertDialogBuilder = AlertDialog.Builder(this)
                        alertDialogBuilder.setTitle("Error")
                            .setMessage("User does not exist. Please check the username and password are correct or create an account.")
                            .setCancelable(true)
                            .setPositiveButton("Ok",
                                DialogInterface.OnClickListener { dialog, id ->

                                })
                        // Create the AlertDialog object and return it
                        alertDialogBuilder.create()


                        val alert1: AlertDialog = alertDialogBuilder.create()
                        alert1.show()

                        // Get the current app screen width and height
                        val mDisplayMetrics = windowManager.currentWindowMetrics
                        val mDisplayWidth = mDisplayMetrics.bounds.width()
                        val mDisplayHeight = mDisplayMetrics.bounds.height()

                        // Generate custom width and height and
                        // add to the dialog attributes
                        // we multiplied the width and height by 0.5,
                        // meaning reducing the size to 50%
                        val mLayoutParams = WindowManager.LayoutParams()
                        mLayoutParams.width = (mDisplayWidth * 0.8f).toInt()
                        mLayoutParams.height = (mDisplayHeight * 0.3f).toInt()
                        alert1.window?.attributes = mLayoutParams
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