package com.example.arborparker

import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arborparker.network.*
import com.example.arborparker.network.RetrofitClient.retrofit
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    val spotList: MutableLiveData<List<Spot>> = MutableLiveData()

    fun getSpots() {
        viewModelScope.launch {
            spotList.value = RetrofitClient.retrofit.getSpots()
        }
    }

    // adds user to database
    fun addUser(userData: User, onResult: (User?) -> Unit){
        retrofit.addUser(userData).enqueue(
            object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<User>, response: Response<User>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }

    // gets user info by id
    fun getUserInfoById(id: Int, onResult: (List<UserInfo>?) -> Unit) {
        Log.d("DEBUG", "getUserInfoById function called")
        retrofit.getUserInfoById(id).enqueue(
            object : Callback<List<UserInfo>> {
                override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                    Log.d("DEBUG", "Failed")
                    onResult(null)
                }
                override fun onResponse( call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                    Log.d("DEBUG", "Success")
                    Log.d("DEBUG", "id " + id)
                    Log.d("DEBUG", "Response raw " + response.raw())
                    Log.d("DEBUG", "Response error " + response.errorBody())
                    Log.d("DEBUG", "Response success " + response.isSuccessful)
                    Log.d("DEBUG", "Response body " + response.body())
                    val userInfo = response.body()
                    Log.d("DEBUG", "url " + response.raw().request().url())
                    Log.d("DEBUG", "Response " + userInfo)
                    onResult(userInfo)
                }
            }
        )
    }

    // gets user id by username
    fun getUserId(username: String, onResult: (List<UserInfo>?) -> Unit) {
        Log.d("DEBUG", "UserId function called")
        retrofit.getUserInfoByUsername(username).enqueue(
            object : Callback<List<UserInfo>> {
                override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                    Log.d("DEBUG", "Failed")
                    onResult(null)
                }
                override fun onResponse( call: Call<List<UserInfo>>, response: Response<List<UserInfo>>) {
                    Log.d("DEBUG", "Success")
                    Log.d("DEBUG", "username " + username)
                    Log.d("DEBUG", "Response raw " + response.raw())
                    Log.d("DEBUG", "Response error " + response.errorBody())
                    Log.d("DEBUG", "Response success " + response.isSuccessful)
                    Log.d("DEBUG", "Response body " + response.body())
                    val getUser = response.body()
                    Log.d("DEBUG", "url " + response.raw().request().url())
                    Log.d("DEBUG", "Response " + getUser)
                    onResult(getUser)
                }
            }
        )
    }

    /**
    // gets user info by id
    fun getUserById(id: UserID, onResult: (User?) -> Unit) {
        retrofit.getUserById(id).enqueue(
            object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<User>, response: Response<User>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }

        )
    }
    */

}
