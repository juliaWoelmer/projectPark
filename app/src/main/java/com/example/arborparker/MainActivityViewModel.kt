package com.example.arborparker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arborparker.network.RetrofitClient
import com.example.arborparker.network.Spot
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val spotList: MutableLiveData<List<Spot>> = MutableLiveData()

    fun getSpots() {
        viewModelScope.launch {
            spotList.value = RetrofitClient.retrofit.getSpots()
        }
    }
}