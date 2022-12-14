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

    // public user id
    companion object {
        var user_id: Int? = null
    }

    val spotList: MutableLiveData<List<Spot>> = MutableLiveData()

    fun getSpots() {
        viewModelScope.launch {
            spotList.value = RetrofitClient.retrofit.getSpots()
        }
    }

    fun getSpotsOccupiedByUser(id: Int, onResult: (List<SpotJustId>?) -> Unit) {
        Log.d("DEBUG", "getSpotsOccupiedByUser function called")
        retrofit.getSpotsOccupiedByUser(id).enqueue(
            object : Callback<List<SpotJustId>> {
                override fun onFailure(call: Call<List<SpotJustId>>, t: Throwable) {
                    Log.d("DEBUG", "Failed")
                    onResult(null)
                }
                override fun onResponse( call: Call<List<SpotJustId>>, response: Response<List<SpotJustId>>) {
                    Log.d("DEBUG", "getSpotsOccupiedByUser Success")
                    Log.d("DEBUG", "id " + id)
                    Log.d("DEBUG", "Response raw " + response.raw())
                    Log.d("DEBUG", "Response error " + response.errorBody())
                    Log.d("DEBUG", "Response success " + response.isSuccessful)
                    Log.d("DEBUG", "Response body " + response.body())
                    Log.d("DEBUG", "url " + response.raw().request.url)

                    val occupied_spots = response.body()
                    onResult(occupied_spots)

                }
            }
        )
    }

    fun editSpotAvailability(id: Int, spotWithUser: SpotWithUser, onResult: (RowsAffected?) -> Unit) {
        Log.d("DEBUG", "editSpotAvailability function called")
        retrofit.editSpotAvailability(id, spotWithUser).enqueue(
            object : Callback<RowsAffected> {
                override fun onFailure(call: Call<RowsAffected>, t: Throwable) {
                    Log.d("DEBUG", "Edit Spot Availability Failed")
                    onResult(null)
                }
                override fun onResponse( call: Call<RowsAffected>, response: Response<RowsAffected>) {
                    Log.d("DEBUG", "Success editing spot availability at spotId: $id")
                    Log.d("DEBUG", "Response raw " + response.raw())
                    Log.d("DEBUG", "Response error " + response.errorBody())
                    Log.d("DEBUG", "Response success " + response.isSuccessful)
                    Log.d("DEBUG", "Response body " + response.body())
                    Log.d("DEBUG", "url " + response.raw().request.url)

                    val succ_response = response.body()
                    onResult(succ_response)
                }
            }
        )
    }

    // adds user to database
    fun addUser(userData: User, onResult: (UserId?) -> Unit){
        retrofit.addUser(userData).enqueue(
            object : Callback<UserId> {
                override fun onFailure(call: Call<UserId>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<UserId>, response: Response<UserId>) {
                    val addedUser = response.body()
                    Log.d("DEBUG", "Response body " + response.body())
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
                    Log.d("DEBUG", "url " + response.raw().request.url)
                    Log.d("DEBUG", "Response " + userInfo)
                    onResult(userInfo)
                }
            }
        )
    }

    // gets user id by username
    fun getUserInfoByUsername(username: String, onResult: (List<UserInfo>?) -> Unit) {
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
                    Log.d("DEBUG", "url " + response.raw().request.url)
                    Log.d("DEBUG", "Response " + getUser)
                    onResult(getUser)
                }
            }
        )
    }

    fun editUserProfile(id: Int, userProfileInfo: UserProfileInfo, onResult: (RowsAffected?) -> Unit) {
        Log.d("DEBUG", "editUserProfile function called")
        retrofit.editUserProfile(id, userProfileInfo).enqueue(
            object : Callback<RowsAffected> {
                override fun onFailure(call: Call<RowsAffected>, t: Throwable) {
                    Log.d("DEBUG", "Edit User Profile Failed")
                    onResult(null)
                }
                override fun onResponse( call: Call<RowsAffected>, response: Response<RowsAffected>) {
                    Log.d("DEBUG", "Success editing user profile")
                    Log.d("DEBUG", "Response raw " + response.raw())
                    Log.d("DEBUG", "Response error " + response.errorBody())
                    Log.d("DEBUG", "Response success " + response.isSuccessful)
                    Log.d("DEBUG", "Response body " + response.body())
                    Log.d("DEBUG", "url " + response.raw().request.url)
                    onResult(response.body())
                }
            }
        )
    }

    fun editUserPreferences(id: Int, userPreferencesInfo: UserPreferencesInfo, onResult: (RowsAffected?) -> Unit) {
        Log.d("DEBUG", "editUserPreferencees function called")
        retrofit.editUserPreferences(id, userPreferencesInfo).enqueue(
            object : Callback<RowsAffected> {
                override fun onFailure(call: Call<RowsAffected>, t: Throwable) {
                    Log.d("DEBUG", "Edit User Preferences Failed")
                    onResult(null)
                }
                override fun onResponse( call: Call<RowsAffected>, response: Response<RowsAffected>) {
                    Log.d("DEBUG", "Success editing user preferences")
                    Log.d("DEBUG", "Response raw " + response.raw())
                    Log.d("DEBUG", "Response error " + response.errorBody())
                    Log.d("DEBUG", "Response success " + response.isSuccessful)
                    Log.d("DEBUG", "Response body " + response.body())
                    Log.d("DEBUG", "url " + response.raw().request.url)
                    onResult(response.body())
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
