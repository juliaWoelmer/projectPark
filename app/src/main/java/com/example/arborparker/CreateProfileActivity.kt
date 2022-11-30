package com.example.arborparker

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.arborparker.MainActivityViewModel.Companion.user_id
import com.example.arborparker.network.User
import timber.log.Timber


class CreateProfileActivity : AppCompatActivity() {


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

            val userInfo = User(
                username = usernametxt,
                password = passwordtxt
            )

            // adds user
            val apiNetwork = MainActivityViewModel()
            apiNetwork.getUserInfoByUsername(userInfo.username) {
                if (it != null && it.isEmpty()) {


                    apiNetwork.addUser(userInfo) {
                        if (it != null) {
                            // it = newly added user parsed as response
                            // it?.id = newly added user ID

                            //set public variable
                            user_id = it.id

                            Log.d("DEBUG", "Create user id " + user_id)

                            val alertDialogBuilder = AlertDialog.Builder(this)
                            alertDialogBuilder.setTitle("Welcome")
                                .setMessage("Welcome " + userInfo.username)
                                .setCancelable(true)
                                .setPositiveButton("Ok",
                                    DialogInterface.OnClickListener { dialog, id ->
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
                        }
                    }
                } else {
                    Log.d("DEBUG","Error registering new user")

                    // error handling for when user already exists
                    // still fixing errors

                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Username Taken")
                        .setMessage("That username is already taken. Please choose another.")
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
                    mLayoutParams.height = (mDisplayHeight * 0.25f).toInt()
                    alert1.window?.attributes = mLayoutParams

                }

            }
        }

        btn_backtologin.setOnClickListener {

            // takes user back to login screen
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}