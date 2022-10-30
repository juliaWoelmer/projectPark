package com.example.arborparker

import android.service.autofill.UserData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arborparker.network.RetrofitClient
import com.example.arborparker.network.RetrofitClient.retrofit
import com.example.arborparker.network.Spot
import com.example.arborparker.network.User
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

}
