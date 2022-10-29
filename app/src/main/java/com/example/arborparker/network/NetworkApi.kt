package com.example.arborparker.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkApi {
    @GET("/spots")
    suspend fun getSpots(): List<Spot>

    //@GET("/user/:id/")
    //suspend fun getSpots(): List<User>

    @POST("/user/add-new-user")
    // fun addUser(@Body userdata: User): UserID
    fun addUser(@Body userdata: User): Call<User>
}